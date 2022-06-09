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
    private static final Map<String, Map<String, Integer>> chanceMap;
    public static final int MAX_CHANCE_VALUE = 100;

    static {
        chanceMap = new HashMap<>();
        for (EntityType entityType : EntityType.values()) {
            chanceMap.put(entityType.getName(), new HashMap<>(SettingsService.getAnimalByName(entityType.getName()).getEatChance()));
        }
    }

    private EatingProcessor() {
    }

    /**
     * Возвращает вероятность потребления пищи для животного.
     * @param eater животное, которое потребляет пищу.
     * @param eaten животное, которое пытаются съесть.
     * @return вероятность потребления пищи для животного.
     */
    public static boolean getEatResult(Entity eater, Entity eaten) {
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
    public static Set<String> getEatableList(Entity eater) {
        return new HashSet<>(chanceMap.get(eater.getClass().getSimpleName()).keySet());
    }
}
