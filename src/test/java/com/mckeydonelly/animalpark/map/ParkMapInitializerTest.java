package com.mckeydonelly.animalpark.map;

import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.settings.SimulationSettings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ParkMapInitializerTest {
    private static SimulationSettings settings;
    private static ParkMapInitializer parkMapInitializer;

    @BeforeAll
    static void setUp() {
        SettingsService settingsService = new SettingsService();
        settings = settingsService.getDefaultSettings();
        parkMapInitializer = new ParkMapInitializer(settingsService);
    }

    @Test
    void testMapProcessorCreate() {
        assertNotNull(parkMapInitializer.create(settings));
    }
}