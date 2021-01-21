package net.alwinfy.mana2rf;

import com.google.common.collect.Iterables;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.energy.CapabilityEnergy;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaItem;

import net.alwinfy.mana2rf.FEItemWrapper;

public final class ItemStackMixinUtil {
	public static final String TARGET = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;";
	private static final int ITEM_DISCHARGE_SPEED = 200;
	public static final Item energyItemWrapper = new FEItemWrapper();

	public static Item redirGetItem(ItemStack in) {
		return in.getCapability(CapabilityEnergy.ENERGY).isPresent() ? energyItemWrapper : in.getItem();
	}

	public static void tickItem(IManaItem item, ItemStack stack, Entity entity) {
		if (item.getMana(stack) <= 0 || !(entity instanceof PlayerEntity)) {
			return;
		}
		PlayerEntity player = (PlayerEntity) entity;

		for (ItemStack recv : Iterables.concat(player.inventory.mainInventory, player.inventory.offHandInventory)) {
			moveMana(item, stack, recv);
		}
		IInventory accessories = BotaniaAPI.instance().getAccessoriesInventory(player);
		for (int i = 0; i < accessories.getSizeInventory(); i++) {
			moveMana(item, stack, accessories.getStackInSlot(i));
		}
	}
	public static void moveMana(IManaItem item, ItemStack stack, ItemStack recv) {
		if (!recv.isEmpty() && !(recv.getItem() instanceof IManaItem)) {
			recv.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
				if (storage.getEnergyStored() < storage.getMaxEnergyStored()) {
					int feSent = storage.receiveEnergy(FEItemWrapper.MULTIPLIER * Math.min(ITEM_DISCHARGE_SPEED, item.getMana(stack)), false);
					item.addMana(stack, -((feSent - 1) / FEItemWrapper.MULTIPLIER + 1));
				}
			});
		}
	}
}
