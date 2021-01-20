package net.alwinfy.mana2rf.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import vazkii.botania.common.entity.EntitySpark;

import net.alwinfy.mana2rf.ItemStackMixinUtil;

@Mixin(EntitySpark.class)
public class MixinEntitySpark {
	@Redirect(at = @At(value = "INVOKE", target = ItemStackMixinUtil.TARGET), method = "tick")
	public Item redirGetItem(ItemStack in) {
		return ItemStackMixinUtil.redirGetItem(in);
	}
}
