package net.alwinfy.mana2rf;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;

import org.apache.commons.lang3.tuple.Pair;

public class BalanceConfig {
	public final ForgeConfigSpec.IntValue conversionRateSpec;
	public final ForgeConfigSpec.IntValue itemDischargeSpeedSpec;
	public final ForgeConfigSpec.BooleanValue fluxfieldGatingSpec;

	public BalanceConfig(ForgeConfigSpec.Builder builder) {
		conversionRateSpec = builder
			.comment("The amount of FE produced by one micromanapool (one millionth of a mana pool) of mana.")
			.defineInRange("conversion_rate", 10, 1, 100);
		itemDischargeSpeedSpec = builder
			.comment("The amount of FE that mana items will discharge into FE items per tick.")
			.defineInRange("item_discharge_speed", 10000, 0, 2000000);
		fluxfieldGatingSpec = builder
			.comment("Does a mana item need to be right-clicked on a Mana Fluxfield before it can produce FE?")
			.define("fluxfield_gating", false);
	}

	public static final BalanceConfig CONFIG;
	public static final ForgeConfigSpec CONFIG_SPEC;

	public static int conversionRate = 10;
	public static int itemDischargeSpeed = 10000;
	public static boolean shouldFluxfieldGate = false;

	public static boolean canConvert(ItemStack stack) {
		return !shouldFluxfieldGate || (stack.hasTag() && stack.getTag().getBoolean(ItemStackMixinUtil.CONVERT_TAG));
	}

	public static void bakeConfig() {
		conversionRate = CONFIG.conversionRateSpec.get();
		itemDischargeSpeed = CONFIG.itemDischargeSpeedSpec.get();
		shouldFluxfieldGate = CONFIG.fluxfieldGatingSpec.get();
	}

	static {
		Pair<BalanceConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(BalanceConfig::new);
		CONFIG = specPair.getLeft();
		CONFIG_SPEC = specPair.getRight();
	}
}
