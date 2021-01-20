package net.alwinfy.mana2rf.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import vazkii.botania.common.block.tile.mana.TilePool;

import net.alwinfy.mana2rf.ItemStackMixinUtil;

@Mixin(TilePool.class)
public class MixinTilePool {
	@Redirect(at = @At(value = "INVOKE", target = ItemStackMixinUtil.TARGET), method = "tick")
	public Item redirGetItem(ItemStack in) {
		return ItemStackMixinUtil.redirGetItem(in);
	}
}
