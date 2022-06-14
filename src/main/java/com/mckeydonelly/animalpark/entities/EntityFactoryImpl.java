package com.mckeydonelly.animalpark.entities;

import com.mckeydonelly.animalpark.entities.animals.predators.*;
import com.mckeydonelly.animalpark.entities.animals.vegetarians.*;
import com.mckeydonelly.animalpark.entities.plants.Bush;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.animal.AnimalProperties;

/**
 * Фабрика для создания сущностей
 */
public class EntityFactoryImpl implements EntityFactory {

    @Override
    public Entity createEntity(String entityType, AnimalProperties animalProperties, Position position) {
        return switch (entityType) {
            case "Bear" -> new Bear(position, animalProperties);
            case "Eagle" -> new Eagle(position, animalProperties);
            case "Fox" -> new Fox(position, animalProperties);
            case "Snake" -> new Snake(position, animalProperties);
            case "Wolf" -> new Wolf(position, animalProperties);
            case "Boar" -> new Boar(position, animalProperties);
            case "Buffalo" -> new Buffalo(position, animalProperties);
            case "Deer" -> new Deer(position, animalProperties);
            case "Duck" -> new Duck(position, animalProperties);
            case "Goat" -> new Goat(position, animalProperties);
            case "Horse" -> new Horse(position, animalProperties);
            case "Larva" -> new Larva(position, animalProperties);
            case "Mouse" -> new Mouse(position, animalProperties);
            case "Rabbit" -> new Rabbit(position, animalProperties);
            case "Sheep" -> new Sheep(position, animalProperties);
            case "Bush" -> new Bush(animalProperties);
            default -> throw new IllegalArgumentException("Unknown entity type: " + entityType);
        };
    }
}
