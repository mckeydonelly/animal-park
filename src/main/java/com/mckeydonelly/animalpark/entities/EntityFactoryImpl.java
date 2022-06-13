package com.mckeydonelly.animalpark.entities;

import com.mckeydonelly.animalpark.entities.animals.predators.*;
import com.mckeydonelly.animalpark.entities.animals.vegetarians.*;
import com.mckeydonelly.animalpark.entities.plants.Bush;
import com.mckeydonelly.animalpark.map.Position;

/**
 * Фабрика для создания сущностей
 */
public class EntityFactoryImpl implements EntityFactory {

    @Override
    public Entity createEntity(String entityType, Position position) {
        return switch (entityType) {
            case "Bear" -> new Bear(position);
            case "Eagle" -> new Eagle(position);
            case "Fox" -> new Fox(position);
            case "Snake" -> new Snake(position);
            case "Wolf" -> new Wolf(position);
            case "Boar" -> new Boar(position);
            case "Buffalo" -> new Buffalo(position);
            case "Deer" -> new Deer(position);
            case "Duck" -> new Duck(position);
            case "Goat" -> new Goat(position);
            case "Horse" -> new Horse(position);
            case "Larva" -> new Larva(position);
            case "Mouse" -> new Mouse(position);
            case "Rabbit" -> new Rabbit(position);
            case "Sheep" -> new Sheep(position);
            case "Bush" -> new Bush();
            default -> throw new IllegalArgumentException("Unknown entity type: " + entityType);
        };
    }
}
