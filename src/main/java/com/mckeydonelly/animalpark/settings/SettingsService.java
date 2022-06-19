package com.mckeydonelly.animalpark.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.mckeydonelly.animalpark.settings.animal.Animal;
import com.mckeydonelly.animalpark.settings.animal.AnimalSettings;
import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Сервис обработки настроек приложения и сущностей.
 */
public class SettingsService {
    private static final String APP_SETTINGS_PATH = "app.properties";
    private static final String ANIMAL_SETTINGS_PATH = "animal-settings.yaml";
    private final Properties appSettings;
    private final AnimalSettings animalSettings;

    public SettingsService() {
        this.animalSettings = initAnimalSettings();
        this.appSettings = new Properties();
    }

    /**
     * Инициализация настроек приложения из properties файла в случае запуска с дефолтными настройками.
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
     * Инициализация настроек животных из YAML файла.
     *
     * @return настройки животных.
     */
    private AnimalSettings initAnimalSettings() {
        ObjectMapper mapper = new YAMLMapper();
        AnimalSettings tmpAnimalSettings = null;
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(ANIMAL_SETTINGS_PATH)) {
            tmpAnimalSettings = mapper.readValue(inputStream, AnimalSettings.class);
        } catch (IOException e) {
            System.out.println(colorize("Can't find or read properties file for animals by path: " + ANIMAL_SETTINGS_PATH, RED_TEXT(), NONE()));
            e.printStackTrace();
            System.exit(1);
        }
        return tmpAnimalSettings;
    }

    /**
     * Получение настроек животного по имени.
     *
     * @param name - имя животного.
     * @return настройки животного.
     */
    public Animal getAnimalByName(String name) {
        return animalSettings.getAnimals().get(name);
    }

    /**
     * Получение настроек животных
     *
     * @return настройки животных.
     */
    public AnimalSettings getAnimalSettings() {
        return animalSettings;
    }
}
