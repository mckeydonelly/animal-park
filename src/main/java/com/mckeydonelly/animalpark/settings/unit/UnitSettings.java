package com.mckeydonelly.animalpark.settings.unit;

import java.util.Map;

/**
 * POJO class for reading unit settings from YAML file
 */
public class UnitSettings {
    private Map<String, Unit> units;

    public Map<String, Unit> getUnits() {
        return units;
    }
}
