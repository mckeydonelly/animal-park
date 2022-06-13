package com.mckeydonelly.animalpark.entities.animals.predators;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Wolf extends AnimalImpl {
    public Wolf(Position position) {
        super(position, SettingsService.getAnimalByName(Wolf.class.getSimpleName()).getAnimalProperties());
    }
}
