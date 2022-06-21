package com.mckeydonelly.animalpark.settings;

import com.mckeydonelly.animalpark.settings.animal.Animal;
import com.mckeydonelly.animalpark.settings.animal.AnimalSettings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SettingsServiceTest {
    private static SettingsService settingsService;

    @BeforeAll
    static void setUp() {
        settingsService = new SettingsService();
    }

    @Test
    void should_create_new_SettingService() {
        SettingsService testSettingsService = new SettingsService();
        assertNotNull(testSettingsService);
    }

    @Test
    void should_success_GetDefaultSettings() {
        SimulationSettings settings = settingsService.getDefaultSettings();
        assertNotNull(settings);
    }

    @Test
    void should_success_GetAnimalByName() {
        Animal animal = settingsService.getAnimalByName("Wolf");
        assertNotNull(animal);
    }

    @Test
    void should_success_GetAnimalSettings() {
        AnimalSettings animalSettings = settingsService.getAnimalSettings();
        assertNotNull(animalSettings);
    }
}