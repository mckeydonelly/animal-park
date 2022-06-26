package com.mckeydonelly.animalpark.settings.creature;

/**
 * POJO class for storing basic parameters of creature
 */
public class CreatureProperties {
    private int weight;
    private int moveSpeed;
    private double weightEatToFill;

    public int getWeight() {
        return weight;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public double getWeightEatToFill() {
        return weightEatToFill;
    }
}
