package com.mckeydonelly.animalpark.entities.animals.vegetarians;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Duck extends AnimalImpl {
    public Duck(Position position) {
        super(position, SettingsService.getAnimalByName(Duck.class.getSimpleName()).getAnimalProperties());
    }
}
