package com.mckeydonelly.animalpark.map;

import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.settings.SimulationSettings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MapProcessorTest {
    private static SettingsService settingsService;
    private static SimulationSettings settings;
    private static MapProcessor mapProcessor;

    @BeforeAll
    void setUp() {
        settingsService = new SettingsService();
        settings = settingsService.getDefaultSettings();
        mapProcessor = new MapProcessor(settingsService);
    }

    @Test
    void testMapProcessorCreate() {
        assertNotNull(mapProcessor.create(settings));
    }
}