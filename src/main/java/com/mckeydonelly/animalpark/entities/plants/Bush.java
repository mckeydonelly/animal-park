package com.mckeydonelly.animalpark.entities.plants;

import com.mckeydonelly.animalpark.settings.SettingsService;

public class Bush extends PlantsImpl {

    public Bush() {
        super(SettingsService.getAnimalByName(Bush.class.getSimpleName()).getAnimalProperties());
    }
}
