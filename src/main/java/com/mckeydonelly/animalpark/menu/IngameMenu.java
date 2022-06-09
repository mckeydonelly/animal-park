package com.mckeydonelly.animalpark.menu;

/**
 * Внутриигровое меню.
 */
public class IngameMenu {
    public String getIngameMenu() {
        StringBuilder menu = new StringBuilder();
        menu.append("p - pause | ");
        menu.append("g - get statistics for a specific cell | ");
        menu.append("q - quit\n");

        return menu.toString();
    }
}
