package com.mckeydonelly.animalpark.entities;

import com.mckeydonelly.animalpark.entities.plants.Bush;
import com.mckeydonelly.animalpark.map.Position;

/**
 * Фабрика генерации растений.
 */
public class PlantsFactory implements EntityFactory {
    @Override
    public Entity createEntity(String entityType, Position position) {
        return new Bush();
    }
}
