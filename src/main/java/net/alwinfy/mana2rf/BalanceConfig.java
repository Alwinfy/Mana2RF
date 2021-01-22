package net.alwinfy.mana2rf;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;

import org.apache.commons.lang3.tuple.Pair;

public class BalanceConfig {
	public final ForgeConfigSpec.IntValue conversionRate;
	public final ForgeConfigSpec.BooleanValue fluxfieldGating;

	public BalanceConfig(ForgeConfigSpec.Builder builder) {
		conversionRate = builder
			.comment("The amount of FE produced by one micromanapool (one millionth of a mana pool) of mana.")
			.defineInRange("conversion_rate", 10, 1, 100);
		fluxfieldGating = builder
			.comment("Does a mana item need to be right-clicked on a Mana Fluxfield to produce FE?")
			.define("fluxfield_gating", false);
	}

	public static final BalanceConfig CONFIG;
	public static final ForgeConfigSpec CONFIG_SPEC;

	public static int conversionRate() {
		int val = CONFIG.conversionRate.get();
		Mana2RF.LOGGER.warn(val);
		return CONFIG.conversionRate.get();
	}

	public static boolean canConvert(ItemStack stack) {
		return !CONFIG.fluxfieldGating.get() || (stack.hasTag() && stack.getTag().getBoolean(ItemStackMixinUtil.CONVERT_TAG));
	}

	static {
		Pair<BalanceConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(BalanceConfig::new);
		CONFIG = specPair.getLeft();
		CONFIG_SPEC = specPair.getRight();
	}
}
