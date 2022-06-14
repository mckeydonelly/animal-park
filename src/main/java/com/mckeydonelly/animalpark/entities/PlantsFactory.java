package com.mckeydonelly.animalpark.entities;

import com.mckeydonelly.animalpark.entities.plants.Bush;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.animal.AnimalProperties;

/**
 * Фабрика генерации растений.
 */
public class PlantsFactory implements EntityFactory {
    @Override
    public Entity createEntity(String entityType, AnimalProperties animalProperties, Position position) {
        return new Bush(animalProperties);
    }
}
