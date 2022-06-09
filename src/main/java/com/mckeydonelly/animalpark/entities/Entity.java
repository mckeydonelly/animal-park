package com.mckeydonelly.animalpark.entities;

import com.mckeydonelly.animalpark.map.Location;
import com.mckeydonelly.animalpark.map.ParkMap;

/**
 * Базовый интерфейс для всех сущностей.
 */
public interface Entity {
    double getWeight();
    void resetReproduction();
    void doTurn(ParkMap parkMap);
    void eat(Location location);
    void reproduction(Location location, boolean externalReproduction);
    void move(ParkMap parkMap);
}
