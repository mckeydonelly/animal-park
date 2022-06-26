package com.mckeydonelly.animalpark.map;

import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Create and initialize simulation map
 */
public class ParkMapInitializer {
    private final SettingsService settingsService;
    private final LocationProcessor locationProcessor;

    public ParkMapInitializer(SettingsService settingsService, LocationProcessor locationProcessor) {
        this.settingsService = settingsService;
        this.locationProcessor = locationProcessor;
    }

    /**
     * Initializes the map and fills it with creatures
     * @param settings simulation settings
     * @return park map
     */
    public ParkMap create(SimulationSettings settings) {
        System.out.println("Start creating map...");

        ParkMap parkMap = initializeMap(settings);
        generateCreature(parkMap);

        System.out.println("World creating completed");

        return parkMap;
    }

    /**
     * Initialize row and columns
     * @param settings simulation settings
     * @return created map
     */
    private ParkMap initializeMap(SimulationSettings settings) {
        System.out.println("Creating locations...");

        int mapRows = settings.get(SettingsType.MAP_ROWS);
        int mapColumns = settings.get(SettingsType.MAP_COLUMNS);

        List<List<Location>> tmpMap = new ArrayList<>();

        for (int row = 0; row < mapRows; row++) {
            List<Location> columnArray = new ArrayList<>();
            tmpMap.add(columnArray);
            for (int column = 0; column < mapColumns; column++) {
                columnArray.add(new Location(new Position(row, column), settingsService));
            }
        }

        return new ParkMap(tmpMap);
    }

    /**
     * Fills locations starting creatures
     * @param parkMap empty map
     */
    private void generateCreature(ParkMap parkMap) {
        System.out.println("Create creatures... growing plants...");

        locationProcessor.fill(parkMap);
    }
}
