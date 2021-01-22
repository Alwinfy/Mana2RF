package net.alwinfy.mana2rf.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import vazkii.botania.common.block.tile.mana.TileRFGenerator;

import net.alwinfy.mana2rf.BalanceConfig;

@Mixin(TileRFGenerator.class)
public class MixinTileRFGenerator {
	// BotaniaCopy: steal the 10 from TileRFGenerator
	@ModifyConstant(constant = @Constant(intValue = 10), method = { "getCurrentMana", "receiveMana", "func_73660_a" }, remap = false)
	public int dynamicRate(int old) {
		return BalanceConfig.conversionRate;
	}
}
