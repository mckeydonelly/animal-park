package com.mckeydonelly.animalpark.settings;

import java.util.HashMap;
import java.util.Map;

/**
 * Хранение настроек для симуляции парка животных
 */
public class SimulationSettings {

    private final Map<String, Integer> config = new HashMap<>();

    /**
     * Добавляет настройку в конфигурацию
     * @param type - тип настройки из {@link com.mckeydonelly.animalpark.settings.SettingsType}
     * @param value - значение настройки
     */
    public void add(SettingsType type, Integer value) {
        config.put(type.getTypeCode(), value);
    }

    /**
     * Получает настройку из конфигурации
     * @param type - тип настройки из {@link com.mckeydonelly.animalpark.settings.SettingsType}
     * @return значение настройки
     */
    public Integer get(SettingsType type) {
        return config.get(type.getTypeCode());
    }

}
