package ca.umanitoba.cs.binsalef.ui;

import ca.umanitoba.cs.binsalef.model.Coordinate;
import ca.umanitoba.cs.binsalef.ui.output.ConsoleColors;

import java.util.Scanner;

public class InputHelper {

    private InputHelper() {
    }

    public static int readInt(Scanner scanner, String prompt) {
        int value = 0;
        boolean valid = false;
        String input;

        while (!valid) {
            System.out.print(ConsoleColors.CYAN + prompt + ConsoleColors.RESET);
            input = scanner.nextLine();

            try {
                value = Integer.parseInt(input.trim());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.RED + "Invalid input. Please enter a valid integer.");
            }
        }
        return value;
    }

    public static String readLine(Scanner scanner, String prompt) {
        String input;
        System.out.print(ConsoleColors.CYAN + prompt);
        input = scanner.nextLine();

        return input;
    }

    public static Coordinate readCoordinate(Scanner scanner, String label) {
        int x;
        int y;


        System.out.println(ConsoleColors.CYAN + label + ConsoleColors.RESET);

        while (true) {
            x = readInt(scanner, "Enter x: ");
            y = readInt(scanner, "Enter y: ");

            if (x < 0 || y < 0) {
                System.out.println(ConsoleColors.RED + "Coordinates cannot be negative. Please enter 0 or greater." + ConsoleColors.RESET);
            } else {
                return new Coordinate(x, y);
            }
        }

    }

    public static boolean readYesNo(Scanner scanner, String prompt) {
        String input;
        boolean valid = false;
        boolean result = false;

        while (!valid) {
            System.out.print(ConsoleColors.CYAN + prompt + " (yes/no) or (y/n) : " + ConsoleColors.RESET);
            input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("yes") || input.equals("y")) {
                result = true;
                valid = true;
            } else if (input.equals("no") || input.equals("n")) {
                valid = true;
            } else {
                System.out.println(ConsoleColors.RED
                        + "Invalid input. Please enter yes/y or no/n."
                        + ConsoleColors.RESET);
            }
        }
        return result;
    }

    public static int readIntAtLeast(Scanner scanner, String prompt, int minimum, String errorMessage) {
        int value;

        value = readInt(scanner, prompt);

        while (value < minimum) {
            System.out.println(ConsoleColors.RED + errorMessage + ConsoleColors.RESET);
            value = readInt(scanner, prompt);
        }

        return value;
    }
}
