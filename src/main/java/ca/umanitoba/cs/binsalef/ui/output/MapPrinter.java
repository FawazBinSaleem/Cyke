package ca.umanitoba.cs.binsalef.ui.output;

import ca.umanitoba.cs.binsalef.model.Coordinate;
import ca.umanitoba.cs.binsalef.model.WorldMap;


public class MapPrinter {
    private MapPrinter() {
    }


    //just shows all of the obstacles in a nice format
    public static void showObstacles(WorldMap worldMap) {
        System.out.println(ConsoleColors.CYAN + "Obstacles:" + ConsoleColors.RESET);
        if (worldMap.getObstacles().isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < worldMap.getObstacles().size(); i++) {
                Coordinate coordinate = worldMap.getObstacles().get(i);
                System.out.println("[" + i + "](" + coordinate.getX() + "," + coordinate.getY() + ")");
            }
        }
        System.out.println();
    }


}
