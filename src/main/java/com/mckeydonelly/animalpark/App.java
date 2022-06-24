package com.mckeydonelly.animalpark;

import com.mckeydonelly.animalpark.park.ParkService;

/**
 * Основной класс приложения. Точка входа в приложение.
*/
public class App {
    public static void main(String[] args) {
        new ParkService().start();
    }
}
