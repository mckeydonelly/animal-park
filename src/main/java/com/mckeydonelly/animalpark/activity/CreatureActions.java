package com.mckeydonelly.animalpark.activity;

import com.mckeydonelly.animalpark.creature.Creature;
import com.mckeydonelly.animalpark.map.Location;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;
import com.mckeydonelly.animalpark.creature.EatingProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Contains logic for creature action
 */
public class CreatureActions {
    private final ParkMap parkMap;
    private final SimulationSettings settings;
    private final EatingProcessor eatingProcessor;
    private final SettingsService settingsService;

    private final Map<ActionTypes, Consumer<Creature>> actionMapper = Map.of(
            ActionTypes.MOVE, this::move,
            ActionTypes.REPRODUCTION, this::reproduction,
            ActionTypes.EAT, this::eat
    );

    public CreatureActions(ParkMap parkMap, SimulationSettings settings, EatingProcessor eatingProcessor, SettingsService settingsService) {
        this.parkMap = parkMap;
        this.settings = settings;
        this.eatingProcessor = eatingProcessor;
        this.settingsService = settingsService;
    }

    /**
     * Return available actions for this creature by his type
     * @param creature creature
     * @return list of available types with lambdas
     */
    public List<Consumer<Creature>> getAvailableActions(Creature creature) {
        List<ActionTypes> availableActionsConfiguration = settingsService.getCreatureByName(creature.getName()).getAvailableActions();

        List<Consumer<Creature>> availableActions = new ArrayList<>();

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
     * @param creature creature
     */
    public void eat(Creature creature) {
        Location location = parkMap.getLocation(creature.getPosition().row(), creature.getPosition().column());

        location.lockLocation();

        try {
            Set<String> eatableList = eatingProcessor.getEatableList(creature.getName());
            ArrayList<Creature> eatableEntities = location.getEntitiesOnLocationList()
                    .stream()
                    .filter(entityOnLoc -> eatableList.contains(entityOnLoc.getName()))
                    .collect(Collectors.toCollection(ArrayList::new));

            if (!eatableEntities.isEmpty()) {
                int randomTarget = ThreadLocalRandom.current().nextInt(eatableEntities.size());
                Creature targetCreature = eatableEntities.get(randomTarget);
                if (isEaten(creature, targetCreature)) {
                    creature.setWeightEaten(Math.min(creature.getWeightEaten() + targetCreature.getWeight(), creature.getWeightEatToFill()));
                    targetCreature.die();
                    location.remove(targetCreature);
                }
            }
        } finally {
            location.unlockLocation();
        }
    }

    /**
     * Reproduction action
     *
     * @param creature creature
     */
    public void reproduction(Creature creature) {
        if (!creature.isReadyToReproduction()) {
            return;
        }

        Location location = parkMap.getLocation(creature.getPosition().row(), creature.getPosition().column());
        location.lockLocation();

        try {
            Creature partnerForReproduction = location.getEntitiesOnLocationList().stream()
                    .filter(entityOnLoc -> entityOnLoc.getName().equals(creature.getName()))
                    .findAny()
                    .orElse(null);

            if (partnerForReproduction != null) {
                creature.setReadyToReproduction(false);
                Creature childCreature = new Creature(creature.getPosition(), creature.getName(), settingsService.getCreatureByName(creature.getName()).getCreatureProperties());
                location.add(childCreature);
                partnerForReproduction.setReadyToReproduction(false);
            }
        } finally {
            location.unlockLocation();
        }
    }

    /**
     * Move action
     *
     * @param creature creature
     */
    public void move(Creature creature) {
        Location currentLocation = parkMap.getLocation(creature.getPosition().row(), creature.getPosition().column());
        Location endLocation = currentLocation;
        int stepsToMove = creature.getMoveSpeed();

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
                currentLocation.remove(creature);
            } finally {
                currentLocation.unlockLocation();
            }

            endLocation.lockLocation();
            try {
                endLocation.add(creature);
            } finally {
                endLocation.unlockLocation();
            }

            creature.setPosition(endLocation.getPosition());
        }
    }

    private boolean isEaten(Creature creature, Creature targetCreature) {
        return !targetCreature.isDead() && eatingProcessor.getEatResult(creature.getName(), targetCreature.getName());
    }

    /**
     * Calculation next position and possibility to move
     *
     * @param parkMap park map
     * @param currentLocation current location
     * @param direction direction for move
     * @return next location
     *
     * If creature can't possibly move to next location with this direction (end of map or next location contains maximum creatures by this type)
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
