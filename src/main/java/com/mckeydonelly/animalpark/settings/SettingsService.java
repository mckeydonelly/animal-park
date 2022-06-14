package com.mckeydonelly.animalpark.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.mckeydonelly.animalpark.settings.animal.Animal;
import com.mckeydonelly.animalpark.settings.animal.AnimalSettings;

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
            System.out.println("Can't find properties file by path: " + APP_SETTINGS_PATH);
            System.exit(1);
        }

        SimulationSettings settings = new SimulationSettings();

        for (SettingsType settingsType : SettingsType.values()) {
            settings.add(settingsType, Integer.valueOf(appSettings.getProperty(settingsType.getTypeCode())));
        }

        return settings;
    }

    /**
     * Инициализация настроек животных из YAML файла.
     * @return настройки животных.
     */
    private AnimalSettings initAnimalSettings() {
        ObjectMapper mapper = new YAMLMapper();
        AnimalSettings tmpAnimalSettings = null;
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(ANIMAL_SETTINGS_PATH)) {
            tmpAnimalSettings = mapper.readValue(inputStream, AnimalSettings.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpAnimalSettings;
    }

    /**
     * Получение настроек животного по имени.
     * @param name - имя животного.
     * @return настройки животного.
     */
    public Animal getAnimalByName(String name) {
        return animalSettings.getAnimals().get(name);
    }

    /**
     * Получение настроек животных
     * @return настройки животных.
     */
    public AnimalSettings getAnimalSettings() {
        return animalSettings;
    }
}
