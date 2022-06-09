package com.mckeydonelly.animalpark.map;

import com.mckeydonelly.animalpark.entities.EntityFactory;
import com.mckeydonelly.animalpark.entities.EntityFactoryImpl;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Создает и заполняет карту начальными значениями по заданным настройкам.
 */
public class MapProcessor {
    private final EntityFactory entityFactory = new EntityFactoryImpl();

    public ParkMap create() {
        System.out.println("Start creating map...");

        ParkMap parkMap = initializeMap();
        generateCreature(parkMap);

        System.out.println("World creating completed");

        return parkMap;
    }

    private ParkMap initializeMap() {
        System.out.println("Creating locations...");

        int mapRows = SimulationSettings.get(SettingsType.MAP_ROWS);
        int mapColumns = SimulationSettings.get(SettingsType.MAP_COLUMNS);

        List<List<Location>> tmpMap = new ArrayList<>();

        for (int row = 0; row < mapRows; row++) {
            List<Location> columnArray = new ArrayList<>();
            tmpMap.add(columnArray);
            for (int column = 0; column < mapColumns; column++) {
                columnArray.add(new Location(new Position(row, column)));
            }
        }

        return new ParkMap(tmpMap);
    }

    private void generateCreature(ParkMap parkMap) {
        System.out.println("Create creatures... growing plants...");

        parkMap.getMap().stream()
                .flatMap(List::stream)
                .toList()
                .forEach(location -> location.fill(entityFactory));
    }
}
