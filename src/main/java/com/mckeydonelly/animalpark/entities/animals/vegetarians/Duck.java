package com.mckeydonelly.animalpark.entities.animals.vegetarians;

import com.mckeydonelly.animalpark.entities.EntityFactory;
import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Duck extends AnimalImpl {
    public Duck(Position position, EntityFactory entityFactory) {
        super(position, entityFactory, SettingsService.getAnimalByName(Duck.class.getSimpleName()).getAnimalProperties());
    }
}
