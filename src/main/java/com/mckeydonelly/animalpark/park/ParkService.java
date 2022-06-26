package com.mckeydonelly.animalpark.park;

import com.mckeydonelly.animalpark.activity.ActivityProcessor;
import com.mckeydonelly.animalpark.activity.CreatureActionProcessor;
import com.mckeydonelly.animalpark.activity.CreatureActions;
import com.mckeydonelly.animalpark.creature.EatingProcessor;
import com.mckeydonelly.animalpark.map.LocationProcessor;
import com.mckeydonelly.animalpark.map.ParkMapInitializer;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.menu.IngameMenu;
import com.mckeydonelly.animalpark.menu.StartMenu;
import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.settings.SimulationSettings;
import com.mckeydonelly.animalpark.utils.StatisticService;

/**
 * The main class for start application processes
 */
public class ParkService {
    private final SettingsService settingsService;
    private final EatingProcessor eatingProcessor;
    private final StartMenu startMenu;
    private final StatisticService statisticService;

    public ParkService() {
        this.settingsService = new SettingsService();
        this.eatingProcessor = new EatingProcessor(settingsService);
        this.startMenu = new StartMenu(settingsService);
        this.statisticService = new StatisticService(new IngameMenu(), settingsService);
    }

    public void start() {
        System.out.println("Welcome to animal park simulation!");
        SimulationSettings settings = startMenu.start();

        LocationProcessor locationProcessor = new LocationProcessor(settingsService, settings);

        ParkMapInitializer parkMapInitializer = new ParkMapInitializer(settingsService, locationProcessor);

        ParkMap parkMap = parkMapInitializer.create(settings);

        CreatureActions creatureActions = new CreatureActions(parkMap,
                settings,
                eatingProcessor,
                settingsService);

        CreatureActionProcessor creatureActionProcessor = new CreatureActionProcessor(parkMap,
                settings,
                creatureActions);


        ActivityProcessor activityProcessor = new ActivityProcessor(parkMap,
                statisticService,
                creatureActionProcessor,
                settings,
                locationProcessor);
        activityProcessor.start();
    }
}
