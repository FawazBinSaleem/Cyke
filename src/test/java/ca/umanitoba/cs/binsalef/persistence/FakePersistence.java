package ca.umanitoba.cs.binsalef.persistence;

import ca.umanitoba.cs.binsalef.model.Cyke;
import ca.umanitoba.cs.binsalef.persistence.exceptions.NotFoundException;


//I had to create this file because I implemented persistence in the main program
// but since I had refactored the main CykeService to support persistence, the TestCykeService also needed to be updated.
// so i thought what if i created a fake persistence that just existed in RAM and pass it in the TestCykeService

public class FakePersistence implements CykePersistence {

    private Cyke stored;

    @Override
    public Cyke saveCyke(Cyke cyke) {

        stored = cyke;
        return stored;
    }

    @Override
    public Cyke loadCyke() throws NotFoundException {
        if (stored == null) {
            throw new NotFoundException();
        }
        return stored;
    }


}
