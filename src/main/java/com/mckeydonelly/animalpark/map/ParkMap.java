package com.mckeydonelly.animalpark.map;

import java.util.List;

/**
 * Хранит карту симуляции парка.
 */
public class ParkMap {
    private final List<List<Location>> map;

    public ParkMap(List<List<Location>> map) {
        this.map = map;
    }

    public List<List<Location>> getMap() {
        return map;
    }

    /**
     * Возвращает локацию по координатам.
     * @param row - номер строки
     * @param column - номер столбца
     * @return локация
     */
    public Location getLocation(int row, int column) {
        try {
            return map.get(row).get(column);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Неверные координаты");
        }
    }
}
