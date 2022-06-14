package com.mckeydonelly.animalpark.entities;

import com.mckeydonelly.animalpark.settings.SettingsService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Хранит и рассчитывает вероятность потребления пищи для животных.
 */
public class EatingProcessor {
    private final Map<String, Map<String, Integer>> chanceMap;
    private static final int MAX_CHANCE_VALUE = 100;

    public EatingProcessor(SettingsService settingsService) {
        this.chanceMap = new HashMap<>();
        settingsService.getAnimalSettings()
                .getAnimals()
                .forEach((name, values) -> chanceMap.put(name, values.getEatChance()));
    }

    /**
     * Возвращает вероятность потребления пищи для животного.
     * @param eater животное, которое потребляет пищу.
     * @param eaten животное, которое пытаются съесть.
     * @return вероятность потребления пищи для животного.
     */
    public boolean getEatResult(Entity eater, Entity eaten) {
        int eaterChance = chanceMap.get(eater.getClass().getSimpleName()).get(eaten.getClass().getSimpleName());

        if (eaterChance == 0) {
            return false;
        }

        return ThreadLocalRandom.current().nextInt(MAX_CHANCE_VALUE) < eaterChance;
    }

    /**
     * Возвращает для конкретного животного список типов животных, которых оно может съесть.
     * @param eater животное, которое потребляет пищу.
     * @return список типов животных, которых оно может съесть.
     */
    public Set<String> getEatableList(Entity eater) {
        return new HashSet<>(chanceMap.get(eater.getClass().getSimpleName()).keySet());
    }
}
