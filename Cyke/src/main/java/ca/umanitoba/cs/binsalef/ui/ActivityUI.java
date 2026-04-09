package ca.umanitoba.cs.binsalef.ui;

import ca.umanitoba.cs.binsalef.logic.CykeService;
import ca.umanitoba.cs.binsalef.logic.exceptions.NoCurrentProfileException;
import ca.umanitoba.cs.binsalef.logic.exceptions.NoRouteException;
import ca.umanitoba.cs.binsalef.model.Coordinate;
import ca.umanitoba.cs.binsalef.model.ExerciseSession;
import ca.umanitoba.cs.binsalef.model.Route;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidCoordinateException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidObstacleException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidRouteException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidSessionException;
import ca.umanitoba.cs.binsalef.persistence.exceptions.PersistenceException;
import ca.umanitoba.cs.binsalef.ui.output.ConsoleColors;
import ca.umanitoba.cs.binsalef.ui.output.MapPrinter;
import ca.umanitoba.cs.binsalef.ui.output.ProfilePrinter;

import java.util.Scanner;

public class ActivityUI {
    private CykeService cykeService;
    private Scanner scanner;

    public ActivityUI(CykeService cykeService, Scanner scanner) {
        this.cykeService = cykeService;
        this.scanner = scanner;
    }

    public void run() {
        boolean running;
        int choice;

        running = true;

        while (running) {
            showActivityMenu();
            choice = InputHelper.readInt(scanner, "Choose an integer option: ");
            if (choice == 6) {
                running = false;
            } else {
                handleActivityMenuChoice(choice);
            }
        }
    }

    private void showActivityMenu() {
        ProfilePrinter.printCurrentProfileHeader(cykeService.getCurrentProfile());
        
        System.out.println(ConsoleColors.CYAN + "\n=== Activity Management ===" + ConsoleColors.RESET);
        System.out.println("1. Add Manual Activity");
        System.out.println("2. Duplicate Route");
        System.out.println("3. Add Obstacle");
        System.out.println("4. Remove Obstacles");
        System.out.println("5. Show Obstacles");
        System.out.println("6. Back");
    }

    private void handleActivityMenuChoice(int choice) {
        if (choice == 1) {
            addManualActivityFlow();
        } else if (choice == 2) {
            duplicateRouteFlow();
        } else if (choice == 3) {
            addObstacleFlow();
        } else if (choice == 4) {
            removeObstacleFlow();
        } else if (choice == 5) {
            showObstaclesFlow();
        } else {
            System.out.println(ConsoleColors.YELLOW + "Invalid option. Please try again." + ConsoleColors.RESET);
        }
    }

    private void addManualActivityFlow() {
        Route route;
        ExerciseSession.ExerciseSessionBuilder builder;
        ExerciseSession session;

        route = buildRouteFromInput();

        builder = new ExerciseSession.ExerciseSessionBuilder();

        getSessionID(builder);
        getRoute(builder, route);
        getMinutes(builder);
        getCalories(builder);

        session = builder.build();

        try {
            cykeService.addActivity(session);
            System.out.println(ConsoleColors.GREEN + "Activity added successfully!" + ConsoleColors.RESET);
        } catch (NoCurrentProfileException | InvalidSessionException e) {
            System.out.println(ConsoleColors.RED + "No profile is currently selected. Select a profile before adding an activity." + ConsoleColors.RESET);
        } catch (PersistenceException e) {
            System.out.println(ConsoleColors.RED + "Could not save your changes." + ConsoleColors.RESET);
        }
    }

    private void duplicateRouteFlow() {
        int oldSessionID;
        Route copiedRoute;
        ExerciseSession.ExerciseSessionBuilder builder;
        ExerciseSession session;

        oldSessionID = InputHelper.readIntAtLeast(scanner, "Enter existing session ID: ", 1, "Session ID must be at least 1. Please enter a positive session ID."
        );

        try {
            copiedRoute = cykeService.getCopiedRoute(oldSessionID);

            builder = new ExerciseSession.ExerciseSessionBuilder();

            getSessionID(builder);
            getRoute(builder, copiedRoute);
            getMinutes(builder);
            getCalories(builder);

            session = builder.build();

            cykeService.addActivity(session);

            System.out.println(ConsoleColors.GREEN + "Route duplicated successfully!" + ConsoleColors.RESET);
        } catch (InvalidSessionException e) {
            System.out.println(ConsoleColors.RED + "The route could not be duplicated. Enter a valid existing session ID." + ConsoleColors.RESET);
        } catch (NoCurrentProfileException e) {
            System.out.println(ConsoleColors.RED + "No profile is currently selected. Select a profile before duplicating a route." + ConsoleColors.RESET);
        } catch (NoRouteException e) {
            System.out.println(ConsoleColors.RED + "The selected session does not contain a valid route to duplicate." + ConsoleColors.RESET);
        } catch (InvalidCoordinateException e) {
            System.out.println(ConsoleColors.RED + "The duplicated route contains an invalid coordinate." + ConsoleColors.RESET);
        } catch (InvalidRouteException e) {
            System.out.println(ConsoleColors.RED + "The duplicated route is invalid. Routes must remain continuous." + ConsoleColors.RESET);
        } catch (PersistenceException e) {
            System.out.println(ConsoleColors.RED + "Could not save your changes." + ConsoleColors.RESET);
        }
    }

