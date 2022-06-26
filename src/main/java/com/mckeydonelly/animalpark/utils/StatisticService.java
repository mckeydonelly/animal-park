package com.mckeydonelly.animalpark.utils;

import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.menu.IngameMenu;
import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.utils.console.ConsoleReaderHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Printing statistics for the park
 */
public class StatisticService {
    private final IngameMenu ingameMenu;
    private final SettingsService settingsService;

    public StatisticService(IngameMenu ingameMenu, SettingsService settingsService) {
        this.ingameMenu = ingameMenu;
        this.settingsService = settingsService;
    }

    public void printStatistic(ParkMap parkMap, int totalTurns, int turnNumber) {
        ConsoleReaderHelper.clearConsole();

        System.out.println(ingameMenu.getIngameMenu());
        System.out.println("Turn - " + turnNumber);
        System.out.println("Total turns - " + totalTurns);
        System.out.println("\nPopulation by type for all map:\n");

        StringBuilder statPrint = new StringBuilder();
        Map<String, Integer> uniqueEntitiesTotal = new HashMap<>();

        parkMap.getAllLocations()
                .forEach(location -> location.getUniqueEntitiesCount().forEach((k, v) -> uniqueEntitiesTotal.merge(k, v, Integer::sum)));

        settingsService.getCreaturesSettings()
                .getCreatures().forEach((key, value) -> statPrint
                        .append(value.getEmoji())
                        .append("=")
                        .append(uniqueEntitiesTotal.getOrDefault(key, 0))
                        .append("\n"));

        System.out.println(statPrint);
    }
}
