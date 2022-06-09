package com.mckeydonelly.animalpark.entities.animals.predators;

import com.mckeydonelly.animalpark.entities.EntityFactory;
import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Snake extends AnimalImpl {
    public Snake(Position position, EntityFactory entityFactory) {
        super(position, entityFactory, SettingsService.getAnimalByName(Snake.class.getSimpleName()).getAnimalProperties());
    }
}
