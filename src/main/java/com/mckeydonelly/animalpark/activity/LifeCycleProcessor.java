package com.mckeydonelly.animalpark.activity;

import com.mckeydonelly.animalpark.entities.ActionTypes;
import com.mckeydonelly.animalpark.entities.EatingProcessor;
import com.mckeydonelly.animalpark.entities.Unit;
import com.mckeydonelly.animalpark.entities.UnitFactory;
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
    private final UnitFactory unitFactory;
    private final SimulationSettings settings;
    private final EatingProcessor eatingProcessor;
    private final SettingsService settingsService;

    public LifeCycleProcessor(ParkMap parkMap,
                              UnitFactory unitFactory,
                              SimulationSettings settings,
                              EatingProcessor eatingProcessor,
                              SettingsService settingsService) {
        this.parkMap = parkMap;
        this.unitFactory = unitFactory;
        this.settings = settings;
        this.eatingProcessor = eatingProcessor;
        this.settingsService = settingsService;
    }

    /**
     * Выполняет ход животного
     *
     * @param unit животное
     */
    public void doTurn(Unit unit) {
        if(unit.isDead()) {
            return;
        }

        if (!(unit instanceof Animal)) {
            return;
        }

        Location currentLocation = parkMap.getLocation(unit.getPosition().row(), unit.getPosition().column());

        int availableTypes = ActionTypes.values().length;
        if (unit.getWeightEaten() >= unit.getWeightEatToFill()) {
            availableTypes = availableTypes - 1;
        }

        int actionType = ThreadLocalRandom.current().nextInt(availableTypes);

        switch (ActionTypes.values()[actionType]) {
            case MOVE -> move(unit, parkMap);
            case REPRODUCTION -> reproduction(unit, currentLocation);
            case EAT -> eat(unit, currentLocation);
        }

        reducePower(unit, currentLocation);
    }

    /**
     * Уменьшает запас сил (еды в желудке) животного и если он погибает, то удаляет его из карты
     *
     * @param location Текущая локация на карте
     */
    private void reducePower(Unit unit, Location location) {
        unit.setWeightEaten(unit.getWeightEaten() - (unit.getWeightEatToFill() / settings.get(SettingsType.TURNS_FOR_DIE_BY_MAX_FILL)));
        if (unit.getWeightEaten() <= 0) {
            location.lockLocation();
            try {
                unit.die();
                location.remove(unit);
            } finally {
                location.unlockLocation();
            }
        }
    }

    /**
     * Существо ест
     *
     * @param unit   животное
     * @param location Локация для поиска еды
     */
    public void eat(Unit unit, Location location) {
        location.lockLocation();

        try {
            Set<String> eatableList = eatingProcessor.getEatableList(unit);
            ArrayList<Unit> eatableEntities = location.getEntitiesOnLocationList().stream()
                    .filter(entityOnLoc -> eatableList.contains(entityOnLoc.getClass().getSimpleName()))
                    .collect(Collectors.toCollection(ArrayList::new));

            if (!eatableEntities.isEmpty()) {
                int randomTarget = ThreadLocalRandom.current().nextInt(eatableEntities.size());
                Unit targetUnit = eatableEntities.get(randomTarget);
                if (!targetUnit.isDead() && eatingProcessor.getEatResult(unit, targetUnit)) {
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
     * Существо размножается
     *
     * @param unit   животное
     * @param location Локация для поиска существа для размножения
     */
    public void reproduction(Unit unit, Location location) {
        if (!unit.isReadyToReproduction()) {
            return;
        }

        location.lockLocation();

        try {
            Unit partnerForReproduction = location.getEntitiesOnLocationList().stream()
                    .filter(entityOnLoc -> entityOnLoc.getClass().equals(unit.getClass()))
                    .findAny()
                    .orElse(null);

            if (partnerForReproduction != null) {
                partnerForReproduction.setReadyToReproduction(false);
                Unit childUnit = unitFactory.createUnit(unit.getClass().getSimpleName(),
                        settingsService.getAnimalByName(unit.getClass().getSimpleName()).getAnimalProperties(),
                        unit.getPosition());
                location.add(childUnit);
                partnerForReproduction.setReadyToReproduction(false);
            }
        } finally {
            location.unlockLocation();
        }
    }

    /**
     * Существо перемещается
     *
     * @param unit  животное
     * @param parkMap Карта парка
     */
    public void move(Unit unit, ParkMap parkMap) {
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

        if (isEndOfMapByColumn(nextPosition.column()) || isEndOfMapByRow(nextPosition.row())) {
            return currentLocation;
        } else {
            return parkMap.getLocation(nextPosition.row(), nextPosition.column());
        }
    }

    /**
     * Проверяет, что столбец следующей локации при перемещении на карте парка не выходит за пределы карты
     * @param column Столбец для проверки
     * @return Возможность перемещения
     */
    private boolean isEndOfMapByColumn(int column) {
        return column >= settings.get(SettingsType.MAP_COLUMNS) || column < 0;
    }

    /**
     * Проверяет, что строка следующей локации при перемещении на карте парка не выходит за пределы карты
     * @param row Строка для проверки
     * @return Возможность перемещения
     */
    private boolean isEndOfMapByRow(int row) {
        return row >= settings.get(SettingsType.MAP_ROWS) || row < 0;
    }

    /**
     * Перечисление направлений движения
     */
    enum Direction {
        DOWN,
        UP,
        LEFT,
        RIGHT
    }
}
