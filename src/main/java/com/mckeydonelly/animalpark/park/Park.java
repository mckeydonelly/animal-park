package com.mckeydonelly.animalpark.park;

import com.mckeydonelly.animalpark.activity.ActivityProcessor;
import com.mckeydonelly.animalpark.map.MapProcessor;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.menu.IngameMenu;
import com.mckeydonelly.animalpark.menu.StartMenu;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;
import com.mckeydonelly.animalpark.utils.StatisticProcessor;

/**
 * Основной класс для запуска процессов симуляции.
 */
public class Park {
    private final MapProcessor mapProcessor;
    private final StartMenu startMenu;
    private final StatisticProcessor statisticProcessor;
    private ActivityProcessor activityProcessor;

    public Park() {
        this.mapProcessor = new MapProcessor();
        this.startMenu = new StartMenu();
        this.statisticProcessor = new StatisticProcessor(new IngameMenu());
    }

    public void start() {
        System.out.println("Welcome to animal park!");
        startMenu.start();

        ParkMap parkMap = mapProcessor.create();
        activityProcessor = new ActivityProcessor(parkMap, SimulationSettings.get(SettingsType.TURNS_COUNT), statisticProcessor);

        activityProcessor.start();
    }
}
