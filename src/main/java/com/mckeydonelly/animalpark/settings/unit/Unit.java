package com.mckeydonelly.animalpark.settings.unit;

import com.mckeydonelly.animalpark.activity.ActionTypes;
import com.mckeydonelly.animalpark.unit.UnitTypes;

import java.util.List;
import java.util.Map;

/**
 * POJO class for storing parameters and settings of units
 */
public class Unit {
    private UnitProperties unitProperties;
    private UnitTypes type;
    private List<ActionTypes> availableActions;
    private String emoji;
    private int maxByLocation;
    private Map<String, Integer> eatChance;

    public UnitProperties getUnitProperties() {
        return unitProperties;
    }

    public String getEmoji() {
        return emoji;
    }

    public UnitTypes getType() {
        return type;
    }

    public List<ActionTypes> getAvailableActions() {
        return availableActions;
    }

    public int getMaxByLocation() {
        return maxByLocation;
    }

    public Map<String, Integer> getEatChance() {
        return eatChance;
    }
}
