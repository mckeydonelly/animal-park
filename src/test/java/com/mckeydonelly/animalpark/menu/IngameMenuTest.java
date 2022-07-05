package com.mckeydonelly.animalpark.menu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IngameMenuTest {

    @Test
    void should_get_ingame_menu() {
        IngameMenu ingameMenu = new IngameMenu();
        String menu = ingameMenu.getIngameMenu();
        assertEquals("\np - pause | g - get statistics for a specific cell | q - quit\n", menu);
    }
}