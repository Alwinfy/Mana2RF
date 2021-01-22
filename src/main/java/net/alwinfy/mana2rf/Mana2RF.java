package net.alwinfy.mana2rf;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vazkii.botania.api.mana.IManaItem;

@Mod(Mana2RF.MOD_ID)
public class Mana2RF {
	public static final Logger LOGGER = LogManager.getLogger();

	public static final String MOD_ID = "mana2rf";

	public static final ResourceLocation manaCapID = new ResourceLocation(MOD_ID, "mana_to_rf");

	public Mana2RF() {
		MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, this::setup);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BalanceConfig.CONFIG_SPEC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::reloadConfig);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadConfig);
		MinecraftForge.EVENT_BUS.addListener(this::registerCommands);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void registerCommands(RegisterCommandsEvent event) {
		Mana2RFCommand.register(event.getDispatcher());
	}

	private void setup(AttachCapabilitiesEvent<ItemStack> event) {
		ItemStack stack = event.getObject();
		Item item = stack.getItem();
		if (item instanceof IManaItem) {
			IManaItem manaItem = (IManaItem) item;
			event.addCapability(manaCapID, new ICapabilityProvider() {
				@Override
				public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction facing) {
					if (!manaItem.isNoExport(stack) && BalanceConfig.canConvert(stack)) {
						return CapabilityEnergy.ENERGY.orEmpty(cap, LazyOptional.of(() -> new ManaItemWrapper(stack, manaItem)));
					}
					return LazyOptional.empty();
				}
			});
		}
	}

	private void reloadConfig(ModConfig.Reloading event) {
		if (event.getConfig().getSpec() == BalanceConfig.CONFIG_SPEC) {
			BalanceConfig.bakeConfig();
		}
	}

	private void loadConfig(ModConfig.Loading event) {
		if (event.getConfig().getSpec() == BalanceConfig.CONFIG_SPEC) {
			BalanceConfig.bakeConfig();
		}
	}
}
