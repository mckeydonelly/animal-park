package com.mckeydonelly.animalpark.entities;

import com.mckeydonelly.animalpark.map.Position;

/**
 * Интерфейс для создания сущностей.
 */
public interface EntityFactory {
    Entity createEntity(String entityType, Position position);
}
