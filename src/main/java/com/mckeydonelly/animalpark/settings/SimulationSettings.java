package com.mckeydonelly.animalpark.settings;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains global simulation settings from property file
 */
public class SimulationSettings {

    private final Map<String, Integer> config = new HashMap<>();

    /**
     * Add setting by type
     * @param type setting type {@link com.mckeydonelly.animalpark.settings.SettingsType}
     * @param value value
     */
    public void add(SettingsType type, Integer value) {
        config.put(type.getTypeCode(), value);
    }

    /**
     * Get setting by type
     * @param type setting type {@link com.mckeydonelly.animalpark.settings.SettingsType}
     * @return value
     */
    public Integer get(SettingsType type) {
        return config.get(type.getTypeCode());
    }

}
