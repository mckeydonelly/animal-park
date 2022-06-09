package com.mckeydonelly.animalpark.entities.animals.vegetarians;

import com.mckeydonelly.animalpark.entities.EntityFactory;
import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Rabbit extends AnimalImpl {
    public Rabbit(Position position, EntityFactory entityFactory) {
        super(position, entityFactory, SettingsService.getAnimalByName(Rabbit.class.getSimpleName()).getAnimalProperties());
    }
}
