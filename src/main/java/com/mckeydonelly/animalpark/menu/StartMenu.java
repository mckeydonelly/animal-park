package com.mckeydonelly.animalpark.menu;

import com.mckeydonelly.animalpark.utils.console.ConsoleReaderHelper;
import com.mckeydonelly.animalpark.settings.SettingsService;
import com.mckeydonelly.animalpark.settings.SettingsType;
import com.mckeydonelly.animalpark.settings.SimulationSettings;
import org.jline.reader.LineReader;
import org.jline.utils.NonBlockingReader;
import java.io.IOException;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

/**
 * Отображает стартовое меню и принимает ввод от пользователя.
 */
public class StartMenu {
    private final NonBlockingReader reader = ConsoleReaderHelper.getReader();
    private final LineReader lineReader = ConsoleReaderHelper.getLineReader();

    public void start() {
        StringBuilder menu = new StringBuilder();
        menu.append("s - start with default parameters\n");
        menu.append("c - manual configuration\n");
        menu.append("q - quit");
        System.out.println(menu);

        int command = 0;

        while (command != 's' && command != 'c') {
            try {
                command = reader.read();
                switch (command) {
                    case 's' -> SettingsService.setDefaultSettings();
                    case 'c' -> manualConfiguration(lineReader);
                    case 'q' -> System.exit(0);
                    default -> System.out.print("\r");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        printStartParameters();
        ConsoleReaderHelper.clearConsole();
    }

    private void manualConfiguration(LineReader lineReader) {
        System.out.println("Welcome to animal park simulation!");
        System.out.println("For starting the simulation please enter configuration parameters: ");

        for (SettingsType settingsType : SettingsType.values()) {
            getInputParameter(lineReader, settingsType);
        }
    }

    private void getInputParameter(LineReader lineReader, SettingsType type) {
        boolean badParam = true;
        System.out.print("Enter " + type.getDescription() + ": ");
        while (badParam) {
            try {
                int paramValue = Integer.parseInt(lineReader.readLine());
                SimulationSettings.add(type, paramValue);
                badParam = false;
            } catch (NumberFormatException e) {
                System.out.println(colorize("Please enter correct value.", RED_TEXT(), NONE()));
            }
        }
    }

    private void printStartParameters() {
        System.out.println("\nStart simulation with parameters:");
        for (SettingsType settingsType : SettingsType.values()) {
            System.out.println("Parameter: " + settingsType.getDescription() + " - " + SimulationSettings.get(settingsType));
        }
        System.out.println("\nFor continue press SPACE...");
        waitForSpace();
    }


    private void waitForSpace() {
        int command;
        while (true) {
            try {
                command = reader.read();
                if (command == ' ') {
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}