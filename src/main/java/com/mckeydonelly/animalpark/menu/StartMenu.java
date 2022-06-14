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
    private final SettingsService settingsService;

    public StartMenu(SettingsService settingsService) {
        this.settingsService = settingsService;
    }


    public SimulationSettings start() {
        String menu = "\ns - start with default parameters\nc - manual configuration\nq - quit";
        System.out.println(menu);

        int command = 0;

        SimulationSettings settings = settingsService.getDefaultSettings();

        while (command != 's' && command != 'c') {
            try {
                command = reader.read();
                switch (command) {
                    case 'c' -> manualConfiguration(settings, lineReader);
                    case 'q' -> System.exit(0);
                    default -> System.out.print("\r");
                }
            } catch (IOException e) {
                System.out.println(colorize("Ошибка при чтении команды", RED_TEXT(), NONE()));
                throw new RuntimeException(e);
            }
        }

        printStartParameters(settings);
        ConsoleReaderHelper.clearConsole();

        return settings;
    }

    private void manualConfiguration(SimulationSettings settings, LineReader lineReader) {
        System.out.println("\nFor starting the simulation please enter configuration parameters (press ENTER for default settings): ");

        for (SettingsType settingsType : SettingsType.values()) {
            getInputParameter(lineReader, settings, settingsType);
        }
    }

    private void getInputParameter(LineReader lineReader, SimulationSettings settings, SettingsType type) {
        boolean badParam = true;
        System.out.print("Enter " + type.getDescription() + ": ");
        while (badParam) {
            try {
                String input = lineReader.readLine();
                if("".equals(input)) {
                    break;
                }
                int paramValue = Integer.parseInt(input);
                settings.add(type, paramValue);
                badParam = false;
            } catch (NumberFormatException e) {
                System.out.println(colorize("Please enter correct value.", RED_TEXT(), NONE()));
            }
        }
    }

    private void printStartParameters(SimulationSettings settings) {
        System.out.println("\nStart simulation with parameters:");
        for (SettingsType settingsType : SettingsType.values()) {
            System.out.println("Parameter: " + settingsType.getDescription() + " - " + settings.get(settingsType));
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
                System.out.println(colorize("Ошибка при чтении команды", RED_TEXT(), NONE()));
                throw new RuntimeException(e);
            }
        }
    }
}
