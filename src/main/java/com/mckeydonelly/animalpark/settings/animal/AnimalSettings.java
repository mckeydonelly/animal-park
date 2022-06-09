package com.mckeydonelly.animalpark.settings.animal;

import java.util.Map;

/**
 * POJO класс для чтения настроек животных из YAML файла
 */
public class AnimalSettings {
    private Map<String, Animal> animals;

    public Map<String, Animal> getAnimals() {
        return this.animals;
    }
}
