package com.mckeydonelly.animalpark.settings.animal;

import java.util.Map;

/**
 * POJO класс для хранения параметров и настроек животных
 */
public class Animal {
    private AnimalProperties animalProperties;
    private String emoji;
    private int maxByLocation;
    private Map<String, Integer> eatChance;

    public AnimalProperties getAnimalProperties() {
        return animalProperties;
    }

    public String getEmoji() {
        return emoji;
    }

    public int getMaxByLocation() {
        return maxByLocation;
    }

    public Map<String, Integer> getEatChance() {
        return eatChance;
    }
}
