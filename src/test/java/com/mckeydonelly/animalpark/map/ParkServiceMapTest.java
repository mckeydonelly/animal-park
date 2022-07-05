package com.mckeydonelly.animalpark.map;

import com.mckeydonelly.animalpark.settings.SettingsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParkServiceMapTest {
    private static ParkMap parkMap;
    private static SettingsService settingsService;
    private static int MAP_ROWS = 5;
    private static int MAP_COLUMNS = 5;

    @BeforeAll
    static void setUp() {
        settingsService = new SettingsService();
        List<List<Location>> tmpMap = new ArrayList<>();

        for (int row = 0; row < MAP_ROWS; row++) {
            List<Location> columnArray = new ArrayList<>();
            tmpMap.add(columnArray);
            for (int column = 0; column < MAP_COLUMNS; column++) {
                columnArray.add(new Location(new Position(row, column), settingsService));
            }
        }

        parkMap = new ParkMap(tmpMap);
    }

    @Test
    void testGetUnknownLocation() {
        assertThrows(IllegalArgumentException.class, () -> parkMap.getLocation(MAP_ROWS+1, MAP_COLUMNS));
    }
}