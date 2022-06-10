package com.mckeydonelly.animalpark.entities;

import com.mckeydonelly.animalpark.map.Location;
import com.mckeydonelly.animalpark.map.ParkMap;

/**
 * Базовый интерфейс для всех сущностей.
 */
public interface Entity {
    double getWeight();

    /**
     * Сбрасывает счетчик готовности к размножению
     */
    void resetReproduction();

    /**
     * Умер
     */
    void die();

    /**
     * Жива ли сущность
     * @return true - жива, false - мертва
     */
    boolean isDead();

    /**
     * Выполняет ход животного
     * @param parkMap Карта парка
     */
    void doTurn(ParkMap parkMap);

    /**
     * Существо ест
     * @param location Локация для поиска еды
     */
    void eat(Location location);

    /**
     * Существо размножается
     * @param location Локация для поиска существа для размножения
     * @param externalReproduction флаг внешнего размножения
     */
    void reproduction(Location location, boolean externalReproduction);

    /**
     * Существо перемещается
     * @param parkMap Карта парка
     */
    void move(ParkMap parkMap);
}
