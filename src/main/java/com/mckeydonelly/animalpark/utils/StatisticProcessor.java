package com.mckeydonelly.animalpark.utils;

import com.mckeydonelly.animalpark.entities.EntityType;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.utils.console.ConsoleReaderHelper;
import com.mckeydonelly.animalpark.menu.IngameMenu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Вывод статистики по парку
 */
public class StatisticProcessor {
    private final IngameMenu ingameMenu;

    public StatisticProcessor(IngameMenu ingameMenu) {
        this.ingameMenu = ingameMenu;
    }

    public void printStatistic(ParkMap parkMap, int totalTurns, int turnNumber) {
        ConsoleReaderHelper.clearConsole();

        System.out.println(ingameMenu.getIngameMenu());
        System.out.println("Turn - " + turnNumber);
        System.out.println("Total turns - " + totalTurns);
        System.out.println("\nPopulation by type for all map:\n");

        StringBuilder statPrint = new StringBuilder();
        Map<String, Integer> uniqueEntitiesTotal = new HashMap<>();

        parkMap.getMap().stream()
                .flatMap(List::stream)
                .toList()
                .forEach(location -> location.getUniqueEntitiesCount().forEach((k, v) -> uniqueEntitiesTotal.merge(k, v, Integer::sum)));

        Arrays.stream(EntityType.values()).forEach(entityType -> statPrint
                .append(entityType.getEmoji())
                .append("=")
                .append(uniqueEntitiesTotal.getOrDefault(entityType.toString(), 0))
                .append("\n"));

        System.out.println(statPrint);
    }
}
