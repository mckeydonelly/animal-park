package com.mckeydonelly.animalpark.utils.console;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

import java.io.IOException;

/**
 * Хелпер для работы с консолью.
 */
public class ConsoleReaderHelper {
    private static final Terminal terminal;
    private static final NonBlockingReader reader;
    private static final LineReader lineReader;

    static {
        try {
            terminal = TerminalBuilder.terminal();
            terminal.enterRawMode();
            reader = terminal.reader();
            lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();
        } catch (IOException e) {
            System.out.println(colorize("Ошибка при инициализации консоли", RED_TEXT(), NONE()));
            throw new RuntimeException(e);
        }
    }

    private ConsoleReaderHelper() {
    }

    public static NonBlockingReader getReader() {
        return reader;
    }

    public static LineReader getLineReader() {
        return lineReader;
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception e) {
            System.out.println(colorize("Ошибка при очистке консоли", RED_TEXT(), NONE()));
            throw new RuntimeException(e);
        }
    }
}
