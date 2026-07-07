package com.partlymanu.refinedutils.block;

import com.partlymanu.refinedutils.RefinedUtils;
import com.refinedmods.refinedstorage.api.network.impl.node.SimpleNetworkNode;

public class ExposedInterfaceNetworkNode extends SimpleNetworkNode {

    private Runnable onActiveChanged;

    public ExposedInterfaceNetworkNode(long energyUsage) {
        super(energyUsage);
    }

    public void setOnActiveChanged(Runnable onActiveChanged) {
        this.onActiveChanged = onActiveChanged;
    }

    @Override
    protected void onActiveChanged(boolean newActive) {
        super.onActiveChanged(newActive);
        RefinedUtils.LOGGER.info("Node onActiveChanged: {}", newActive);
        if (onActiveChanged != null) {
            onActiveChanged.run();
        }
    }
}