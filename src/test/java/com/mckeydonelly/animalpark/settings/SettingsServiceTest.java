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
    void testInitSettingService() {
        SettingsService testSettingsService = new SettingsService();
        assertNotNull(testSettingsService);
    }

    @Test
    void testGetDefaultSettings() {
        SimulationSettings settings = settingsService.getDefaultSettings();
        assertNotNull(settings);
    }

    @Test
    void testGetAnimalByName() {
        Animal animal = settingsService.getAnimalByName("wolf");
        assertNotNull(animal);
    }

    @Test
    void testGetAnimalSettings() {
        AnimalSettings animalSettings = settingsService.getAnimalSettings();
        assertNotNull(animalSettings);
    }
}