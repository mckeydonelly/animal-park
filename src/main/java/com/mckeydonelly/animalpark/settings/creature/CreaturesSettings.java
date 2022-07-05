package com.mckeydonelly.animalpark.settings.creature;

import java.util.Map;

/**
 * POJO class for reading creatures settings from YAML file
 */
public class CreaturesSettings {
    private Map<String, Creature> creatures;

    public Map<String, Creature> getCreatures() {
        return creatures;
    }
}
