package com.mckeydonelly.animalpark.activity;

import com.mckeydonelly.animalpark.map.Location;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;
import com.mckeydonelly.animalpark.unit.EatingProcessor;
import com.mckeydonelly.animalpark.unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Contains logic for unit action
 */
public class UnitActions {
    private final ParkMap parkMap;
    private final SimulationSettings settings;
    private final EatingProcessor eatingProcessor;
    private final SettingsService settingsService;

    private final Map<ActionTypes, Consumer<Unit>> actionMapper = Map.of(
            ActionTypes.MOVE, this::move,
            ActionTypes.REPRODUCTION, this::reproduction,
            ActionTypes.EAT, this::eat
    );

    public UnitActions(ParkMap parkMap, SimulationSettings settings, EatingProcessor eatingProcessor, SettingsService settingsService) {
        this.parkMap = parkMap;
        this.settings = settings;
        this.eatingProcessor = eatingProcessor;
        this.settingsService = settingsService;
    }

    /**
     * Return available actions for this unit by his type
     * @param unit unit
     * @return list of available types with lambdas
     */
    public List<Consumer<Unit>> getAvailableActions(Unit unit) {
        List<ActionTypes> availableActionsConfiguration = settingsService.getUnitByName(unit.getName()).getAvailableActions();

        List<Consumer<Unit>> availableActions = new ArrayList<>();

        for (ActionTypes actionType : ActionTypes.values()) {
            if(availableActionsConfiguration.contains(actionType)) {
                availableActions.add(actionMapper.get(actionType));
            }
        }

        return availableActions;
    }

    /**
     * Eating action
     *
     * @param unit unit
     */
    public void eat(Unit unit) {
        Location location = parkMap.getLocation(unit.getPosition().row(), unit.getPosition().column());

        location.lockLocation();

        try {
            Set<String> eatableList = eatingProcessor.getEatableList(unit.getName());
            ArrayList<Unit> eatableEntities = location.getEntitiesOnLocationList().stream()
                    .filter(entityOnLoc -> eatableList.contains(entityOnLoc.getName()))
                    .collect(Collectors.toCollection(ArrayList::new));

            if (!eatableEntities.isEmpty()) {
                int randomTarget = ThreadLocalRandom.current().nextInt(eatableEntities.size());
                Unit targetUnit = eatableEntities.get(randomTarget);
                if (!targetUnit.isDead() && eatingProcessor.getEatResult(unit.getName(), targetUnit.getName())) {
                    unit.setWeightEaten(Math.min(unit.getWeightEaten() + targetUnit.getWeight(), unit.getWeightEatToFill()));
                    targetUnit.die();
                    location.remove(targetUnit);
                }
            }
        } finally {
            location.unlockLocation();
        }
    }

    /**
     * Reproduction action
     *
     * @param unit unit
     */
    public void reproduction(Unit unit) {
        if (!unit.isReadyToReproduction()) {
            return;
        }

        Location location = parkMap.getLocation(unit.getPosition().row(), unit.getPosition().column());
        location.lockLocation();

        try {
            Unit partnerForReproduction = location.getEntitiesOnLocationList().stream()
                    .filter(entityOnLoc -> entityOnLoc.getName().equals(unit.getName()))
                    .findAny()
                    .orElse(null);

            if (partnerForReproduction != null) {
                partnerForReproduction.setReadyToReproduction(false);
                Unit childUnit = new Unit(unit.getPosition(), unit.getName(), settingsService.getUnitByName(unit.getName()).getUnitProperties());
                location.add(childUnit);
                partnerForReproduction.setReadyToReproduction(false);
            }
        } finally {
            location.unlockLocation();
        }
    }

    /**
     * Move action
     *
     * @param unit unit
     */
    public void move(Unit unit) {
        Location currentLocation = parkMap.getLocation(unit.getPosition().row(), unit.getPosition().column());
        Location endLocation = currentLocation;
        int stepsToMove = unit.getMoveSpeed();

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
                currentLocation.remove(unit);
            } finally {
                currentLocation.unlockLocation();
            }

            endLocation.lockLocation();
            try {
                endLocation.add(unit);
            } finally {
                endLocation.unlockLocation();
            }

            unit.setPosition(endLocation.getPosition());
        }
    }

    /**
     * Calculation next position and possibility to move
     *
     * @param parkMap park map
     * @param currentLocation current location
     * @param direction direction for move
     * @return next location
     *
     * If unit can't possibly move to next location with this direction (end of map or next location contains maximum units by this type)
     * returns current position
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

        if (isEndOfMapByColumn(nextPosition.column()) || isEndOfMapByRow(nextPosition.row())) {
            return currentLocation;
        } else {
            return parkMap.getLocation(nextPosition.row(), nextPosition.column());
        }
    }

    private boolean isEndOfMapByColumn(int column) {
        return column >= settings.get(SettingsType.MAP_COLUMNS) || column < 0;
    }

    private boolean isEndOfMapByRow(int row) {
        return row >= settings.get(SettingsType.MAP_ROWS) || row < 0;
    }

    enum Direction {
        DOWN,
        UP,
        LEFT,
        RIGHT
    }
}
