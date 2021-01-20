package net.alwinfy.mana2rf.mixin;

import com.google.common.collect.Iterables;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.energy.CapabilityEnergy;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.ManaItemHandler;

import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.bauble.ItemManaRing;
import vazkii.botania.common.item.ItemManaTablet;

import net.alwinfy.mana2rf.ItemStackMixinUtil;
import net.alwinfy.mana2rf.FEItemWrapper;

@Mixin({ ItemManaRing.class, ItemManaTablet.class })
public abstract class MixinInventoryManaDispensers extends Item implements IManaItem {
	private final int SPEED = 50; // half the speed of pool filling

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
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
		if (!recv.isEmpty()) {
			recv.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
				if (storage.getEnergyStored() < storage.getMaxEnergyStored()) {
					int feSent = storage.receiveEnergy(FEItemWrapper.MULTIPLIER * Math.min(SPEED, getMana(stack)), false);
					addMana(stack, -((feSent - 1) / FEItemWrapper.MULTIPLIER + 1));
				}
			});
		}
	}
}
