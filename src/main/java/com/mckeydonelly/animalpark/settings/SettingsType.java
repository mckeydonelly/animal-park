package com.mckeydonelly.animalpark.settings;

/**
 * Хранит типы настроек приложения
 */

public enum SettingsType {
    MAP_ROWS("app.map.rows", "count map rows (number)"),
    MAP_COLUMNS("app.map.columns", "count map columns (number)"),
    TURNS_COUNT("app.turnsCount", "turns count (number)"),
    TURNS_FOR_DIE_BY_MAX_FILL("app.turnForDieByMaxFill", "turns for die (units loses every turn of \"maximum food / turns for die\" (number)"),
    STATISTIC_UPDATE_FREQUENCY("app.statisticUpdateFrequency", "statistics update frequency (ms)"),
    GROW_PLANTS_FREQUENCY("app.growPlantsFrequency", "the frequency of the task of growing new plants (ms)");

    private final String typeCode;
    private final String description;

    SettingsType(String typeCode, String description) {
        this.typeCode = typeCode;
        this.description = description;
    }

    public String getTypeCode() {
        return typeCode;
    }
    public String getDescription() {
        return description;
    }
}
