package com.mckeydonelly.animalpark.entities.animals.predators;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.settings.animal.AnimalProperties;

public class Wolf extends AnimalImpl {
    public Wolf(Position position, AnimalProperties animalProperties) {
        super(position, animalProperties);
    }
}
