package com.mckeydonelly.animalpark.activity;

import com.mckeydonelly.animalpark.map.LocationProcessor;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.menu.IngameMenuListener;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;
import com.mckeydonelly.animalpark.utils.StatisticService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Contains multithreading logic.
 */
public class ActivityProcessor {
    private final ExecutorService executorService = Executors.newWorkStealingPool();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
    private final ParkMap parkMap;
    private final StatisticService statisticService;
    private final CreatureActionProcessor creatureActionProcessor;
    private final SimulationSettings settings;
    private final LocationProcessor locationProcessor;
    private final AtomicInteger turnsCount;
    private final int startTurnsCount;
    private volatile boolean stop;

    public ActivityProcessor(ParkMap parkMap,
                             StatisticService statisticService,
                             CreatureActionProcessor creatureActionProcessor,
                             SimulationSettings settings,
                             LocationProcessor locationProcessor) {
        this.parkMap = parkMap;
        this.statisticService = statisticService;
        this.creatureActionProcessor = creatureActionProcessor;
        this.settings = settings;
        this.locationProcessor = locationProcessor;
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
                this::growPlantsActivity,
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
     * Performing a life cycle for creatures.
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
                            location.getEntitiesOnLocationList()
                                    .forEach(creature -> entitiesTaskList.add(() -> creatureActionProcessor.doTurn(creature)));
                        } finally {
                            location.unlockLocation();
                        }
                    });

            Collections.shuffle(entitiesTaskList);

            List<Future<?>> finalFuturesEntitiesTaskList = new ArrayList<>();
            entitiesTaskList.forEach(runnable -> finalFuturesEntitiesTaskList.add(executorService.submit(runnable)));

            while (true) {
                boolean isFinished = finalFuturesEntitiesTaskList.stream()
                        .allMatch(Future::isDone);
                if (isFinished)
                    break;
            }

            resetReproductionActivity();

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
    private void resetReproductionActivity() {
        parkMap.getAllLocations()
                .forEach(location -> {
                    location.lockLocation();
                    try {
                        location.getEntitiesOnLocationList().forEach(creature -> creature.setReadyToReproduction(true));
                    } finally {
                        location.unlockLocation();
                    }
                });
    }

    /**
     * Growing new plants
     */
    private void growPlantsActivity() {
        while (stop) {
            Thread.onSpinWait();
        }

        locationProcessor.growPlants(parkMap);
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
