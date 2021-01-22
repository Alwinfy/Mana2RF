package net.alwinfy.mana2rf;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.common.capabilities.Capability;

import vazkii.botania.api.mana.IManaItem;

public class ManaItemWrapper implements IEnergyStorage {
	private final ItemStack stack;
	private final IManaItem item;

	public ManaItemWrapper(ItemStack stack, IManaItem item) {
		this.stack = stack;
		this.item = item;
	}

	@Override
	public int getEnergyStored() {
		return item.getMana(stack) * BalanceConfig.conversionRate;
	}

	@Override
	public int getMaxEnergyStored() {
		return item.getMaxMana(stack) * BalanceConfig.conversionRate;
	}

	@Override
	public boolean canExtract() {
		return getEnergyStored() > 0;
	}

	@Override
	public int extractEnergy(int cap, boolean simulate) {
		int manaPull = Math.min(cap == 0 ? 0 : ((cap - 1) / BalanceConfig.conversionRate + 1), item.getMana(stack));
		if (!simulate) {
			item.addMana(stack, -manaPull);
		}
		return manaPull * BalanceConfig.conversionRate;
	}

	// No RF 2 mana in this good clean household
	@Override
	public boolean canReceive() {
		return false;
	}

	@Override
	public int receiveEnergy(int cap, boolean simulate) {
		return 0;
	}
}
