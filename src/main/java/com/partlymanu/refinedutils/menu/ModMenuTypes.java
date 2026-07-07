package com.partlymanu.refinedutils.menu;

import com.partlymanu.refinedutils.RefinedUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(BuiltInRegistries.MENU, RefinedUtils.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ExposedInterfaceMenu>> EXPOSED_INTERFACE =
            MENU_TYPES.register("exposed_interface",
                    () -> IMenuTypeExtension.create(ExposedInterfaceMenu::new)
            );

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}