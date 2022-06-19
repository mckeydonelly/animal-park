package com.mckeydonelly.animalpark.map;

import com.mckeydonelly.animalpark.entities.Unit;
import com.mckeydonelly.animalpark.entities.UnitFactory;
import com.mckeydonelly.animalpark.settings.SettingsService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Локация на карте.
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
     * Заполняет локацию новыми сущностями.
     *
     * @param unitFactory Фабрика сущностей.
     */
    public void fill(UnitFactory unitFactory) {
        Random randomEntityIndex = new Random();

        List<String> entityTypes = new ArrayList<>(settingsService.getAnimalSettings().getAnimals().keySet());
        int entityTypesCount = settingsService.getAnimalSettings().getAnimals().size();

        for (int index = 0; index <= entityTypesCount; index++) {
            String entityType = entityTypes.get(randomEntityIndex.nextInt(entityTypesCount));
            Unit unit = unitFactory.createUnit(entityType, settingsService.getAnimalByName(entityType).getAnimalProperties(), position);
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
     * Добавляет сущность на локацию.
     *
     * @param unit Сущность.
     */
    public void add(Unit unit) {
        if (changeUniqueEntities(unit, false)) {
            entitiesOnLocationList.add(unit);
        }
    }

    /**
     * Удаляет сущность с локации.
     *
     * @param unit Сущность.
     */
    public void remove(Unit unit) {
        changeUniqueEntities(unit, true);
        entitiesOnLocationList.remove(unit);
    }

    /**
     * Изменяет счетчик с лимитами уникальных сущностей.
     *
     * @param unit Сущность.
     * @param leave  Признак выхода или смерти сущности из локации.
     * @return Признак успешного изменения счетчика.
     */
    public boolean changeUniqueEntities(Unit unit, boolean leave) {
        String entityName = unit.getClass().getSimpleName();

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
     * Проверяет на превышение лимита уникальных сущностей.
     */
    private boolean checkMaxCountEntities(String entityName, int countUniqueEntityOnLocation) {
        return countUniqueEntityOnLocation <= settingsService.getAnimalSettings().getAnimals().get(entityName).getMaxByLocation() - 1;
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
                    .getAnimals().forEach((key, value) -> result
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
