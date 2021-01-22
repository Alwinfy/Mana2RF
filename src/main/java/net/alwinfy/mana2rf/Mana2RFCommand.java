package net.alwinfy.mana2rf;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;

public class Mana2RFCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> commandBuilder = Commands.literal("mana2rf")
			.then(Commands.literal("conversion_rate")
				.then(Commands.argument("conversion_rate", IntegerArgumentType.integer(1, 100))
					.requires(source -> source.hasPermissionLevel(2))
					.executes(context -> {
						BalanceConfig.conversionRate = context.getArgument("conversion_rate", Integer.class);
						BalanceConfig.CONFIG.conversionRateSpec.set(BalanceConfig.conversionRate);
						return Command.SINGLE_SUCCESS;
					}))
				.executes(context -> {
					context.getSource().sendFeedback(new StringTextComponent("conversion_rate = " + BalanceConfig.conversionRate), false);
					return Command.SINGLE_SUCCESS;
				})
			.then(Commands.literal("item_discharge_speed")
				.then(Commands.argument("discharge_speed", IntegerArgumentType.integer(0, 2000000))
					.requires(source -> source.hasPermissionLevel(2))
					.executes(context -> {
						BalanceConfig.itemDischargeSpeed = context.getArgument("discharge_speed", Integer.class);
						BalanceConfig.CONFIG.itemDischargeSpeedSpec.set(BalanceConfig.itemDischargeSpeed);
						return Command.SINGLE_SUCCESS;
					}))
				.executes(context -> {
					context.getSource().sendFeedback(new StringTextComponent("item_discharge_speed = " + BalanceConfig.itemDischargeSpeed), false);
					return Command.SINGLE_SUCCESS;
				}))
			.then(Commands.literal("fluxfield_gating")
				.then(Commands.argument("fluxfield_gating", BoolArgumentType.bool())
					.requires(source -> source.hasPermissionLevel(2))
					.executes(context -> {
						BalanceConfig.shouldFluxfieldGate = context.getArgument("fluxfield_gating", Boolean.class);
						BalanceConfig.CONFIG.fluxfieldGatingSpec.set(BalanceConfig.shouldFluxfieldGate);
						return Command.SINGLE_SUCCESS;
					}))
				.executes(context -> {
					context.getSource().sendFeedback(new StringTextComponent("fluxfield_gating = " + BalanceConfig.shouldFluxfieldGate), false);
					return Command.SINGLE_SUCCESS;
				})));
		LiteralCommandNode<CommandSource> command = dispatcher.register(commandBuilder);
	}
}

