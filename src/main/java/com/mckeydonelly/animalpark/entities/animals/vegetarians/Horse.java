package com.mckeydonelly.animalpark.entities.animals.vegetarians;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Horse extends AnimalImpl {
    public Horse(Position position) {
        super(position, SettingsService.getAnimalByName(Horse.class.getSimpleName()).getAnimalProperties());
    }
}
