package com.mckeydonelly.animalpark.entities.animals.vegetarians;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Sheep extends AnimalImpl {
    public Sheep(Position position) {
        super(position, SettingsService.getAnimalByName(Sheep.class.getSimpleName()).getAnimalProperties());
    }
}
