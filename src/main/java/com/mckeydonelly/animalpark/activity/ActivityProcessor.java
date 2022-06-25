package com.mckeydonelly.animalpark.activity;

import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.menu.IngameMenuListener;
import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;
import com.mckeydonelly.animalpark.unit.UnitTypes;
import com.mckeydonelly.animalpark.utils.StatisticService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Contains multithreading logic.
 */
public class ActivityProcessor {
    private final ExecutorService executorService = Executors.newWorkStealingPool();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
    private final ParkMap parkMap;
    private final StatisticService statisticService;
    private final UnitActionsProcessor unitActionsProcessor;
    private final SimulationSettings settings;
    private final SettingsService settingsService;
    private final AtomicInteger turnsCount;
    private final int startTurnsCount;
    private volatile boolean stop;

    public ActivityProcessor(ParkMap parkMap,
                             StatisticService statisticService,
                             UnitActionsProcessor unitActionsProcessor,
                             SimulationSettings settings,
                             SettingsService settingsService) {
        this.parkMap = parkMap;
        this.statisticService = statisticService;
        this.unitActionsProcessor = unitActionsProcessor;
        this.settings = settings;
        this.settingsService = settingsService;
        this.turnsCount = new AtomicInteger(settings.get(SettingsType.TURNS_COUNT));
        this.startTurnsCount = settings.get(SettingsType.TURNS_COUNT);
    }

    /**
     * Run multithreading services and processes.
     */
    public void start() {
        Thread keyListener = new Thread(new IngameMenuListener(this, parkMap));
        keyListener.start();

        scheduledExecutorService.scheduleWithFixedDelay(() -> {
                    while (stop) {
                        Thread.onSpinWait();
                    }
                    statisticService.printStatistic(parkMap, startTurnsCount, startTurnsCount - turnsCount.get());
                },
                0,
                settings.get(SettingsType.STATISTIC_UPDATE_FREQUENCY),
                TimeUnit.MILLISECONDS);

        scheduledExecutorService.scheduleWithFixedDelay(
                () -> growPlants(parkMap),
                100,
                settings.get(SettingsType.GROW_PLANTS_FREQUENCY),
                TimeUnit.MILLISECONDS);

        scheduledExecutorService.schedule(() -> lifeCycle(parkMap),
                100,
                TimeUnit.MILLISECONDS);
    }

    public void pause() {
        stop = true;
    }

    public void unPause() {
        stop = false;
    }

    /**
     * Performing a life cycle for units.
     *
     * @param parkMap park map
     */
    private void lifeCycle(ParkMap parkMap) {
        stop = false;

        do {
            List<Runnable> entitiesTaskList = new ArrayList<>();
            parkMap.getAllLocations()
                    .forEach(location -> {
                        location.lockLocation();
                        try {
                            location.getEntitiesOnLocationList().stream()
                                    .filter(unit -> !UnitTypes.PLANT.equals(settingsService.getUnitByName(unit.getName()).getType()))
                                    .forEach(unit -> entitiesTaskList.add(() -> unitActionsProcessor.doTurn(unit)));
                        } finally {
                            location.unlockLocation();
                        }
                    });

            Collections.shuffle(entitiesTaskList);

            List<Future<?>> finalFuturesEntitiesTaskList = new ArrayList<>();
            entitiesTaskList.forEach(runnable -> finalFuturesEntitiesTaskList.add(executorService.submit(runnable)));

            while (true) {
                boolean isFinished = finalFuturesEntitiesTaskList.stream().allMatch(Future::isDone);
                if (isFinished)
                    break;
            }

            resetReproduction();

            while (stop) {
                Thread.onSpinWait();
            }

        } while (turnsCount.decrementAndGet() > 0);
        shutdownActivity();
        System.out.println("\nSimulation is ended.");
    }

    /**
     * Reset reproduction counter after each cycle
     */
    private void resetReproduction() {
        parkMap.getAllLocations()
                .forEach(location -> {
                    location.lockLocation();
                    try {
                        location.getEntitiesOnLocationList().forEach(unit -> unit.setReadyToReproduction(true));
                    } finally {
                        location.unlockLocation();
                    }
                });
    }

    /**
     * Growing new plants
     *
     * @param parkMap park map
     */
    private void growPlants(ParkMap parkMap) {
        while (stop) {
            Thread.onSpinWait();
        }

        parkMap.getAllLocations()
                .forEach(location -> {
                    location.lockLocation();
                    try {
                        location.growPlants();
                    } finally {
                        location.unlockLocation();
                    }
                });
    }

    /**
     * Stops all executors after done all cycles
     */
    private void shutdownActivity() {
        statisticService.printStatistic(parkMap, startTurnsCount, startTurnsCount - turnsCount.get());
        executorService.shutdownNow();
        scheduledExecutorService.shutdownNow();
    }
}
