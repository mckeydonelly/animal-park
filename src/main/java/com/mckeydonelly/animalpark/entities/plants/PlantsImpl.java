package com.mckeydonelly.animalpark.entities.plants;

import com.mckeydonelly.animalpark.map.Position;
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
    public void die() {
        dead = true;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public void setPosition(Position position) {

    }

    @Override
    public double getWeightEaten() {
        return 0;
    }

    @Override
    public void setWeightEaten(double weightEaten) {

    }

    @Override
    public double getWeightEatToFill() {
        return 0;
    }

    @Override
    public boolean isReadyToReproduction() {
        return false;
    }

    @Override
    public void setReadyToReproduction(boolean readyToReproduction) {
    }

    @Override
    public int getMoveSpeed() {
        return 0;
    }
}
