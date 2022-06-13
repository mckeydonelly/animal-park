package com.mckeydonelly.animalpark.park;

import com.mckeydonelly.animalpark.activity.ActivityProcessor;
import com.mckeydonelly.animalpark.activity.LifeCycleProcessor;
import com.mckeydonelly.animalpark.entities.EntityFactory;
import com.mckeydonelly.animalpark.entities.EntityFactoryImpl;
import com.mckeydonelly.animalpark.map.MapProcessor;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.menu.IngameMenu;
import com.mckeydonelly.animalpark.menu.StartMenu;
import com.mckeydonelly.animalpark.settings.SimulationSettings;
import com.mckeydonelly.animalpark.utils.StatisticProcessor;

/**
 * Основной класс для запуска процессов симуляции.
 */
public class Park {
    private final MapProcessor mapProcessor;
    private final StartMenu startMenu;
    private final StatisticProcessor statisticProcessor;
    private final EntityFactory entityFactory;

    public Park() {
        this.mapProcessor = new MapProcessor();
        this.startMenu = new StartMenu();
        this.statisticProcessor = new StatisticProcessor(new IngameMenu());
        this.entityFactory = new EntityFactoryImpl();
    }

    public void start() {
        System.out.println("Welcome to animal park!");
        SimulationSettings settings = startMenu.start();

        ParkMap parkMap = mapProcessor.create(settings);
        LifeCycleProcessor lifeCycleProcessor = new LifeCycleProcessor(parkMap, entityFactory, settings);

        ActivityProcessor activityProcessor = new ActivityProcessor(parkMap,
                statisticProcessor,
                lifeCycleProcessor,
                settings);
        activityProcessor.start();
    }
}
