package com.mckeydonelly.animalpark.entities;

import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.animal.AnimalProperties;

/**
 * Интерфейс для создания сущностей.
 */
public interface UnitFactory {
    Unit createUnit(String entityType, AnimalProperties animalProperties, Position position);
}
