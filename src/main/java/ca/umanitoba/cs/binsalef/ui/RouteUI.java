package ca.umanitoba.cs.binsalef.ui;

import ca.umanitoba.cs.binsalef.logic.CykeService;
import ca.umanitoba.cs.binsalef.logic.exceptions.EmptyProfileException;
import ca.umanitoba.cs.binsalef.logic.exceptions.NoCurrentProfileException;
import ca.umanitoba.cs.binsalef.logic.exceptions.PathNotFoundException;
import ca.umanitoba.cs.binsalef.model.Coordinate;
import ca.umanitoba.cs.binsalef.model.ExerciseSession;
import ca.umanitoba.cs.binsalef.model.Route;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidCoordinateException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidRouteException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidSessionException;
import ca.umanitoba.cs.binsalef.persistence.exceptions.PersistenceException;
import ca.umanitoba.cs.binsalef.ui.output.ConsoleColors;
import ca.umanitoba.cs.binsalef.ui.output.ProfilePrinter;

import java.util.List;
import java.util.Scanner;

public class RouteUI {
    private CykeService cykeService;
    private Scanner scanner;

    public RouteUI(CykeService cykeService, Scanner scanner) {
        this.cykeService = cykeService;
        this.scanner = scanner;
    }

    public void run() {
        boolean running;
        int choice;

        running = true;

        while (running) {
            showRouteMenu();
            choice = InputHelper.readInt(scanner, "Choose an integer option: ");

            if (choice == 3) {
                running = false;
            } else {
                handleRouteMenuChoice(choice);
            }
        }
    }

    private void showRouteMenu() {
        ProfilePrinter.printCurrentProfileHeader(cykeService.getCurrentProfile());
        
        System.out.println(ConsoleColors.CYAN + "\n=== Route Finder ===" + ConsoleColors.RESET);
        System.out.println("1. Find Route Using My Routes");
        System.out.println("2. Find Route Using Feed Routes");
        System.out.println("3. Back");
    }

    private void handleRouteMenuChoice(int choice) {
        if (choice == 1) {
            findUsingMyRoutesFlow();
        } else if (choice == 2) {
            findUsingFeedRoutesFlow();
        } else {
            System.out.println(ConsoleColors.YELLOW + "Invalid option. Please try again." + ConsoleColors.RESET);
        }
    }

    private void findUsingMyRoutesFlow() {
        Route foundRoute;
        Coordinate start = InputHelper.readCoordinate(scanner, "Enter the start coordinate: ");
        Coordinate end = InputHelper.readCoordinate(scanner, "Enter the end coordinate:");

        try {
            foundRoute = cykeService.findRouteUsingMyRoutes(start, end);
            System.out.println(ConsoleColors.GREEN + "Route found successfully!" + ConsoleColors.RESET);
            printRouteCoordinates(foundRoute);
            promptToSaveRoute(foundRoute);
        } catch (NoCurrentProfileException e) {
            System.out.println(ConsoleColors.RED + "No profile is currently selected. Select a profile before finding a route." + ConsoleColors.RESET);
        } catch (EmptyProfileException e) {
            System.out.println(ConsoleColors.RED + "There are no activities available to build a route from." + ConsoleColors.RESET);
        } catch (PathNotFoundException | InvalidCoordinateException | InvalidRouteException e) {
            System.out.println(ConsoleColors.RED + "No route could be found between those coordinates using your saved routes." + ConsoleColors.RESET);
        }

    }

    private void findUsingFeedRoutesFlow() {
        Route foundRoute;
        Coordinate start = InputHelper.readCoordinate(scanner, "Enter the start coordinate: ");
        Coordinate end = InputHelper.readCoordinate(scanner, "Enter the end coordinate:");

        try {
            foundRoute = cykeService.findRouteUsingFeedRoutes(start, end);
            System.out.println(ConsoleColors.GREEN + "Route found successfully!" + ConsoleColors.RESET);
            printRouteCoordinates(foundRoute);
            promptToSaveRoute(foundRoute);
        } catch (NoCurrentProfileException e) {
            System.out.println(ConsoleColors.RED + "No profile is currently selected. Select a profile before finding a route." + ConsoleColors.RESET);
        } catch (EmptyProfileException e) {
            System.out.println(ConsoleColors.RED + "There are no activities available to build a route from." + ConsoleColors.RESET);
        } catch (PathNotFoundException | InvalidCoordinateException | InvalidRouteException e) {
            System.out.println(ConsoleColors.RED + "No route could be found between those coordinates using your feed routes." + ConsoleColors.RESET);
        }

    }

    private void promptToSaveRoute(Route route) {
        boolean saveRoute;
        ExerciseSession.ExerciseSessionBuilder builder;
        ExerciseSession session;

        saveRoute = InputHelper.readYesNo(scanner, "Would you like to save this route as an activity?");

        if (saveRoute) {
            builder = new ExerciseSession.ExerciseSessionBuilder();

            getSessionID(builder);
            getRoute(builder, route);
            getMinutes(builder);
            getCalories(builder);

            session = builder.build();

            try {
                cykeService.saveFoundRouteAsActivity(session);
                System.out.println(ConsoleColors.GREEN + "Route saved as an activity successfully!" + ConsoleColors.RESET);
            } catch (NoCurrentProfileException | InvalidSessionException e) {
                System.out.println(ConsoleColors.RED + "No profile is currently selected. Select a profile before saving a route." + ConsoleColors.RESET);
            } catch (PersistenceException e) {
                System.out.println(ConsoleColors.RED + "Could not save your changes." + ConsoleColors.RESET);
            }
        }
    }

    private void printRouteCoordinates(Route route) {
        List<Coordinate> coordinates;
        int index;

        System.out.println(ConsoleColors.CYAN + "Route Coordinates:" + ConsoleColors.RESET);

        if (route == null || route.getCoordinates().isEmpty()) {
            System.out.println("No coordinates found.");
        } else {
            coordinates = route.getCoordinates();
            index = 0;


            while (index < coordinates.size()) {
                System.out.println("(" + coordinates.get(index).getX() + ", " + coordinates.get(index).getY() + ")");
                index++;
            }
        }
    }

    private void getSessionID(ExerciseSession.ExerciseSessionBuilder builder) {
        boolean valid = false;
        int sessionID;

        while (!valid) {
            sessionID = InputHelper.readInt(scanner, "Enter Session ID: ");

            try {
                builder.sessionID(sessionID);
                valid = true;
            } catch (InvalidSessionException e) {
                System.out.println(ConsoleColors.RED + "Session ID must be atleast 1." + ConsoleColors.RESET);
            }
        }
    }

    private void getRoute(ExerciseSession.ExerciseSessionBuilder builder, Route route) {
        boolean valid = false;

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
        boolean valid = false;
        int minutes;


        while (!valid) {
            minutes = InputHelper.readInt(scanner, "Enter minutes: ");

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
            calories = InputHelper.readInt(scanner, "Enter calories: ");

            try {
                builder.calories(calories);
                valid = true;
            } catch (InvalidSessionException e) {
                System.out.println(ConsoleColors.RED + "Calories cannot be negative." + ConsoleColors.RESET);
            }
        }
    }


}
