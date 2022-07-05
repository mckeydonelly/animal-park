package com.mckeydonelly.animalpark.settings;

import com.mckeydonelly.animalpark.settings.creature.Creature;
import com.mckeydonelly.animalpark.settings.creature.CreaturesSettings;
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
    void should_success_GetCreatureByName() {
        Creature creature = settingsService.getCreatureByName("Wolf");
        assertNotNull(creature);
    }

    @Test
    void should_success_GetCreaturesSettings() {
        CreaturesSettings creaturesSettings = settingsService.getCreaturesSettings();
        assertNotNull(creaturesSettings);
    }
}