    private void addObstacleFlow() {
        Coordinate coordinate;

        coordinate = InputHelper.readCoordinate(scanner, "Enter obstacle coordinate:");

        try {
            cykeService.addObstacle(coordinate);
            System.out.println(ConsoleColors.GREEN + "Obstacle added successfully!" + ConsoleColors.RESET);
        } catch (InvalidCoordinateException e) {
            System.out.println(ConsoleColors.RED + "That coordinate is invalid. Enter a coordinate within the world map bounds." + ConsoleColors.RESET);
        } catch (InvalidObstacleException e) {
            System.out.println(ConsoleColors.RED + "That obstacle cannot be added. Choose a coordinate that does not already contain an obstacle." + ConsoleColors.RESET);
        }
    }

    private void removeObstacleFlow() {
        MapPrinter.showObstacles(cykeService.getWorldMap());
        int index = InputHelper.readInt(scanner, "Enter obstacle index to remove: ");

        try {
            cykeService.removeObstacle(index);
            System.out.println(ConsoleColors.GREEN + "Obstacle removed successfully!" + ConsoleColors.RESET);
        } catch (InvalidObstacleException e) {
            System.out.println(ConsoleColors.RED + "That obstacle index is invalid. Enter an index shown in the obstacle list." + ConsoleColors.RESET);
        }
    }

    private void showObstaclesFlow() {
        MapPrinter.showObstacles(cykeService.getWorldMap());
    }

    private Route buildRouteFromInput() {
        int index;
        int numberOfCoordinates;
        Coordinate coordinate;
        Route route;

        numberOfCoordinates = InputHelper.readIntAtLeast(scanner, "Enter number of coordinates: ", 1, "A route must contain at least one coordinate. Enter a positive number."
        );

        route = new Route();
        index = 0;

        while (index < numberOfCoordinates) {
            try {
                coordinate = InputHelper.readCoordinate(scanner, "Enter coordinate number " + (index + 1) + ":");
                route.addCoordinate(coordinate);
                index++;
            } catch (InvalidRouteException e) {
                System.out.println(ConsoleColors.RED + "That route step is invalid. Enter coordinates that move one step horizontally or vertically." + ConsoleColors.RESET);
            } catch (InvalidCoordinateException e) {
                System.out.println(ConsoleColors.RED + "That coordinate is invalid. Enter a valid coordinate." + ConsoleColors.RESET);
            }
        }

        return route;
    }

    private void getSessionID(ExerciseSession.ExerciseSessionBuilder builder) {
        boolean valid;
        int sessionID;

        valid = false;

        while (!valid) {
            sessionID = InputHelper.readInt(scanner, "Enter session ID: ");

            try {
                builder.sessionID(sessionID);
                valid = true;
            } catch (InvalidSessionException e) {
                System.out.println(ConsoleColors.RED + "Session ID must be at least 1." + ConsoleColors.RESET);
            }
        }
    }

    private void getRoute(ExerciseSession.ExerciseSessionBuilder builder, Route route) {
        boolean valid;

        valid = false;

        while (!valid) {
            try {
                builder.route(route);
                valid = true;
            } catch (InvalidRouteException e) {
                System.out.println(ConsoleColors.RED + "The route is invalid." + ConsoleColors.RESET);
            }
        }
    }

    private void getMinutes(ExerciseSession.ExerciseSessionBuilder builder) {
        boolean valid;
        int minutes;

        valid = false;

        while (!valid) {
            minutes = InputHelper.readInt(scanner, "Enter minutes taken: ");

            try {
                builder.minutes(minutes);
                valid = true;
            } catch (InvalidSessionException e) {
                System.out.println(ConsoleColors.RED + "Minutes cannot be negative." + ConsoleColors.RESET);
            }
        }
    }

    private void getCalories(ExerciseSession.ExerciseSessionBuilder builder) {
        boolean valid;
        int calories;

        valid = false;

        while (!valid) {
            calories = InputHelper.readInt(scanner, "Enter calories burnt: ");

            try {
                builder.calories(calories);
                valid = true;
            } catch (InvalidSessionException e) {
                System.out.println(ConsoleColors.RED + "Calories cannot be negative." + ConsoleColors.RESET);
            }
        }
    }
}