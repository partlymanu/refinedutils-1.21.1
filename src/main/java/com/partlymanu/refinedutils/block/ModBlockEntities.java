package com.partlymanu.refinedutils.block;

import com.partlymanu.refinedutils.RefinedUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, RefinedUtils.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExposedInterfaceBlockEntity>> EXPOSED_INTERFACE_BE =
            BLOCK_ENTITY_TYPES.register("exposed_interface_be",
                    () -> BlockEntityType.Builder
                            .of(ExposedInterfaceBlockEntity::new, ModBlocks.EXPOSED_INTERFACE.get())
                            .build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
