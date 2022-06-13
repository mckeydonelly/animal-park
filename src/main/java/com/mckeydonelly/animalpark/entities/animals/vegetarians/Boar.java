package com.mckeydonelly.animalpark.entities.animals.vegetarians;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Boar extends AnimalImpl {
    public Boar(Position position) {
        super(position, SettingsService.getAnimalByName(Boar.class.getSimpleName()).getAnimalProperties());
    }
}
