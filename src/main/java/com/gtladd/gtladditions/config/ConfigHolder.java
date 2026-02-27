package com.gtladd.gtladditions.config;

import com.gtladd.gtladditions.GTLAdditions;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = GTLAdditions.MOD_ID)
public class ConfigHolder {

    public static ConfigHolder INSTANCE;
    private static final Object LOCK = new Object();

    public static void init() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = Configuration.registerConfig(ConfigHolder.class, ConfigFormats.yaml()).getConfigInstance();
            }
        }
    }

    @Configurable
    @Configurable.Comment({ "均分模式(EQUALLY_DIVIDED_MODE)、极限模式(EXTREME_MODE)",
            "Equally Divided Recipes Mode(EQUALLY_DIVIDED_MODE), Extreme Mode(EXTREME_MODE)" })
    public GTLAddMachineMode isMultiple = GTLAddMachineMode.EQUALLY_DIVIDED_MODE;
    @Configurable
    @Configurable.Range(min = 5, max = 200)
    public int limitDuration = 20;
}
