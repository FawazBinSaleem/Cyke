package ca.umanitoba.cs.binsalef.ui.output;

import ca.umanitoba.cs.binsalef.model.Profile;


import java.util.List;

public class ProfilePrinter {

    private ProfilePrinter() {
    }

    public static void printProfile(Profile profile) {
        if (profile == null) {
            System.out.println(ConsoleColors.RED + "No profile selected." + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.CYAN + "Profile Details:" + ConsoleColors.RESET);
            System.out.println("Username: " + profile.getUsername());
            System.out.println("Sessions: " + profile.getNumberOfSessions());
            System.out.println("Following: " + profile.getNumberFollowing());
        }
    }

    public static void printProfiles(List<Profile> profiles, Profile currentProfile) {
        int index;

        System.out.println(ConsoleColors.CYAN + "Profiles:" + ConsoleColors.RESET);

        if (profiles == null || profiles.isEmpty()) {
            System.out.println(ConsoleColors.RED + "No Profiles found." + ConsoleColors.RESET);
        } else {
            index = 0;

            while (index < profiles.size()) {
                Profile p = profiles.get(index);

                if (currentProfile != null && p.getUsername().equals(currentProfile.getUsername())) {
                    System.out.println("[" + (index + 1) + "] " + p.getUsername() + ConsoleColors.GREEN + " --> Current Profile" + ConsoleColors.RESET);
                } else {
                    System.out.println("[" + (index + 1) + "] " + p.getUsername());
                }

                index++;
            }
        }

        System.out.println();
    }

    public static void printCurrentProfileHeader(Profile currentProfile) {
        if (currentProfile == null) {
            System.out.println(ConsoleColors.YELLOW + "No profile is currently selected." + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.GREEN + currentProfile.getUsername() + ConsoleColors.RESET + " is currently logged in.");
        }
    }
}
