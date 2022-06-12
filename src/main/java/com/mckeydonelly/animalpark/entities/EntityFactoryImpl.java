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
            case "Bear" -> new Bear(position, this);
            case "Eagle" -> new Eagle(position, this);
            case "Fox" -> new Fox(position, this);
            case "Snake" -> new Snake(position, this);
            case "Wolf" -> new Wolf(position, this);
            case "Boar" -> new Boar(position, this);
            case "Buffalo" -> new Buffalo(position, this);
            case "Deer" -> new Deer(position, this);
            case "Duck" -> new Duck(position, this);
            case "Goat" -> new Goat(position, this);
            case "Horse" -> new Horse(position, this);
            case "Larva" -> new Larva(position, this);
            case "Mouse" -> new Mouse(position, this);
            case "Rabbit" -> new Rabbit(position, this);
            case "Sheep" -> new Sheep(position, this);
            case "Bush" -> new Bush();
            default -> throw new IllegalArgumentException("Unknown entity type: " + entityType);
        };
    }
}
