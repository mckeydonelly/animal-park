package com.mckeydonelly.animalpark.entities.animals.vegetarians;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.settings.animal.AnimalProperties;

public class Boar extends AnimalImpl {
    public Boar(Position position, AnimalProperties animalProperties) {
        super(position, animalProperties);
    }
}
