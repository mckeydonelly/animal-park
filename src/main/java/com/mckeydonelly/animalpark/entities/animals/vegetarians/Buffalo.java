package com.mckeydonelly.animalpark.entities.animals.vegetarians;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Buffalo extends AnimalImpl {
    public Buffalo(Position position) {
        super(position, SettingsService.getAnimalByName(Buffalo.class.getSimpleName()).getAnimalProperties());
    }
}
