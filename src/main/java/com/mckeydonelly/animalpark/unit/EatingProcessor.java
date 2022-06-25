package com.mckeydonelly.animalpark.unit;

import com.mckeydonelly.animalpark.settings.SettingsService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Stores and calculates the chance of food consumption for creatures.
 */
public class EatingProcessor {
    private static final int MAX_CHANCE_VALUE = 100;
    private final Map<String, Map<String, Integer>> chanceMap;

    public EatingProcessor(SettingsService settingsService) {
        this.chanceMap = new HashMap<>();
        settingsService.getAnimalSettings()
                .getUnits()
                .forEach((name, values) -> chanceMap.put(name, values.getEatChance()));
    }

    /**
     * Calculate chance of food consumption for creature
     *
     * @param eater eater
     * @param eaten eaten
     * @return true for success eat
     */
    public boolean getEatResult(String eater, String eaten) {
        int eaterChance = chanceMap.get(eater).get(eaten);

        if (eaterChance == 0) {
            return false;
        }

        return ThreadLocalRandom.current().nextInt(MAX_CHANCE_VALUE) < eaterChance;
    }

    /**
     * Returns for a specific creature a list of the types of creature it can eat.
     *
     * @param eater eater
     * @return list of types
     */
    public Set<String> getEatableList(String eater) {
        return chanceMap.get(eater).entrySet().stream()
                .filter(chance -> chance.getValue() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
