package com.mckeydonelly.animalpark.entities;

import com.mckeydonelly.animalpark.settings.SettingsService;

/**
 * Список сущностей для игры с максимальным количеством животных на локации и эмодзи.
 */
public enum EntityType {
    WOLF("Wolf"),
    SNAKE("Snake"),
    FOX("Fox"),
    BEAR("Bear"),
    EAGLE("Eagle"),
    HORSE("Horse"),
    DEER("Deer"),
    RABBIT("Rabbit"),
    MOUSE("Mouse"),
    GOAT("Goat"),
    SHEEP("Sheep"),
    BOAR("Boar"),
    DUCK("Duck"),
    LARVA("Larva"),
    BUFFALO("Buffalo"),
    BUSH("Bush");

    EntityType(String name) {
        this.name = name;
        this.maxEntitiesByLocation = SettingsService.getAnimalByName(name).getMaxByLocation();
        this.emoji = SettingsService.getAnimalByName(name).getEmoji();
    }

    private final String name;
    private final int maxEntitiesByLocation;
    private final String emoji;

    public String getName() {
        return name;
    }

    public int getMaxEntitiesByLocation () {
        return maxEntitiesByLocation;
    }

    public String getEmoji() {
        return emoji;
    }
}
