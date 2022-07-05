package com.mckeydonelly.animalpark.map;

import com.mckeydonelly.animalpark.creature.Creature;
import com.mckeydonelly.animalpark.creature.CreatureTypes;
import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class LocationProcessor {
    private final SettingsService settingsService;
    private final SimulationSettings simulationSettings;

    public LocationProcessor(SettingsService settingsService, SimulationSettings simulationSettings) {
        this.settingsService = settingsService;
        this.simulationSettings = simulationSettings;
    }


    /**
     * Filling location for the starting simulation.
     */
    public void fill(ParkMap parkMap) {
        List<String> entityTypes = new ArrayList<>(settingsService.getCreaturesSettings().getCreatures().keySet());
        int startingCreaturesCount = simulationSettings.get(SettingsType.STARTING_CREATURES_COUNT_BY_LOCATION);

        parkMap.getAllLocations()
                .forEach(location -> {
                    for (int index = 0; index <= startingCreaturesCount; index++) {
                        String entityType = entityTypes.get(ThreadLocalRandom.current().nextInt(startingCreaturesCount));
                        Creature creature = new Creature(location.getPosition(), entityType, settingsService.getCreatureByName(entityType).getCreatureProperties());
                        location.add(creature);
                    }
                });
    }

    /**
     * Growing new plants on location during the simulation
     */
    public void growPlants(ParkMap parkMap) {
        List<String> plantTypes = settingsService.getCreaturesSettings()
                .getCreatures()
                .entrySet()
                .stream()
                .filter(creature -> CreatureTypes.PLANT.equals(creature.getValue().getType()))
                .map(Map.Entry::getKey)
                .toList();
        int plantGrowingCount = simulationSettings.get(SettingsType.PLANTS_GROWING_COUNT);

        parkMap.getAllLocations()
                .forEach(location -> {
                    location.lockLocation();
                    try {
                        for (int index = 0; index <= plantGrowingCount; index++) {
                            String entityType = plantTypes.get(ThreadLocalRandom.current().nextInt(plantTypes.size()));
                            Creature creature = new Creature(location.getPosition(), entityType, settingsService.getCreatureByName(entityType).getCreatureProperties());
                            location.add(creature);
                        }
                    } finally {
                        location.unlockLocation();
                    }
                });
    }
}
