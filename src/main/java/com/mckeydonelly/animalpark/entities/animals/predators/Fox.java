package com.mckeydonelly.animalpark.entities.animals.predators;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Fox extends AnimalImpl {
    public Fox(Position position) {
        super(position, SettingsService.getAnimalByName(Fox.class.getSimpleName()).getAnimalProperties());
    }
}
