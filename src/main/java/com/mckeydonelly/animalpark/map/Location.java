package com.mckeydonelly.animalpark.map;

import com.mckeydonelly.animalpark.unit.Unit;
import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.unit.UnitTypes;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Location on map
 */
public class Location {
    private final List<Unit> entitiesOnLocationList = new ArrayList<>();
    private final Map<String, Integer> uniqueEntitiesCount = new ConcurrentHashMap<>();
    private final Position position;
    private final SettingsService settingsService;
    private final Lock lock = new ReentrantLock(true);

    public Location(Position position, SettingsService settingsService) {
        this.position = position;
        this.settingsService = settingsService;
    }

    public List<Unit> getEntitiesOnLocationList() {
        return entitiesOnLocationList;
    }

    public Position getPosition() {
        return position;
    }

    public Map<String, Integer> getUniqueEntitiesCount() {
        return uniqueEntitiesCount;
    }

    /**
     * Filling location for the starting simulation.
     */
    public void fill() {
        List<String> entityTypes = new ArrayList<>(settingsService.getAnimalSettings().getUnits().keySet());
        int entityTypesCount = settingsService.getAnimalSettings().getUnits().size();

        for (int index = 0; index <= entityTypesCount; index++) {
            String entityType = entityTypes.get(ThreadLocalRandom.current().nextInt(entityTypesCount));
            Unit unit = new Unit(position, entityType, settingsService.getUnitByName(entityType).getUnitProperties());
            add(unit);
        }
    }

    /**
     * Growing new plants on location during the simulation
     */
    public void growPlants() {
        List<String> plantTypes = settingsService.getAnimalSettings().getUnits().entrySet().stream()
                .filter(unit -> UnitTypes.PLANT.equals(unit.getValue().getType()))
                .map(Map.Entry::getKey)
                .toList();
        int entityTypesCount = settingsService.getAnimalSettings().getUnits().size();

        for (int index = 0; index <= entityTypesCount; index++) {
            String entityType = plantTypes.get(ThreadLocalRandom.current().nextInt(plantTypes.size()));
            Unit unit = new Unit(position, entityType, settingsService.getUnitByName(entityType).getUnitProperties());
            add(unit);
        }
    }

    public void lockLocation() {
        lock.lock();
    }

    public void unlockLocation() {
        lock.unlock();
    }

    /**
     * Add unit in location
     *
     * @param unit unit
     */
    public void add(Unit unit) {
        if (changeUniqueEntities(unit, false)) {
            entitiesOnLocationList.add(unit);
        }
    }

    /**
     * Remove unit from location
     *
     * @param unit unit
     */
    public void remove(Unit unit) {
        changeUniqueEntities(unit, true);
        entitiesOnLocationList.remove(unit);
    }

    /**
     * Change counter of uniques units on location
     *
     * @param unit unit
     * @param leave flag of the exit entity from the location or death
     * @return boolean
     *
     * Return false if location have maximum of this unit type on location
     */
    public boolean changeUniqueEntities(Unit unit, boolean leave) {
        String entityName = unit.getName();

        int countUniqueEntityOnLocation = uniqueEntitiesCount.getOrDefault(entityName, 0);

        if (!checkMaxCountEntities(entityName, countUniqueEntityOnLocation)) return false;

        if (leave) {
            if (countUniqueEntityOnLocation - 1 > 0) {
                uniqueEntitiesCount.put(entityName, --countUniqueEntityOnLocation);
            } else {
                uniqueEntitiesCount.remove(entityName);
            }
        } else {
            uniqueEntitiesCount.put(entityName, countUniqueEntityOnLocation + 1);
        }
        return true;
    }

    /**
     * Check limit unique unit on location
     */
    private boolean checkMaxCountEntities(String entityName, int countUniqueEntityOnLocation) {
        return countUniqueEntityOnLocation <= settingsService.getUnitByName(entityName).getMaxByLocation() - 1;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("[")
                .append(position.row())
                .append("][")
                .append(position.column())
                .append("] --> ");
        result.append("[");

        if (uniqueEntitiesCount.size() > 0) {
            settingsService.getAnimalSettings()
                    .getUnits().forEach((key, value) -> result
                            .append(value.getEmoji())
                            .append("=")
                            .append(uniqueEntitiesCount.getOrDefault(key, 0))
                            .append(","));
            result.deleteCharAt(result.lastIndexOf(","));
        }

        result.append("]\n");
        return result.toString();
    }
}
