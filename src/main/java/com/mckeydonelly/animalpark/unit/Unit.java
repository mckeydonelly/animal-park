package com.mckeydonelly.animalpark.unit;

import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.unit.UnitProperties;

/**
 * Stores the state of the creature
 */
public class Unit {
    private final double weight;
    private final String name;
    private final int moveSpeed;
    private final double weightEatToFill;
    protected Position positionOnMap;
    private double weightEaten;
    private volatile boolean dead;
    private volatile boolean readyToReproduction;

    public Unit(Position position, String name, UnitProperties unitProperties) {
        this.name = name;
        this.positionOnMap = position;
        this.weight = unitProperties.getWeight();
        this.moveSpeed = unitProperties.getMoveSpeed();
        this.weightEatToFill = unitProperties.getWeightEatToFill();
        this.weightEaten = weightEatToFill;
        this.readyToReproduction = true;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public Position getPosition() {
        return positionOnMap;
    }

    public void setPosition(Position position) {
        this.positionOnMap = position;
    }

    public double getWeightEaten() {
        return weightEaten;
    }

    public void setWeightEaten(double weightEaten) {
        this.weightEaten = weightEaten;
    }

    public double getWeightEatToFill() {
        return weightEatToFill;
    }

    public boolean isReadyToReproduction() {
        return readyToReproduction;
    }

    public void setReadyToReproduction(boolean readyToReproduction) {
        this.readyToReproduction = readyToReproduction;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public void die() {
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }
}
