package com.mckeydonelly.animalpark.settings;

import com.mckeydonelly.animalpark.settings.animal.AnimalSettings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class SimulationSettingsTest {
    private static final String APP_SETTINGS_PATH = "app.properties";
    private static Properties appSettings;

    @BeforeAll
    static void setUp() {
        appSettings = new Properties();
    }

    @Test
    void should_add_and_get_param_settings() {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(APP_SETTINGS_PATH)) {
            appSettings.load(inputStream);
        } catch (IOException e) {
            System.out.println("Can't find properties file by path: " + APP_SETTINGS_PATH);
        }

        SimulationSettings settings = new SettingsService().getDefaultSettings();
        for (SettingsType settingsType : SettingsType.values()) {
            assertEquals(Integer.valueOf(appSettings.getProperty(settingsType.getTypeCode())), settings.get(settingsType));
        }
    }
}