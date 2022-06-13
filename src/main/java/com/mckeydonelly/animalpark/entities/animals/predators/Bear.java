package com.mckeydonelly.animalpark.entities.animals.predators;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Bear extends AnimalImpl {
    public Bear(Position position) {
        super(position, SettingsService.getAnimalByName(Bear.class.getSimpleName()).getAnimalProperties());
    }
}
