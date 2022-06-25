package com.mckeydonelly.animalpark.settings;

import com.mckeydonelly.animalpark.settings.unit.Unit;
import com.mckeydonelly.animalpark.settings.unit.UnitSettings;
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
        Unit unit = settingsService.getUnitByName("Wolf");
        assertNotNull(unit);
    }

    @Test
    void should_success_GetAnimalSettings() {
        UnitSettings unitSettings = settingsService.getAnimalSettings();
        assertNotNull(unitSettings);
    }
}