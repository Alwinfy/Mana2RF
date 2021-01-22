package net.alwinfy.mana2rf;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class Mana2RFCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> commandBuilder = Commands.literal("mana2rf")
				.requires(source -> source.hasPermissionLevel(2))
				.then(Commands.literal("setConversionRate")
						.then(Commands.argument("conversion_rate", IntegerArgumentType.integer(1, 100)
						).executes(context -> {
							BalanceConfig.conversionRate = context.getArgument("conversion_rate", Integer.class);
							BalanceConfig.CONFIG.conversionRateSpec.set(BalanceConfig.conversionRate);
							return Command.SINGLE_SUCCESS;
						})))
				.then(Commands.literal("setItemDischargeSpeed")
						.then(Commands.argument("discharge_speed", IntegerArgumentType.integer(0, 2000000)
						).executes(context -> {
							BalanceConfig.itemDischargeSpeed = context.getArgument("discharge_speed", Integer.class);
							BalanceConfig.CONFIG.itemDischargeSpeedSpec.set(BalanceConfig.itemDischargeSpeed);
							return Command.SINGLE_SUCCESS;
						})))
				.then(Commands.literal("setFluxfieldGating")
						.then(Commands.argument("fluxfield_gating", BoolArgumentType.bool()
						).executes(context -> {
							BalanceConfig.shouldFluxfieldGate = context.getArgument("fluxfield_gating", Boolean.class);
							BalanceConfig.CONFIG.fluxfieldGatingSpec.set(BalanceConfig.shouldFluxfieldGate);
							return Command.SINGLE_SUCCESS;
						})));
		LiteralCommandNode<CommandSource> command = dispatcher.register(commandBuilder);
	}
}

