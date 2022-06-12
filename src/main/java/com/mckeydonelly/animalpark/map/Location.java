package com.mckeydonelly.animalpark.map;

import com.mckeydonelly.animalpark.entities.Entity;
import com.mckeydonelly.animalpark.entities.EntityFactory;
import com.mckeydonelly.animalpark.settings.SettingsService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Локация на карте.
 */
public class Location {
    private final List<Entity> entitiesOnLocationList = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, Integer> uniqueEntitiesCount = new ConcurrentHashMap<>();
    private final Position position;
    private final Lock lock = new ReentrantLock(true);

    public Location(Position position) {
        this.position = position;
    }

    public synchronized List<Entity> getEntitiesOnLocationList() {
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
     * @param entityFactory Фабрика сущностей.
     */
    public void fill(EntityFactory entityFactory) {
        Random randomEntityIndex = new Random();

        List<String> entityTypes = new ArrayList<>(SettingsService.getAnimalSettings().getAnimals().keySet());
        int entityTypesCount = SettingsService.getAnimalSettings().getAnimals().size();

        for (int index = 0; index <= entityTypesCount; index++) {
            String entityType = entityTypes.get(randomEntityIndex.nextInt(entityTypesCount));
            Entity entity = entityFactory.createEntity(entityType, position);
            add(entity);
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
     * @param entity Сущность.
     */
    public void add(Entity entity) {
        if (changeUniqueEntities(entity, false)) {
            entitiesOnLocationList.add(entity);
        }
    }

    /**
     * Удаляет сущность с локации.
     *
     * @param entity Сущность.
     */
    public void remove(Entity entity) {
        changeUniqueEntities(entity, true);
        entitiesOnLocationList.remove(entity);
    }

    /**
     * Изменяет счетчик с лимитами уникальных сущностей.
     *
     * @param entity Сущность.
     * @param leave  Признак выхода или смерти сущности из локации.
     * @return Признак успешного изменения счетчика.
     */
    public boolean changeUniqueEntities(Entity entity, boolean leave) {
        String entityName = entity.getClass().getSimpleName();

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
        return countUniqueEntityOnLocation <= SettingsService.getAnimalSettings().getAnimals().get(entityName).getMaxByLocation() - 1;
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
            SettingsService.getAnimalSettings()
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
