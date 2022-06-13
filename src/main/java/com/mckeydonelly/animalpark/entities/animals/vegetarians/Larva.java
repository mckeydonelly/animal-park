package com.mckeydonelly.animalpark.entities.animals.vegetarians;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Larva extends AnimalImpl {

    public Larva(Position position) {
        super(position, SettingsService.getAnimalByName(Larva.class.getSimpleName()).getAnimalProperties());
    }
}
