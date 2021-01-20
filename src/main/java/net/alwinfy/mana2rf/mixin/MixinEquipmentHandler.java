package net.alwinfy.mana2rf.mixin;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.core.handler.EquipmentHandler;

@Mixin(EquipmentHandler.class)
public class MixinEquipmentHandler {
	@Inject(method = "isAccessory", at = @At("RETURN"), cancellable = true, remap = false)
	public void isAccessoryOrRFUser(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
			cir.setReturnValue(true);
		}
	}
}
