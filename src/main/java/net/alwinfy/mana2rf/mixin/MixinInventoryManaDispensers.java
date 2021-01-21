package net.alwinfy.mana2rf.mixin;

import com.google.common.collect.Iterables;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.energy.CapabilityEnergy;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.item.equipment.bauble.ItemManaRing;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.ItemManaMirror;

import net.alwinfy.mana2rf.FEItemWrapper;

@Mixin({ ItemManaRing.class, ItemManaTablet.class, ItemManaMirror.class })
public abstract class MixinInventoryManaDispensers extends Item implements IManaItem {
	private static final int ITEM_DISCHARGE_SPEED = 200;

	protected MixinInventoryManaDispensers() {
		super(new Item.Properties());
		throw new AssertionError();
	}

	@Unique
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {}

	@Inject(method = "inventoryTick(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;IZ)V", at = @At("HEAD"))
	@SuppressWarnings("target")
	public void injectInventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected, CallbackInfo ci) {
		if (getMana(stack) <= 0 || !(entity instanceof PlayerEntity)) {
			return;
		}
		PlayerEntity player = (PlayerEntity) entity;

		for (ItemStack recv : Iterables.concat(player.inventory.mainInventory, player.inventory.offHandInventory)) {
			moveMana(stack, recv);
		}
		IInventory accessories = BotaniaAPI.instance().getAccessoriesInventory(player);
		for (int i = 0; i < accessories.getSizeInventory(); i++) {
			moveMana(stack, accessories.getStackInSlot(i));
		}
	}
	private void moveMana(ItemStack stack, ItemStack recv) {
		if (!recv.isEmpty() && !(recv.getItem() instanceof IManaItem)) {
			recv.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
				if (storage.getEnergyStored() < storage.getMaxEnergyStored()) {
					int feSent = storage.receiveEnergy(FEItemWrapper.MULTIPLIER * Math.min(ITEM_DISCHARGE_SPEED, getMana(stack)), false);
					addMana(stack, -((feSent - 1) / FEItemWrapper.MULTIPLIER + 1));
				}
			});
		}
	}
}
