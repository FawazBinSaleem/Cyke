package ca.umanitoba.cs.binsalef.ui;

import ca.umanitoba.cs.binsalef.logic.CykeService;
import ca.umanitoba.cs.binsalef.model.Profile;
import ca.umanitoba.cs.binsalef.model.exceptions.DuplicateProfileException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidNameException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidSessionException;
import ca.umanitoba.cs.binsalef.model.exceptions.ProfileNotFoundException;
import ca.umanitoba.cs.binsalef.persistence.exceptions.PersistenceException;
import ca.umanitoba.cs.binsalef.ui.output.ConsoleColors;
import ca.umanitoba.cs.binsalef.ui.output.ProfilePrinter;

import java.util.Scanner;

public class ProfileUI {
    private CykeService cykeService;
    private Scanner scanner;

    public ProfileUI(CykeService cykeService, Scanner scanner) {
        this.cykeService = cykeService;
        this.scanner = scanner;
    }

    public void run() {
        boolean running;
        int choice;

        running = true;

        while (running) {
            showProfileMenu();
            choice = InputHelper.readInt(scanner, "Choose an integer option: ");

            if (choice == 6) {
                running = false;
            } else {
                handleProfileMenuChoice(choice);
            }
        }
    }

    private void showProfileMenu() {
        ProfilePrinter.printCurrentProfileHeader(cykeService.getCurrentProfile());
        System.out.println(ConsoleColors.CYAN + "\n=== Profile Management ===" + ConsoleColors.RESET);
        System.out.println("1. Create Profile");
        System.out.println("2. Select Profile");
        System.out.println("3. Rename Current Profile");
        System.out.println("4. Show Current Profile");
        System.out.println("5. Show All Profiles");
        System.out.println("6. Back");
    }

    private void handleProfileMenuChoice(int choice) {
        if (choice == 1) {
            createProfileFlow();
        } else if (choice == 2) {
            selectProfileFlow();
        } else if (choice == 3) {
            renameProfileFlow();
        } else if (choice == 4) {
            showCurrentProfileFlow();
        } else if (choice == 5) {
            showAllProfilesFlow();
        } else {
            System.out.println(ConsoleColors.YELLOW + "Invalid option. Please try again." + ConsoleColors.RESET);
        }
    }

    private void createProfileFlow() {
        Profile.ProfileBuilder builder = new Profile.ProfileBuilder();
        Profile profile;
        getUsername(builder);

        try {
            profile = builder.build();
            cykeService.createProfile(profile);
            System.out.println(ConsoleColors.GREEN + "Profile created successfully!" + ConsoleColors.RESET);
        } catch (InvalidNameException e) {
            System.out.println(ConsoleColors.RED + "That profile name is invalid." + ConsoleColors.RESET);
        } catch (DuplicateProfileException | InvalidSessionException e) {
            System.out.println(ConsoleColors.RED + "That profile already exists. Enter a different profile name." + ConsoleColors.RESET);
        } catch (PersistenceException e) {
            System.out.println(ConsoleColors.RED + "Could not save your changes." + ConsoleColors.RESET);
        }
    }

    private void selectProfileFlow() {
        Profile currentProfile = cykeService.getCurrentProfile();
        ProfilePrinter.printProfiles(cykeService.getProfiles(), currentProfile);

        String username = InputHelper.readLine(scanner, "Enter the profile name to select: ");

        if (currentProfile != null && currentProfile.getUsername().equals(username.trim())) {
            System.out.println(ConsoleColors.YELLOW + currentProfile.getUsername() + " is already selected as the current profile.." + ConsoleColors.RESET);
        } else {
            try {
                cykeService.selectProfile(username);
                System.out.println(ConsoleColors.GREEN + "Profile selected successfully!" + ConsoleColors.RESET);
            } catch (InvalidNameException e) {
                System.out.println(ConsoleColors.RED + "That profile name is invalid. Enter a non-empty profile name." + ConsoleColors.RESET);
            } catch (ProfileNotFoundException e) {
                System.out.println(ConsoleColors.RED + "That profile was not found. Enter the name of an existing profile." + ConsoleColors.RESET);
            }
        }

    }

    private void renameProfileFlow() {
        String oldUsername;
        String newUsername;
        Profile currentProfile;

        currentProfile = cykeService.getCurrentProfile();

        if (currentProfile == null) {
            System.out.println(ConsoleColors.RED + "No profile is currently selected." + ConsoleColors.RESET);
        } else {
            oldUsername = currentProfile.getUsername();
            newUsername = InputHelper.readLine(scanner, "Enter the new profile name: ");

            try {
                cykeService.renameProfile(oldUsername, newUsername);
                System.out.println(ConsoleColors.GREEN + "Profile renamed successfully to: " + newUsername + ConsoleColors.RESET);
            } catch (InvalidNameException e) {
                System.out.println(ConsoleColors.RED + "That new profile name is invalid. Enter a non-empty profile name." + ConsoleColors.RESET);
            } catch (DuplicateProfileException e) {
                System.out.println(ConsoleColors.RED + "That profile name is already in use. Enter a different profile name." + ConsoleColors.RESET);
            } catch (ProfileNotFoundException e) {
                System.out.println(ConsoleColors.RED + "The current profile could not be found." + ConsoleColors.RESET);
            } catch (PersistenceException e) {
                System.out.println(ConsoleColors.RED + "Could not save your changes." + ConsoleColors.RESET);
            }
        }
    }

    private void showCurrentProfileFlow() {
        ProfilePrinter.printProfile(cykeService.getCurrentProfile());
    }

    private void showAllProfilesFlow() {
        ProfilePrinter.printProfiles(cykeService.getProfiles(), cykeService.getCurrentProfile());
    }

    private void getUsername(Profile.ProfileBuilder builder) {
        boolean valid = false;
        String username;

        while (!valid) {
            username = InputHelper.readLine(scanner, "Enter a profile name: ");

            try {
                builder.username(username);
                valid = true;
            } catch (InvalidNameException e) {
                System.out.println(ConsoleColors.RED + "Please enter a valid profile name." + ConsoleColors.RESET);
            }
        }
    }
}