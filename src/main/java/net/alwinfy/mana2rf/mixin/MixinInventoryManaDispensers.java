package net.alwinfy.mana2rf.mixin;

import com.google.common.collect.Iterables;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.item.equipment.bauble.ItemManaRing;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.ItemManaMirror;

import net.alwinfy.mana2rf.BalanceConfig;
import net.alwinfy.mana2rf.ItemStackMixinUtil;

import static vazkii.botania.common.block.ModBlocks.rfGenerator;
import static vazkii.botania.common.core.handler.ModSounds.ding;

@Mixin({ ItemManaRing.class, ItemManaTablet.class, ItemManaMirror.class })
public abstract class MixinInventoryManaDispensers extends Item implements IManaItem {
	protected MixinInventoryManaDispensers() {
		super(new Item.Properties());
		throw new AssertionError();
	}

	@Unique
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, world, entity, itemSlot, isSelected);
	}

	@SuppressWarnings("target")
	@Inject(method = "inventoryTick(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;IZ)V", at = @At("HEAD"))
	public void injectInventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected, CallbackInfo ci) {
		if (getMana(stack) <= 0 || !(entity instanceof PlayerEntity) || !BalanceConfig.canConvert(stack)) {
			return;
		}
		PlayerEntity player = (PlayerEntity) entity;

		for (ItemStack recv : Iterables.concat(player.inventory.mainInventory, player.inventory.offHandInventory)) {
			moveMana(stack, recv);
		}
		IInventory accessories = BotaniaAPI.instance().getAccessoriesInventory(player);
		for (int i = 0; i < accessories.getSizeInventory(); i++) {
			moveMana(stack, accessories.getStackInSlot(i));
		}
	}
	private void moveMana(ItemStack stack, ItemStack recv) {
		if (!recv.isEmpty() && !(recv.getItem() instanceof IManaItem)) {
			recv.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
				if (storage.getEnergyStored() < storage.getMaxEnergyStored()) {
					int feSent = storage.receiveEnergy(Math.min(BalanceConfig.itemDischargeSpeed, BalanceConfig.conversionRate * getMana(stack)), false);
					addMana(stack, -((feSent - 1) / BalanceConfig.conversionRate + 1));
				}
			});
		}
	}

	@Unique
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		return super.onItemUse(ctx);
	}

	@SuppressWarnings("target")
	@Inject(method = "onItemUse(Lnet/minecraft/item/ItemUseContext;)Lnet/minecraft/util/ActionResultType;", at = @At("HEAD"), cancellable = true)
	public void injectOnItemUse(ItemUseContext ctx, CallbackInfoReturnable<ActionResultType> cir) {
		ItemStack stack = ctx.getItem();
		BlockPos pos = ctx.getPos();
		if (!BalanceConfig.canConvert(stack) && ctx.getWorld().getBlockState(pos).isIn(rfGenerator)) {
			stack.getOrCreateTag().putBoolean(ItemStackMixinUtil.CONVERT_TAG, true);
			ctx.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), ding, SoundCategory.PLAYERS, 1F, 1F);
			cir.setReturnValue(ActionResultType.SUCCESS);
		}
	}
}
