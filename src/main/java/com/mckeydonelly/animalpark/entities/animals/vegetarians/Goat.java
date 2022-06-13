package com.mckeydonelly.animalpark.entities.animals.vegetarians;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Goat extends AnimalImpl {
    public Goat(Position position) {
        super(position, SettingsService.getAnimalByName(Goat.class.getSimpleName()).getAnimalProperties());
    }
}
