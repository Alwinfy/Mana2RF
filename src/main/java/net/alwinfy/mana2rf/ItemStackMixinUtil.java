package net.alwinfy.mana2rf;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;

public final class ItemStackMixinUtil {
	public static final String TARGET = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;";
	public static final String CONVERT_TAG = "mana2rf:can_convert";
	public static final Item energyItemWrapper = new FEItemWrapper();

	public static Item redirGetItem(ItemStack in) {
		return in.getCapability(CapabilityEnergy.ENERGY).isPresent() ? energyItemWrapper : in.getItem();
	}

}
