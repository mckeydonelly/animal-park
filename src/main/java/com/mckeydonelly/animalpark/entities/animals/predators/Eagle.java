package com.mckeydonelly.animalpark.entities.animals.predators;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Eagle extends AnimalImpl {
    public Eagle(Position position) {
        super(position, SettingsService.getAnimalByName(Eagle.class.getSimpleName()).getAnimalProperties());
    }
}
