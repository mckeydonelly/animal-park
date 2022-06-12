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
    private static final Properties appSettings = new Properties();
    private static final AnimalSettings animalSettings = initAnimalSettings();

    private SettingsService() {

    }

    /**
     * Инициализация настроек приложения из properties файла в случае запуска с дефолтными настройками.
     */
    public static void setDefaultSettings() {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(APP_SETTINGS_PATH)) {
            appSettings.load(inputStream);
        } catch (IOException e) {
            System.out.println("Can't find properties file by path: " + APP_SETTINGS_PATH);
            System.exit(1);
        }

        SimulationSettings.add(SettingsType.MAP_ROWS, Integer.valueOf(appSettings.getProperty(SettingsType.MAP_ROWS.getTypeCode())));
        SimulationSettings.add(SettingsType.MAP_COLUMNS, Integer.valueOf(appSettings.getProperty(SettingsType.MAP_COLUMNS.getTypeCode())));
        SimulationSettings.add(SettingsType.TURNS_COUNT, Integer.valueOf(appSettings.getProperty(SettingsType.TURNS_COUNT.getTypeCode())));
        SimulationSettings.add(SettingsType.TURNS_FOR_DIE_BY_MAX_FILL, Integer.valueOf(appSettings.getProperty(SettingsType.TURNS_FOR_DIE_BY_MAX_FILL.getTypeCode())));
        SimulationSettings.add(SettingsType.STATISTIC_UPDATE_FREQUENCY, Integer.valueOf(appSettings.getProperty(SettingsType.STATISTIC_UPDATE_FREQUENCY.getTypeCode())));
        SimulationSettings.add(SettingsType.GROW_PLANTS_FREQUENCY, Integer.valueOf(appSettings.getProperty(SettingsType.GROW_PLANTS_FREQUENCY.getTypeCode())));
    }

    /**
     * Инициализация настроек животных из YAML файла.
     * @return настройки животных.
     */
    private static AnimalSettings initAnimalSettings() {
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
    public static Animal getAnimalByName(String name) {
        return animalSettings.getAnimals().get(name);
    }

    /**
     * Получение настроек животных
     * @return настройки животных.
     */
    public static AnimalSettings getAnimalSettings() {
        return animalSettings;
    }
}
