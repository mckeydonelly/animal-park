package com.mckeydonelly.animalpark.entities.plants;

import com.mckeydonelly.animalpark.map.Location;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.settings.animal.AnimalProperties;

/**
 * Реализация интерфейса для растений.
 */
public class PlantsImpl implements Plants {
    private final double weight;
    private boolean dead;

    public PlantsImpl(AnimalProperties animalProperties) {
        this.weight = animalProperties.getWeight();
        this.dead = false;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void resetReproduction() {
        // Don't need to do anything.
    }

    @Override
    public void die() {
        dead = true;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public void doTurn(ParkMap parkMap) {
        // Don't need to do anything.
    }

    @Override
    public void eat(Location location) {
        // Don't need to do anything.
    }

    @Override
    public void reproduction(Location location, boolean externalReproduction) {
        // Don't need to do anything.
    }

    @Override
    public void move(ParkMap parkMap) {
        // Don't need to do anything.
    }

}
