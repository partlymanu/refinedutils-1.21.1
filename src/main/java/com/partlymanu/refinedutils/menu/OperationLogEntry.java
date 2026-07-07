package com.partlymanu.refinedutils.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class OperationLogEntry {

    public enum Type {
        INSERT,
        EXTRACT
    }

    private final Type type;
    private final ItemStack stack;
    private final int amount;

    public OperationLogEntry(Type type, ItemStack stack, int amount) {
        this.type = type;
        this.stack = stack;
        this.amount = amount;
    }

    public Type getType() {
        return type;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getAmount() {
        return amount;
    }

    // For sending over the network to the client
    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeEnum(type);
        ItemStack.STREAM_CODEC.encode(buf, stack);
        buf.writeInt(amount);
    }

    // For reading from the network on the client
    public static OperationLogEntry fromBytes(RegistryFriendlyByteBuf buf) {
        Type type = buf.readEnum(Type.class);
        ItemStack stack = ItemStack.STREAM_CODEC.decode(buf);
        int amount = buf.readInt();
        return new OperationLogEntry(type, stack, amount);
    }
}