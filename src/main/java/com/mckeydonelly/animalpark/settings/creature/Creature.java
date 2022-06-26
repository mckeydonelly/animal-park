package com.mckeydonelly.animalpark.settings.creature;

import com.mckeydonelly.animalpark.activity.ActionTypes;
import com.mckeydonelly.animalpark.creature.CreatureTypes;

import java.util.List;
import java.util.Map;

/**
 * POJO class for storing parameters and settings of creatures
 */
public class Creature {
    private CreatureProperties creatureProperties;
    private CreatureTypes type;
    private List<ActionTypes> availableActions;
    private String emoji;
    private int maxByLocation;
    private Map<String, Integer> eatChance;

    public CreatureProperties getCreatureProperties() {
        return creatureProperties;
    }

    public String getEmoji() {
        return emoji;
    }

    public CreatureTypes getType() {
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
