package com.mckeydonelly.animalpark.menu;

import com.mckeydonelly.animalpark.activity.ActivityProcessor;
import com.mckeydonelly.animalpark.map.Location;
import com.mckeydonelly.animalpark.map.ParkMap;
import com.mckeydonelly.animalpark.utils.console.ConsoleReaderHelper;
import org.jline.reader.LineReader;
import org.jline.utils.NonBlockingReader;
import java.io.IOException;
import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

/**
 * Слушает и обрабатывает ввод пользователя во время симуляции.
 */
public class IngameMenuListener implements Runnable {
    private final NonBlockingReader reader = ConsoleReaderHelper.getReader();
    private final LineReader lineReader = ConsoleReaderHelper.getLineReader();
    private final ActivityProcessor activityProcessor;
    private final ParkMap parkMap;

    public IngameMenuListener(ActivityProcessor activityProcessor, ParkMap parkMap) {
        this.activityProcessor = activityProcessor;
        this.parkMap = parkMap;
    }

    @Override
    public void run() {
        int command = 0;
        while (command != 'p' && command != 'g') {
            try {
                command = reader.read();
                switch (command) {
                    case 'p' -> pause();
                    case 'g' -> getStatisticByCell();
                    case 'q' -> System.exit(0);
                    default -> System.out.print("\r");
                }
            } catch (IOException e) {
                System.out.println(colorize("Ошибка при чтении команды", RED_TEXT(), NONE()));
                throw new RuntimeException(e);
            }
        }
        run();
    }

    /**
     * Ставит симуляцию на паузу.
     */
    private void pause() {
        activityProcessor.pause();
        System.out.println("Simulation is paused... For continue press SPACE...");
        unPause();
    }

    /**
     * Выводит статистику по конкретной клетке.
     */
    private void getStatisticByCell() {
        activityProcessor.pause();

        boolean badParam = true;
        while (badParam) {
            System.out.print("Please enter location coordinates \"row,column\" (example: \"10,10\"): ");
            String paramValue = lineReader.readLine();

            if (paramValue.matches("(\\d+,\\d+)")) {
                String[] position = paramValue.split(",");
                Location location = parkMap.getLocation(Integer.parseInt(position[0]), Integer.parseInt(position[1]));
                if (location == null) {
                    System.out.println(colorize("Incorrect coordinates.", RED_TEXT(), NONE()));
                    continue;
                }
                System.out.println(location);
                badParam = false;
            } else {
                System.out.println(colorize("Please enter correct coordinates. Example: \"10,10\"", RED_TEXT(), NONE()));
            }
        }

        System.out.println("For continue press SPACE...");
        unPause();
    }

    /**
     * Возобновляет симуляцию.
     */
    private void unPause() {
        int command = 0;
        while (command != ' ') {
            try {
                command = reader.read();
                if (command == ' ') {
                    activityProcessor.unPause();
                }
            } catch (IOException e) {
                System.out.println(colorize("Ошибка при чтении команды", RED_TEXT(), NONE()));
                throw new RuntimeException(e);
            }
        }
    }
}
