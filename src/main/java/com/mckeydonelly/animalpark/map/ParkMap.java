package com.mckeydonelly.animalpark.map;

import java.util.List;

/**
 * Simulation map
 */
public class ParkMap {
    private final List<List<Location>> map;

    public ParkMap(List<List<Location>> map) {
        this.map = map;
    }

    /**
     * Return location by coordinates
     * @param row row
     * @param column column
     * @return location
     */
    public Location getLocation(int row, int column) {
        try {
            return map.get(row).get(column);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Unknown coordinates");
        }
    }

    /**
     * Returns all location list
     * @return list of locations
     */
    public List<Location> getAllLocations() {
        return map.stream()
                .flatMap(List::stream)
                .toList();
    }
}
