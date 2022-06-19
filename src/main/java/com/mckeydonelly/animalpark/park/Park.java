package com.mckeydonelly.animalpark.park;

import com.mckeydonelly.animalpark.activity.ActivityProcessor;
import com.mckeydonelly.animalpark.activity.LifeCycleProcessor;
import com.mckeydonelly.animalpark.entities.EatingProcessor;
import com.mckeydonelly.animalpark.entities.UnitFactory;
import com.mckeydonelly.animalpark.entities.UnitFactoryImpl;
import com.mckeydonelly.animalpark.map.MapProcessor;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.menu.IngameMenu;
import com.mckeydonelly.animalpark.menu.StartMenu;
import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.settings.SimulationSettings;
import com.mckeydonelly.animalpark.utils.StatisticProcessor;

/**
 * Основной класс для запуска процессов симуляции.
 */
public class Park {
    private final SettingsService settingsService;
    private final UnitFactory unitFactory;
    private final MapProcessor mapProcessor;
    private final EatingProcessor eatingProcessor;
    private final StartMenu startMenu;
    private final StatisticProcessor statisticProcessor;

    public Park() {
        this.settingsService = new SettingsService();
        this.unitFactory = new UnitFactoryImpl();
        this.mapProcessor = new MapProcessor(settingsService);
        this.eatingProcessor = new EatingProcessor(settingsService);
        this.startMenu = new StartMenu(settingsService);
        this.statisticProcessor = new StatisticProcessor(new IngameMenu(), settingsService);
    }

    public void start() {
        System.out.println("Welcome to animal park simulation!");
        SimulationSettings settings = startMenu.start();

        ParkMap parkMap = mapProcessor.create(settings);
        LifeCycleProcessor lifeCycleProcessor = new LifeCycleProcessor(parkMap,
                unitFactory,
                settings,
                eatingProcessor,
                settingsService);

        ActivityProcessor activityProcessor = new ActivityProcessor(parkMap,
                statisticProcessor,
                lifeCycleProcessor,
                settingsService,
                settings);
        activityProcessor.start();
    }
}
