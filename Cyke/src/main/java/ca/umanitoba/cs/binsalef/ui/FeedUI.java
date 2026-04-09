package ca.umanitoba.cs.binsalef.ui;

import ca.umanitoba.cs.binsalef.logic.CykeService;
import ca.umanitoba.cs.binsalef.logic.FeedItem;
import ca.umanitoba.cs.binsalef.logic.exceptions.EmptyProfileException;
import ca.umanitoba.cs.binsalef.logic.exceptions.NoCurrentProfileException;
import ca.umanitoba.cs.binsalef.model.ExerciseSession;
import ca.umanitoba.cs.binsalef.model.Profile;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidFollowException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidNameException;
import ca.umanitoba.cs.binsalef.model.exceptions.ProfileNotFoundException;
import ca.umanitoba.cs.binsalef.persistence.exceptions.PersistenceException;
import ca.umanitoba.cs.binsalef.ui.output.ActivityPrinter;
import ca.umanitoba.cs.binsalef.ui.output.ConsoleColors;
import ca.umanitoba.cs.binsalef.ui.output.ProfilePrinter;

import java.util.List;
import java.util.Scanner;

public class FeedUI {
    private CykeService cykeService;
    private Scanner scanner;

    public FeedUI(CykeService cykeService, Scanner scanner) {
        this.cykeService = cykeService;
        this.scanner = scanner;
    }

    public void run() {
        boolean running = true;
        int choice;

        while (running) {
            showFeedMenu();
            choice = InputHelper.readInt(scanner, "Choose an integer option: ");

            if (choice == 5) {
                running = false;
            } else {
                handleFeedMenuChoice(choice);
            }
        }
    }

    private void showFeedMenu() {
        ProfilePrinter.printCurrentProfileHeader(cykeService.getCurrentProfile());

        System.out.println(ConsoleColors.CYAN + "\n=== Feed Menu ===" + ConsoleColors.RESET);
        System.out.println("1. View Feed");
        System.out.println("2. Follow Profile");
        System.out.println("3. Unfollow Profile");
        System.out.println("4. View My Activities");
        System.out.println("5. Back");
    }

    private void handleFeedMenuChoice(int choice) {
        if (choice == 1) {
            viewFeedFlow();
        } else if (choice == 2) {
            followProfileFlow();
        } else if (choice == 3) {
            unfollowProfileFlow();
        } else if (choice == 4) {
            viewMyActivitiesFlow();
        } else {
            System.out.println(ConsoleColors.YELLOW + "Invalid option. Please try again." + ConsoleColors.RESET);
        }
    }

    private void viewFeedFlow() {
        List<FeedItem> feedItems;
        FeedItem currentItem;
        ExerciseSession currentSession;
        int index;


        try {
            feedItems = cykeService.getFeed();

            if (feedItems == null || feedItems.isEmpty()) {
                System.out.println(ConsoleColors.YELLOW + "Feed is empty." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.CYAN + "Feed " + ConsoleColors.RESET);
                index = 0;

                while (index < feedItems.size()) {
                    currentItem = feedItems.get(index);
                    currentSession = currentItem.getSession();
                    System.out.println("Date: " + currentSession.getSessionDate());
                    System.out.println("Username: " + currentItem.getOwner().getUsername());
                    System.out.println("Session ID: " + currentSession.getSessionID());
                    System.out.println("Minutes: " + currentSession.getMinutes());
                    System.out.println("Calories: " + currentSession.getCalories());
                    ActivityPrinter.printRoute(currentSession);
                    System.out.println();

                    index++;
                }
            }
        } catch (NoCurrentProfileException e) {
            System.out.println(ConsoleColors.RED + "No profile is currently selected. Select a profile before viewing the feed." + ConsoleColors.RESET);
        } catch (EmptyProfileException e) {
            System.out.println(ConsoleColors.RED + "The feed could not be displayed." + ConsoleColors.RESET);
        }
    }

    private void followProfileFlow() {
        ProfilePrinter.printProfiles(cykeService.getProfiles(), cykeService.getCurrentProfile());
        String username = InputHelper.readLine(scanner, "Enter the profile name you want to follow: ");
        
        try {
            cykeService.followProfile(username);
            System.out.println(ConsoleColors.GREEN + username + " has been followed successfully!" + ConsoleColors.RESET);
        } catch (InvalidNameException e) {
            System.out.println(ConsoleColors.RED + "That profile name is invalid. Enter a non-empty profile name." + ConsoleColors.RESET);
        } catch (ProfileNotFoundException e) {
            System.out.println(ConsoleColors.RED + "That profile does not exist. Enter the name of an existing profile." + ConsoleColors.RESET);
        } catch (InvalidFollowException e) {
            System.out.println(ConsoleColors.RED + "That profile cannot be followed. You may already be following it or it may be your own profile." + ConsoleColors.RESET);
        } catch (NoCurrentProfileException e) {
            System.out.println(ConsoleColors.RED + "No profile is currently selected. Select a profile before following someone." + ConsoleColors.RESET);
        } catch (PersistenceException e) {
            System.out.println(ConsoleColors.RED + "Could not save your changes." + ConsoleColors.RESET);
        }
    }

    private void unfollowProfileFlow() {
        ProfilePrinter.printProfiles(cykeService.getProfiles(), cykeService.getCurrentProfile());
        String username = InputHelper.readLine(scanner, "Enter the profile name you want to unfollow: ");

        try {
            cykeService.unfollowProfile(username);
            System.out.println(ConsoleColors.GREEN + username + " has been unfollowed successfully!" + ConsoleColors.RESET);
        } catch (InvalidNameException e) {
            System.out.println(ConsoleColors.RED + "That profile name is invalid. Enter a non-empty profile name." + ConsoleColors.RESET);
        } catch (ProfileNotFoundException e) {
            System.out.println(ConsoleColors.RED + "That profile does not exist. Enter the name of an existing profile." + ConsoleColors.RESET);
        } catch (InvalidFollowException e) {
            System.out.println(ConsoleColors.RED + "That profile cannot be unfollowed. Make sure you are currently following that profile." + ConsoleColors.RESET);
        } catch (NoCurrentProfileException e) {
            System.out.println(ConsoleColors.RED + "No profile is currently selected. Select a profile before unfollowing someone." + ConsoleColors.RESET);
        }
    }

    private void viewMyActivitiesFlow() {
        Profile currentProfile = cykeService.getCurrentProfile();

        if (currentProfile == null) {
            System.out.println(ConsoleColors.RED + "No profile is currently selected." + ConsoleColors.RESET);
        } else {
            ActivityPrinter.printAllActivities(currentProfile);
        }
    }

}