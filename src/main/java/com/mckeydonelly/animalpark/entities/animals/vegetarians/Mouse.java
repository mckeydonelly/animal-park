package com.mckeydonelly.animalpark.entities.animals.vegetarians;

import com.mckeydonelly.animalpark.entities.animals.AnimalImpl;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;

public class Mouse extends AnimalImpl {
    public Mouse(Position position) {
        super(position, SettingsService.getAnimalByName(Mouse.class.getSimpleName()).getAnimalProperties());
    }
}
