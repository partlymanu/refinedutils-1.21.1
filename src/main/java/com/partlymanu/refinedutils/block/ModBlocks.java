package com.partlymanu.refinedutils.block;

import com.partlymanu.refinedutils.RefinedUtils;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.BuiltInRegistries;


public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(BuiltInRegistries.BLOCK, RefinedUtils.MODID);

    public static final DeferredHolder<Block, ExposedInterfaceBlock> EXPOSED_INTERFACE =
            BLOCKS.register("exposed_interface", ExposedInterfaceBlock::new);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
