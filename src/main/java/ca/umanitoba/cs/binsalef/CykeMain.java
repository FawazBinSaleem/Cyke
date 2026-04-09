package ca.umanitoba.cs.binsalef;

import ca.umanitoba.cs.binsalef.logic.CykeService;
import ca.umanitoba.cs.binsalef.logic.exceptions.InvalidSystemException;
import ca.umanitoba.cs.binsalef.model.Cyke;
import ca.umanitoba.cs.binsalef.model.WorldMap;
import ca.umanitoba.cs.binsalef.model.exceptions.MapNotFoundException;
import ca.umanitoba.cs.binsalef.persistence.CykePersistence;
import ca.umanitoba.cs.binsalef.persistence.exceptions.NotFoundException;
import ca.umanitoba.cs.binsalef.persistence.exceptions.PersistenceException;
import ca.umanitoba.cs.binsalef.persistence.json.CykePersistenceJson;
import ca.umanitoba.cs.binsalef.ui.CykeDisplay;

import java.nio.file.Path;
import java.util.Scanner;

public class CykeMain {

    public CykeMain() {
    }

    public void run() throws MapNotFoundException, InvalidSystemException {
        Scanner scanner;
        Cyke cyke;
        CykeService cykeService;
        CykeDisplay cykeDisplay;
        CykePersistence persistence;
        boolean dataLoaded;
        String filePath = "cyke-data.json"; //this should work, it just creates a file in the root folder, zach said its okay

        scanner = new Scanner(System.in);
        persistence = new CykePersistenceJson(Path.of(filePath));

        try {
            cyke = persistence.loadCyke();

            dataLoaded = true;
        } catch (NotFoundException | PersistenceException e) {
            cyke = new Cyke(new WorldMap(10, 10));
            dataLoaded = false;
        }

        cykeService = new CykeService(cyke, persistence);
        cykeDisplay = new CykeDisplay(cykeService, scanner, dataLoaded);

        cykeDisplay.run();

        scanner.close();
    }
}
