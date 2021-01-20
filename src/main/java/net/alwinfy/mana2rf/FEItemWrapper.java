package net.alwinfy.mana2rf;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.block.tile.mana.TileRFGenerator;

import java.lang.reflect.Field;

// Contractual guarantee: the ItemStack _will_ have CapabilityEnergy attached.
// TODO: Some sort of cache?
public class FEItemWrapper extends Item implements IManaItem {
	public static final int MULTIPLIER;
	static {
		int m = 10;
		try {
			Field f = TileRFGenerator.class.getField("MANA_TO_RF");
			f.setAccessible(true);
			m = f.getInt(null);
		} catch (ReflectiveOperationException ex) {}
		MULTIPLIER = m;
	}

	public FEItemWrapper() {
		super(new Properties());
	}

	public static IEnergyStorage unwrap(ItemStack stack) {
		return stack.getCapability(CapabilityEnergy.ENERGY).orElseThrow(NullPointerException::new);
	}

	@Override
	public int getMana(ItemStack stack) {
		IEnergyStorage storage = unwrap(stack);
		int maxStore = storage.getMaxEnergyStored();
		// Account for rounding error
		return maxStore / MULTIPLIER - (maxStore - storage.getEnergyStored()) / MULTIPLIER;
	}

	@Override
	public int getMaxMana(ItemStack stack) {
		return unwrap(stack).getMaxEnergyStored() / MULTIPLIER;
	}

	@Override
	public void addMana(ItemStack stack, int mana) {
		IEnergyStorage storage = unwrap(stack);
		if (mana > 0)
			storage.receiveEnergy(mana * MULTIPLIER, false);
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
		return true;
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack other) {
		return true;
	}

	@Override
	public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
		return false;
	}

	@Override
	public boolean canExportManaToItem(ItemStack stack, ItemStack other) {
		return false;
	}

	@Override
	public boolean isNoExport(ItemStack stack) {
		return true;
	}

}
