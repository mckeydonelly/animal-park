package com.mckeydonelly.animalpark.settings.animal;

/**
 * POJO класс для хранения базовых параметров животных
 */
public class AnimalProperties {
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
