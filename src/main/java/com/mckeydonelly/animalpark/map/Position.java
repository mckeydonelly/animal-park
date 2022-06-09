package com.mckeydonelly.animalpark.map;

/**
 * Хранит позицию на карте.
 */
public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int row() {
        return row;
    }

    public int column() {
        return column;
    }

    public void set(int row, int column) {
        this.row = row;
        this.column = column;
    }
}