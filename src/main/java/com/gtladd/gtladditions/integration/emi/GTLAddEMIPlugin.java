package com.gtladd.gtladditions.integration.emi;

import com.gtladd.gtladditions.common.register.GTLAddMaterial;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;

@EmiEntrypoint
public class GTLAddEMIPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry emiRegistry) {
        emiRegistry.addEmiStack(EmiStack.of(GTLAddMaterial.MINING_ESSENCE.getBucket()));
        emiRegistry.addEmiStack(EmiStack.of(GTLAddMaterial.TREASURES_ESSENCE.getBucket()));
        emiRegistry.addEmiStack(EmiStack.of(GTLAddMaterial.CRYSTALLINE_PROTOPLASM.getBucket()));
    }
}
