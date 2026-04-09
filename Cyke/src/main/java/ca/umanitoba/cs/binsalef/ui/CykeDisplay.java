package ca.umanitoba.cs.binsalef.ui;

import ca.umanitoba.cs.binsalef.logic.CykeService;
import ca.umanitoba.cs.binsalef.model.Profile;
import ca.umanitoba.cs.binsalef.ui.output.ConsoleColors;
import ca.umanitoba.cs.binsalef.ui.output.ProfilePrinter;

import java.util.Scanner;

public class CykeDisplay {

    private CykeService cykeService;
    private Scanner scanner;
    private ProfileUI profileUI;
    private FeedUI feedUI;
    private ActivityUI activityUI;
    private RouteUI routeUI;
    private boolean dataLoaded;

    public CykeDisplay(CykeService cykeService, Scanner scanner, boolean dataLoaded) {
        this.cykeService = cykeService;
        this.scanner = scanner;
        this.profileUI = new ProfileUI(cykeService, scanner);
        this.feedUI = new FeedUI(cykeService, scanner);
        this.activityUI = new ActivityUI(cykeService, scanner);
        this.routeUI = new RouteUI(cykeService, scanner);
        this.dataLoaded = dataLoaded;
    }

    public void run() {
        int choice;
        boolean running = true;
        Profile currentProfile;
        System.out.println(ConsoleColors.CYAN + "Welcome to Cyke!" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.CYAN + "Cyke! is a Exercise Tracker built for Cycling." + ConsoleColors.RESET);

        if (dataLoaded) {
            System.out.println(ConsoleColors.GREEN + "Cyke! data loaded successfully." + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.YELLOW + "No saved data found. Starting a new Cyke! system...." + ConsoleColors.RESET);
        }

        while (running) {
            currentProfile = cykeService.getCurrentProfile();

            ProfilePrinter.printCurrentProfileHeader(currentProfile);

            showMainMenu();
            choice = InputHelper.readInt(scanner, "Choose an integer option: ");

            if (choice == 5) {
                running = false;
            } else {
                handleMainMenuChoice(choice);
            }
        }

        System.out.println(ConsoleColors.GREEN + "Goodbye :(" + ConsoleColors.RESET);
    }

    private void showMainMenu() {
        System.out.println(ConsoleColors.CYAN + "\n=== Cyke Main Menu ===" + ConsoleColors.RESET);
        System.out.println("1. Profile Management");
        System.out.println("2. Activity Management");
        System.out.println("3. Feed and Follow Profiles");
        System.out.println("4. Find a Route");
        System.out.println("5. Exit");
    }

    private void handleMainMenuChoice(int choice) {
        if (choice == 1) {
            profileUI.run();
        } else if (choice == 2) {
            activityUI.run();
        } else if (choice == 3) {
            feedUI.run();
        } else if (choice == 4) {
            routeUI.run();
        } else {
            System.out.println(ConsoleColors.YELLOW + "Invalid option. Please try again" + ConsoleColors.RESET);
        }
    }

}
