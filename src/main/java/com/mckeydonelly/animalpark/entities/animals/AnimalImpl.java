package com.mckeydonelly.animalpark.entities.animals;

import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.animal.AnimalProperties;

/**
 * Реализация интерфейса для животных
 */
public abstract class AnimalImpl implements Animal {
    private final double weight;
    private final int moveSpeed;
    private final double weightEatToFill;
    protected Position positionOnMap;
    private double weightEaten;
    private volatile boolean dead;
    private volatile boolean readyToReproduction;

    protected AnimalImpl(Position position, AnimalProperties animalProperties) {
        this.positionOnMap = position;
        this.weight = animalProperties.getWeight();
        this.moveSpeed = animalProperties.getMoveSpeed();
        this.weightEatToFill = animalProperties.getWeightEatToFill();
        this.weightEaten = weightEatToFill;
        this.readyToReproduction = true;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public Position getPosition() {
        return positionOnMap;
    }

    @Override
    public void setPosition(Position position) {
        this.positionOnMap = position;
    }

    @Override
    public double getWeightEaten() {
        return weightEaten;
    }

    @Override
    public void setWeightEaten(double weightEaten) {
        this.weightEaten = weightEaten;
    }

    @Override
    public double getWeightEatToFill() {
        return weightEatToFill;
    }

    @Override
    public boolean isReadyToReproduction() {
        return readyToReproduction;
    }

    @Override
    public void setReadyToReproduction(boolean readyToReproduction) {
        this.readyToReproduction = readyToReproduction;
    }

    @Override
    public int getMoveSpeed() {
        return moveSpeed;
    }

    @Override
    public void die() {
        dead = true;
    }

    @Override
    public boolean isDead() {
        return dead;
    }
}
