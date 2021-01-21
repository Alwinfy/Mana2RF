package net.alwinfy.mana2rf.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.item.ItemManaMirror;

import net.alwinfy.mana2rf.ItemStackMixinUtil;

@Mixin(ItemManaMirror.class) 
public abstract class MixinItemManaMirror implements IManaItem {
	@Inject(method = "inventoryTick(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;IZ)V", at = @At("HEAD"))
	public void injectInventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected, CallbackInfo ci) {
		ItemStackMixinUtil.tickItem(this, stack, entity);
	}
}
