package com.mckeydonelly.animalpark;

import com.mckeydonelly.animalpark.park.ParkService;

/**
 * The entry point to the application.
 */
public class App {
    public static void main(String[] args) {
        new ParkService().start();
    }
}
