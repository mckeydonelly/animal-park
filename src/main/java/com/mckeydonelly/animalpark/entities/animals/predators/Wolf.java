package com.mckeydonelly.animalpark.entities.animals.predators;

import com.mckeydonelly.animalpark.entities.EntityFactory;
import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Wolf extends AnimalImpl {
    public Wolf(Position position, EntityFactory entityFactory) {
        super(position, entityFactory, SettingsService.getAnimalByName(Wolf.class.getSimpleName()).getAnimalProperties());
    }
}
