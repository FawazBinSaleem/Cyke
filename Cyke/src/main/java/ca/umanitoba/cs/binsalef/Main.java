package ca.umanitoba.cs.binsalef;

import ca.umanitoba.cs.binsalef.logic.exceptions.InvalidSystemException;
import ca.umanitoba.cs.binsalef.model.exceptions.MapNotFoundException;
import ca.umanitoba.cs.binsalef.ui.output.ConsoleColors;


public class Main {

    public static void main(String[] args) {
        CykeMain cykeMain = new CykeMain();

        try {
            cykeMain.run();
        } catch (InvalidSystemException | MapNotFoundException e) {
            System.out.println(ConsoleColors.RED + "Application failed to start: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

}

