package com.mckeydonelly.animalpark.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.mckeydonelly.animalpark.settings.creature.Creature;
import com.mckeydonelly.animalpark.settings.creature.CreaturesSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.NONE;
import static com.diogonunes.jcolor.Attribute.RED_TEXT;

/**
 * Service for processing application settings and creatures configuration.
 */
public class SettingsService {
    private static final String APP_SETTINGS_PATH = "app.properties";
    private static final String CREATURES_SETTINGS_PATH = "creatures-settings.yaml";
    private final Properties appSettings;
    private final CreaturesSettings creaturesSettings;

    public SettingsService() {
        this.creaturesSettings = initCreaturesSettings();
        this.appSettings = new Properties();
    }

    /**
     * Initialization of application settings from the properties file
     */
    public SimulationSettings getDefaultSettings() {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(APP_SETTINGS_PATH)) {
            appSettings.load(inputStream);
        } catch (IOException e) {
            System.out.println(colorize("Can't find properties file by path: " + APP_SETTINGS_PATH, RED_TEXT(), NONE()));
            System.exit(1);
        }

        SimulationSettings settings = new SimulationSettings();

        for (SettingsType settingsType : SettingsType.values()) {
            String settingsProperty = appSettings.getProperty(settingsType.getTypeCode());
            if (settingsProperty == null) {
                throw new IllegalArgumentException(colorize("Can't find property: "
                        + settingsType.getTypeCode()
                        + " in properties file: "
                        + APP_SETTINGS_PATH
                        + ". Please check your properties file.", RED_TEXT(), NONE()));
            }

            int settingsValue = 0;
            try {
                settingsValue = Integer.parseInt(settingsProperty);
            } catch (NumberFormatException e) {
                throw new NumberFormatException(colorize("Property: "
                        + settingsType.getTypeCode()
                        + " is not an integer"
                        + " in properties file: "
                        + APP_SETTINGS_PATH
                        + ". Please check your properties file.", RED_TEXT(), NONE()));
            }

            settings.add(settingsType, settingsValue);
        }

        return settings;
    }

    /**
     * Initializing creature settings from a YAML file.
     *
     * @return CreaturesSettings
     */
    private CreaturesSettings initCreaturesSettings() {
        ObjectMapper mapper = new YAMLMapper();
        CreaturesSettings tmpCreaturesSettings = null;
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CREATURES_SETTINGS_PATH)) {
            tmpCreaturesSettings = mapper.readValue(inputStream, CreaturesSettings.class);
        } catch (IOException e) {
            System.out.println(colorize("Can't find or read properties file for creatures by path: " + CREATURES_SETTINGS_PATH, RED_TEXT(), NONE()));
            e.printStackTrace();
            System.exit(1);
        }
        return tmpCreaturesSettings;
    }

    /**
     * Get creature setting by name
     *
     * @param name creature name
     * @return configuration of this creature
     */
    public Creature getCreatureByName(String name) {
        return creaturesSettings.getCreatures().get(name);
    }

    /**
     * Get configuration for all creatures
     *
     * @return creatures settings
     */
    public CreaturesSettings getCreaturesSettings() {
        return creaturesSettings;
    }
}
