package com.partlymanu.refinedutils;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import com.partlymanu.refinedutils.block.ModBlocks;
import com.partlymanu.refinedutils.block.ModBlockEntities;
import com.partlymanu.refinedutils.block.ModItems;
import com.partlymanu.refinedutils.network.AccessModePacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import com.partlymanu.refinedutils.menu.ModMenuTypes;
import com.refinedmods.refinedstorage.neoforge.api.RefinedStorageNeoForgeApi;
import com.partlymanu.refinedutils.block.NetworkBackedItemHandler;


// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(RefinedUtils.MODID)
public class RefinedUtils {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "refinedutils";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public RefinedUtils(IEventBus modEventBus, ModContainer modContainer) {

        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (RefinedUtils) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        modEventBus.addListener(this::registerCapabilities);

        modEventBus.addListener(this::registerPackets);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().location().getNamespace().equals("refinedstorage")) {
            event.accept((ModItems.EXPOSED_INTERFACE_ITEM.get()));
        }

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    private void registerPackets(RegisterPayloadHandlersEvent event) {
        event.registrar(MODID)
                .playToServer(
                        AccessModePacket.TYPE,
                        AccessModePacket.CODEC,
                        AccessModePacket::handle
                );
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.EXPOSED_INTERFACE_BE.get(),
                (blockEntity, side) -> {
//                    RefinedUtils.LOGGER.info("IItemHandler capability queried from side: {}", side);
                    return new NetworkBackedItemHandler(blockEntity);
                }
        );
        event.registerBlockEntity(
                RefinedStorageNeoForgeApi.INSTANCE.getNetworkNodeContainerProviderCapability(),
                ModBlockEntities.EXPOSED_INTERFACE_BE.get(),
                (blockEntity, side) -> blockEntity.getContainerProvider()
        );
    }
}
