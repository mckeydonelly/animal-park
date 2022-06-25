package com.mckeydonelly.animalpark.park;

import com.mckeydonelly.animalpark.activity.ActivityProcessor;
import com.mckeydonelly.animalpark.activity.UnitActionsProcessor;
import com.mckeydonelly.animalpark.activity.UnitActions;
import com.mckeydonelly.animalpark.unit.EatingProcessor;
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
    private final ParkMapInitializer parkMapInitializer;
    private final EatingProcessor eatingProcessor;
    private final StartMenu startMenu;
    private final StatisticService statisticService;

    public ParkService() {
        this.settingsService = new SettingsService();
        this.parkMapInitializer = new ParkMapInitializer(settingsService);
        this.eatingProcessor = new EatingProcessor(settingsService);
        this.startMenu = new StartMenu(settingsService);
        this.statisticService = new StatisticService(new IngameMenu(), settingsService);
    }

    public void start() {
        System.out.println("Welcome to animal park simulation!");
        SimulationSettings settings = startMenu.start();

        ParkMap parkMap = parkMapInitializer.create(settings);

        UnitActions unitActions = new UnitActions(parkMap,
                settings,
                eatingProcessor,
                settingsService);

        UnitActionsProcessor unitActionsProcessor = new UnitActionsProcessor(parkMap,
                settings,
                unitActions);

        ActivityProcessor activityProcessor = new ActivityProcessor(parkMap,
                statisticService,
                unitActionsProcessor,
                settings, settingsService);
        activityProcessor.start();
    }
}
