package com.mckeydonelly.animalpark.activity;

import com.mckeydonelly.animalpark.unit.Unit;
import com.mckeydonelly.animalpark.map.Location;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/**
 * Contains logic for unit turn
 */
public class UnitActionsProcessor {
    private final ParkMap parkMap;
    private final SimulationSettings settings;
    private final UnitActions unitActions;

    public UnitActionsProcessor(ParkMap parkMap,
                                SimulationSettings settings,
                                UnitActions unitActions) {
        this.parkMap = parkMap;
        this.settings = settings;
        this.unitActions = unitActions;
    }

    /**
     * Make unit turn
     *
     * @param unit unit
     */
    public void doTurn(Unit unit) {
        if(unit.isDead()) {
            return;
        }

        List<Consumer<Unit>> availableActions = unitActions.getAvailableActions(unit);
        if(availableActions.isEmpty()) {
            return;
        }

        int actionType = ThreadLocalRandom.current().nextInt(availableActions.size());
        availableActions.get(actionType).accept(unit);

        reducePower(unit);
    }

    /**
     * Reduce unit power after his turn
     */
    private void reducePower(Unit unit) {
        Location location = parkMap.getLocation(unit.getPosition().row(), unit.getPosition().column());

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
}
