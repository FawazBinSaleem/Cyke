package ca.umanitoba.cs.binsalef.ui.output;

import ca.umanitoba.cs.binsalef.model.ExerciseSession;
import ca.umanitoba.cs.binsalef.model.Profile;

import java.util.List;

public class ActivityPrinter {
    private ActivityPrinter() {
    }

    public static void printAllActivities(Profile profile) {
        System.out.println(ConsoleColors.CYAN + "Activities:" + ConsoleColors.RESET);

        List<ExerciseSession> sessions = profile.getSessions();

        if (sessions.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < sessions.size(); i++) {
                ExerciseSession session = sessions.get(i);

                System.out.println("Date: " + session.getSessionDate());
                System.out.println("Session ID: " + session.getSessionID());
                System.out.println("Minutes: " + session.getMinutes());
                System.out.println("Calories: " + session.getCalories());
                printRoute(session);
                System.out.println();
            }
        }
    }

    public static void printRoute(ExerciseSession session) {
        int index = 0;
        System.out.print("Route: ");
        while (index < session.getRoute().getCoordinates().size()) {
            System.out.print("(" + session.getRoute().getCoordinates().get(index).getX() + "," + session.getRoute().getCoordinates().get(index).getY() + ")");
            if (index < session.getRoute().getCoordinates().size() - 1) {
                System.out.print("->");
            }
            index++;
        }
        System.out.println();
    }


}
