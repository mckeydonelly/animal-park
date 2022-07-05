package com.mckeydonelly.animalpark.map;

import com.mckeydonelly.animalpark.creature.Creature;
import com.mckeydonelly.animalpark.settings.SettingsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Location on map
 */
public class Location {
    private final List<Creature> entitiesOnLocationList = new ArrayList<>();
    private final Map<String, Integer> uniqueEntitiesCount = new ConcurrentHashMap<>();
    private final Position position;
    private final SettingsService settingsService;
    private final Lock lock = new ReentrantLock(true);

    public Location(Position position, SettingsService settingsService) {
        this.position = position;
        this.settingsService = settingsService;
    }

    public List<Creature> getEntitiesOnLocationList() {
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
        List<String> entityTypes = new ArrayList<>(settingsService.getCreaturesSettings().getCreatures().keySet());
        int entityTypesCount = settingsService.getCreaturesSettings().getCreatures().size();

        for (int index = 0; index <= entityTypesCount; index++) {
            String entityType = entityTypes.get(ThreadLocalRandom.current().nextInt(entityTypesCount));
            Creature creature = new Creature(position, entityType, settingsService.getCreatureByName(entityType).getCreatureProperties());
            add(creature);
        }
    }

    public void lockLocation() {
        lock.lock();
    }

    public void unlockLocation() {
        lock.unlock();
    }

    /**
     * Add creature in location
     *
     * @param creature creature
     */
    public void add(Creature creature) {
        if (changeUniqueEntities(creature, false)) {
            entitiesOnLocationList.add(creature);
        }
    }

    /**
     * Remove creature from location
     *
     * @param creature creature
     */
    public void remove(Creature creature) {
        changeUniqueEntities(creature, true);
        entitiesOnLocationList.remove(creature);
    }

    /**
     * Change counter of uniques creatures on location
     *
     * @param creature creature
     * @param leave    flag of the exit entity from the location or death
     * @return boolean
     * <p>
     * Return false if location have maximum of this creature type on location
     */
    public boolean changeUniqueEntities(Creature creature, boolean leave) {
        String entityName = creature.getName();

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
     * Check limit unique creature on location
     */
    private boolean checkMaxCountEntities(String entityName, int countUniqueEntityOnLocation) {
        return countUniqueEntityOnLocation <= settingsService.getCreatureByName(entityName).getMaxByLocation() - 1;
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
            settingsService.getCreaturesSettings()
                    .getCreatures().forEach((key, value) -> result
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
