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
    public Entity createEntity(EntityType entityType, Position position) {
        return switch (entityType) {
            case BEAR -> new Bear(position, this);
            case EAGLE -> new Eagle(position, this);
            case FOX -> new Fox(position, this);
            case SNAKE -> new Snake(position, this);
            case WOLF -> new Wolf(position, this);
            case BOAR -> new Boar(position, this);
            case BUFFALO -> new Buffalo(position, this);
            case DEER -> new Deer(position, this);
            case DUCK -> new Duck(position, this);
            case GOAT -> new Goat(position, this);
            case HORSE -> new Horse(position, this);
            case LARVA -> new Larva(position, this);
            case MOUSE -> new Mouse(position, this);
            case RABBIT -> new Rabbit(position, this);
            case SHEEP -> new Sheep(position, this);
            case BUSH -> new Bush();
        };
    }
}
