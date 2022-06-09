package com.mckeydonelly.animalpark.activity;

import com.mckeydonelly.animalpark.entities.Entity;
import com.mckeydonelly.animalpark.entities.EntityFactory;
import com.mckeydonelly.animalpark.entities.PlantsFactory;
import com.mckeydonelly.animalpark.entities.animals.Animal;
import com.mckeydonelly.animalpark.map.Location;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.menu.IngameMenuListener;
import com.mckeydonelly.animalpark.utils.StatisticProcessor;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Запускает процесс игры.
 */
public class ActivityProcessor {
    private final ExecutorService executorService = Executors.newWorkStealingPool();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
    private final AtomicInteger turnsCount;
    private final int startTurnsCount;
    private final StatisticProcessor statisticProcessor;
    private final ParkMap parkMap;
    private volatile boolean stop;

    public ActivityProcessor(ParkMap parkMap, Integer turnsCount, StatisticProcessor statisticProcessor) {
        this.parkMap = parkMap;
        this.turnsCount = new AtomicInteger(turnsCount);
        this.startTurnsCount = turnsCount;
        this.statisticProcessor = statisticProcessor;
    }

    /**
     * Запускает многопоточный сервис и сопутствующие задачи.
     */
    public void start() {
        Thread keyListener = new Thread(new IngameMenuListener(this, parkMap));
        keyListener.start();

        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            while (stop) {
                Thread.onSpinWait();
            }
            statisticProcessor.printStatistic(parkMap, startTurnsCount, startTurnsCount - turnsCount.get());
        }, 0, SimulationSettings.get(SettingsType.STATISTIC_UPDATE_FREQUENCY), TimeUnit.MILLISECONDS);

        scheduledExecutorService.scheduleWithFixedDelay(
                () -> growPlants(parkMap), 100, SimulationSettings.get(SettingsType.GROW_PLANTS_FREQUENCY), TimeUnit.MILLISECONDS);
        scheduledExecutorService.schedule(() -> lifeCycle(parkMap), 100, TimeUnit.MILLISECONDS);
    }

    public void pause() {
        stop = true;
    }

    public void unPause() {
        stop = false;
    }

    /**
     * Выполняет жизненный цикл (ход) для всех живых существ.
     * @param parkMap карта парка
     */
    private void lifeCycle(ParkMap parkMap) {
        stop = false;

        do {
            List<Runnable> entitiesTaskList = new ArrayList<>();
            for (List<Location> locations : parkMap.getMap()) {
                for (Location location : locations) {
                    location.lockLocation();
                    try {
                        location.getEntitiesOnLocationList().stream()
                                .filter(Animal.class::isInstance)
                                .forEach(entity -> entitiesTaskList.add(() -> entity.doTurn(parkMap)));
                    } finally {
                        location.unlockLocation();
                    }
                }
            }

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
     * Сбрасывает счетчик готовности к размножению в конце каждого цикла.
     */
    private void resetReproduction() {
        for (List<Location> locations : parkMap.getMap()) {
            for (Location location : locations) {
                location.lockLocation();
                try {
                    location.getEntitiesOnLocationList().forEach(Entity::resetReproduction);
                } finally {
                    location.unlockLocation();
                }
            }
        }
    }

    /**
     * Выращивает новые растения
     * @param parkMap карта парка
     */
    private void growPlants(ParkMap parkMap) {
        while (stop) {
            Thread.onSpinWait();
        }

        EntityFactory entityFactory = new PlantsFactory();
        parkMap.getMap().stream()
                .flatMap(List::stream)
                .toList()
                .forEach(location -> {
                    location.lockLocation();
                    try {
                        location.fill(entityFactory);
                    } finally {
                        location.unlockLocation();
                    }
                });
    }

    private void shutdownActivity() {
        executorService.shutdownNow();
        scheduledExecutorService.shutdownNow();
    }
}
