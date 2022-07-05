package com.mckeydonelly.animalpark.activity;

import com.mckeydonelly.animalpark.creature.Creature;
import com.mckeydonelly.animalpark.map.Location;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/**
 * Contains logic for creature turn
 */
public class CreatureActionProcessor {
    private final ParkMap parkMap;
    private final SimulationSettings settings;
    private final CreatureActions creatureActions;

    public CreatureActionProcessor(ParkMap parkMap,
                                   SimulationSettings settings,
                                   CreatureActions creatureActions) {
        this.parkMap = parkMap;
        this.settings = settings;
        this.creatureActions = creatureActions;
    }

    /**
     * Make creature turn
     *
     * @param creature creature
     */
    public void doTurn(Creature creature) {
        if(creature.isDead()) {
            return;
        }

        List<Consumer<Creature>> availableActions = creatureActions.getAvailableActions(creature);
        if(availableActions.isEmpty()) {
            return;
        }

        int actionType = ThreadLocalRandom.current().nextInt(availableActions.size());
        availableActions.get(actionType).accept(creature);

        reducePower(creature);
    }

    /**
     * Reduce creature power after his turn
     */
    private void reducePower(Creature creature) {
        Location location = parkMap.getLocation(creature.getPosition().row(), creature.getPosition().column());

        creature.setWeightEaten(creature.getWeightEaten() - (creature.getWeightEatToFill() / settings.get(SettingsType.TURNS_FOR_DIE_BY_MAX_FILL)));
        if (creature.getWeightEaten() <= 0) {
            location.lockLocation();
            try {
                creature.die();
                location.remove(creature);
            } finally {
                location.unlockLocation();
            }
        }
    }
}
