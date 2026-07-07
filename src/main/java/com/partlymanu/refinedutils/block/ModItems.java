package com.partlymanu.refinedutils.block;

import com.partlymanu.refinedutils.RefinedUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import com.refinedmods.refinedstorage.common.support.NetworkNodeBlockItem;
import net.minecraft.network.chat.Component;
public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, RefinedUtils.MODID);

    public static final DeferredHolder<Item, NetworkNodeBlockItem> EXPOSED_INTERFACE_ITEM =
            ITEMS.register("exposed_interface",
                    () -> new NetworkNodeBlockItem(
                            ModBlocks.EXPOSED_INTERFACE.get(),
                            Component.translatable("block.refinedutils.exposed_interface.help")
                    )
            );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
