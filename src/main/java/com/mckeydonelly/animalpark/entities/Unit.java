package com.mckeydonelly.animalpark.entities;

import com.mckeydonelly.animalpark.map.Position;

/**
 * Базовый интерфейс для всех сущностей.
 */
public interface Unit {
    double getWeight();

    Position getPosition();

    void setPosition(Position position);

    double getWeightEaten();

    void setWeightEaten(double weightEaten);

    double getWeightEatToFill();

    boolean isReadyToReproduction();

    void setReadyToReproduction(boolean readyToReproduction);

    int getMoveSpeed();

    /**
     * Умер
     */
    void die();

    /**
     * Жива ли сущность
     *
     * @return true - жива, false - мертва
     */
    boolean isDead();
}
