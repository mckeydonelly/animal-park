package com.mckeydonelly.animalpark.activity;

import com.mckeydonelly.animalpark.entities.ActionTypes;
import com.mckeydonelly.animalpark.entities.EatingProcessor;
import com.mckeydonelly.animalpark.entities.Entity;
import com.mckeydonelly.animalpark.entities.EntityFactory;
import com.mckeydonelly.animalpark.entities.animals.Animal;
import com.mckeydonelly.animalpark.map.Location;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class LifeCycleProcessor {
    private final ParkMap parkMap;
    private final EntityFactory entityFactory;
    private final SimulationSettings settings;
    private final EatingProcessor eatingProcessor;
    private final SettingsService settingsService;

    public LifeCycleProcessor(ParkMap parkMap,
                              EntityFactory entityFactory,
                              SimulationSettings settings,
                              EatingProcessor eatingProcessor,
                              SettingsService settingsService) {
        this.parkMap = parkMap;
        this.entityFactory = entityFactory;
        this.settings = settings;
        this.eatingProcessor = eatingProcessor;
        this.settingsService = settingsService;
    }

    /**
     * Выполняет ход животного
     *
     * @param entity животное
     */
    public void doTurn(Entity entity) {

        if (!(entity instanceof Animal)) {
            return;
        }

        Location currentLocation = parkMap.getLocation(entity.getPosition().row(), entity.getPosition().column());

        int availableTypes = ActionTypes.values().length;
        if (entity.getWeightEaten() >= entity.getWeightEatToFill()) {
            availableTypes = availableTypes - 1;
        }

        int actionType = ThreadLocalRandom.current().nextInt(availableTypes);

        switch (ActionTypes.values()[actionType]) {
            case MOVE -> move(entity, parkMap);
            case REPRODUCTION -> reproduction(entity, currentLocation);
            case EAT -> eat(entity, currentLocation);
        }

        reducePower(entity, currentLocation);
    }

    /**
     * Уменьшает запас сил (еды в желудке) животного и если он погибает, то удаляет его из карты
     *
     * @param location Текущая локация на карте
     */
    private void reducePower(Entity entity, Location location) {
        entity.setWeightEaten(entity.getWeightEaten() - (entity.getWeightEatToFill() / settings.get(SettingsType.TURNS_FOR_DIE_BY_MAX_FILL)));
        if (entity.getWeightEaten() <= 0) {
            location.lockLocation();
            try {
                entity.die();
                location.remove(entity);
            } finally {
                location.unlockLocation();
            }
        }
    }

    /**
     * Существо ест
     *
     * @param entity   животное
     * @param location Локация для поиска еды
     */
    public void eat(Entity entity, Location location) {
        location.lockLocation();

        try {
            Set<String> eatableList = eatingProcessor.getEatableList(entity);
            ArrayList<Entity> eatableEntities = location.getEntitiesOnLocationList().stream()
                    .filter(entityOnLoc -> eatableList.contains(entityOnLoc.getClass().getSimpleName()))
                    .collect(Collectors.toCollection(ArrayList::new));

            if (!eatableEntities.isEmpty()) {
                int randomTarget = ThreadLocalRandom.current().nextInt(eatableEntities.size());
                Entity targetEntity = eatableEntities.get(randomTarget);
                if (!targetEntity.isDead() && eatingProcessor.getEatResult(entity, targetEntity)) {
                        entity.setWeightEaten(Math.min(entity.getWeightEaten() + targetEntity.getWeight(), entity.getWeightEatToFill()));
                        targetEntity.die();
                        location.remove(targetEntity);
                }
            }
        } finally {
            location.unlockLocation();
        }
    }

    /**
     * Существо размножается
     *
     * @param entity   животное
     * @param location Локация для поиска существа для размножения
     */
    public void reproduction(Entity entity, Location location) {
        if (!entity.isReadyToReproduction()) {
            return;
        }

        location.lockLocation();

        try {
            Entity partnerForReproduction = location.getEntitiesOnLocationList().stream()
                    .filter(entityOnLoc -> entityOnLoc.getClass().equals(entity.getClass()))
                    .findAny()
                    .orElse(null);

            if (partnerForReproduction != null) {
                partnerForReproduction.setReadyToReproduction(false);
                Entity childEntity = entityFactory.createEntity(entity.getClass().getSimpleName(),
                        settingsService.getAnimalByName(entity.getClass().getSimpleName()).getAnimalProperties(),
                        entity.getPosition());
                location.add(childEntity);
                partnerForReproduction.setReadyToReproduction(false);
            }
        } finally {
            location.unlockLocation();
        }
    }

    /**
     * Существо перемещается
     *
     * @param entity  животное
     * @param parkMap Карта парка
     */
    public void move(Entity entity, ParkMap parkMap) {
        Location currentLocation = parkMap.getLocation(entity.getPosition().row(), entity.getPosition().column());
        Location endLocation = currentLocation;
        int stepsToMove = entity.getMoveSpeed();

        while (stepsToMove > 0) {
            int directionIndex = ThreadLocalRandom.current().nextInt(Direction.values().length);
            endLocation = getNextPosition(parkMap, endLocation, Direction.values()[directionIndex]);
            if (endLocation == null) {
                endLocation = currentLocation;
            }

            stepsToMove--;
        }

        if (currentLocation != endLocation) {
            currentLocation.lockLocation();
            try {
                currentLocation.remove(entity);
            } finally {
                currentLocation.unlockLocation();
            }

            endLocation.lockLocation();
            try {
                endLocation.add(entity);
            } finally {
                endLocation.unlockLocation();
            }

            entity.setPosition(endLocation.getPosition());
        }
    }

    /**
     * Вычисляет позицию последующего положения животного и проверяет возможность перемещения
     *
     * @param parkMap         Карта парка
     * @param currentLocation Текущая локация на карте
     * @param direction       Направление движения
     * @return Локация последующего положения животного
     */
    private Location getNextPosition(ParkMap parkMap, Location currentLocation, Direction direction) {
        int currentRow = currentLocation.getPosition().row();
        int currentColumn = currentLocation.getPosition().column();

        Position nextPosition = switch (direction) {
            case DOWN -> new Position(currentRow + 1, currentColumn);
            case UP -> new Position(currentRow - 1, currentColumn);
            case LEFT -> new Position(currentRow, currentColumn - 1);
            case RIGHT -> new Position(currentRow, currentColumn + 1);
        };

        if (nextPosition.column() >= settings.get(SettingsType.MAP_COLUMNS) ||
                nextPosition.column() < 0 ||
                nextPosition.row() >= settings.get(SettingsType.MAP_ROWS) ||
                nextPosition.row() < 0) {
            return currentLocation;
        } else {
            return parkMap.getLocation(nextPosition.row(), nextPosition.column());
        }
    }

    enum Direction {
        DOWN,
        UP,
        LEFT,
        RIGHT
    }
}
