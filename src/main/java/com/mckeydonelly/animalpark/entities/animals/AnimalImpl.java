package com.mckeydonelly.animalpark.entities.animals;

import com.mckeydonelly.animalpark.entities.*;
import com.mckeydonelly.animalpark.map.Location;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.map.Position;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;
import com.mckeydonelly.animalpark.settings.animal.AnimalProperties;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса для животных
 */
public abstract class AnimalImpl implements Animal {
    private final double weight;
    private final int moveSpeed;
    private final double weightEatToFill;
    private final EntityFactory entityFactory;
    protected Position positionOnMap;
    private double weightEaten;
    private boolean dead;
    private volatile boolean readyToReproduction;

    protected AnimalImpl(Position position, EntityFactory entityFactory, AnimalProperties animalProperties) {
        this.positionOnMap = position;
        this.entityFactory = entityFactory;
        this.weight = animalProperties.getWeight();
        this.moveSpeed = animalProperties.getMoveSpeed();
        this.weightEatToFill = animalProperties.getWeightEatToFill();
        this.weightEaten = weightEatToFill;
        this.readyToReproduction = true;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void resetReproduction() {
        readyToReproduction = true;
    }

    @Override
    public void die() {
        dead = true;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public void doTurn(ParkMap parkMap) {

        Location currentLocation = parkMap.getLocation(positionOnMap.row(), positionOnMap.column());

        int availableTypes = ActionTypes.values().length;
        if (weightEaten >= weightEatToFill) {
            availableTypes = availableTypes - 1;
        }

        int actionType = ThreadLocalRandom.current().nextInt(availableTypes);

        switch (ActionTypes.values()[actionType]) {
            case MOVE -> move(parkMap);
            case REPRODUCTION -> reproduction(currentLocation, false);
            case EAT -> eat(currentLocation);
        }

        reducePower(currentLocation);
    }

    @Override
    public void eat(Location location) {
        location.lockLocation();

        try {
            Set<String> eatableList = EatingProcessor.getEatableList(this);
            ArrayList<Entity> eatableEntities = location.getEntitiesOnLocationList().stream()
                    .filter(entity -> eatableList.contains(entity.getClass().getSimpleName().toUpperCase()))
                    .collect(Collectors.toCollection(ArrayList::new));

            if (!eatableEntities.isEmpty()) {
                boolean successEat = false;

                while (!successEat) {
                    int randomTarget = ThreadLocalRandom.current().nextInt(eatableEntities.size());
                    Entity targetEntity = eatableEntities.get(randomTarget);
                    if (!targetEntity.isDead()) {
                        if (EatingProcessor.getEatResult(this, targetEntity)) {
                            weightEaten = Math.min(weightEaten + targetEntity.getWeight(), weightEatToFill);
                            targetEntity.die();
                            location.remove(targetEntity);
                            successEat = true;
                        } else {
                            eatableEntities.remove(targetEntity);
                        }
                    }
                }
            }
        } finally {
            location.unlockLocation();
        }
    }

    @Override
    public void reproduction(Location location, boolean externalReproduction) {
        if (!readyToReproduction) {
            return;
        }

        if (externalReproduction) {
            readyToReproduction = false;
            return;
        }

        location.lockLocation();

        try {
            Entity compatibleForReproduction = location.getEntitiesOnLocationList().stream()
                    .filter(entity -> entity.getClass().equals(this.getClass()))
                    .findAny()
                    .orElse(null);

            if (compatibleForReproduction != null) {
                compatibleForReproduction.reproduction(location, true);
                Entity childEntity = entityFactory.createEntity(EntityType.valueOf(this.getClass().getSimpleName().toUpperCase()), positionOnMap);
                location.add(childEntity);
                readyToReproduction = false;
            }
        } finally {
            location.unlockLocation();
        }
    }

    @Override
    public void move(ParkMap parkMap) {
        Location currentLocation = parkMap.getLocation(positionOnMap.row(), positionOnMap.column());
        Location endLocation = currentLocation;
        int stepsToMove = moveSpeed;

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
                currentLocation.remove(this);
            } finally {
                currentLocation.unlockLocation();
            }

            endLocation.lockLocation();
            try {
                endLocation.add(this);
            } finally {
                endLocation.unlockLocation();
            }

            positionOnMap = endLocation.getPosition();
        }
    }

    /**
     * Уменьшает запас сил (еды в желудке) животного и если он погибает, то удаляет его из карты
     * @param location Текущая локация на карте
     */
    private void reducePower(Location location) {
        weightEaten -= weightEatToFill / SimulationSettings.get(SettingsType.TURNS_FOR_DIE_BY_MAX_FILL);
        if (weightEaten <= 0) {
            location.lockLocation();
            try {
                die();
                location.remove(this);
            } finally {
                location.unlockLocation();
            }
        }
    }

    /**
     * Вычисляет позицию последующего положения животного и проверяет возможность перемещения
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

        if (nextPosition.column() >= SimulationSettings.get(SettingsType.MAP_COLUMNS) ||
                nextPosition.column() < 0 ||
                nextPosition.row() >= SimulationSettings.get(SettingsType.MAP_ROWS) ||
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
