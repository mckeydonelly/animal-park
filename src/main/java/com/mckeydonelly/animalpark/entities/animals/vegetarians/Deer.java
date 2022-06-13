package com.mckeydonelly.animalpark.entities.animals.vegetarians;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Deer extends AnimalImpl {
    public Deer(Position position) {
        super(position, SettingsService.getAnimalByName(Deer.class.getSimpleName()).getAnimalProperties());
    }
}
