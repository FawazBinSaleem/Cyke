package ca.umanitoba.cs.binsalef.persistence;

import ca.umanitoba.cs.binsalef.model.Cyke;
import ca.umanitoba.cs.binsalef.persistence.exceptions.NotFoundException;
import ca.umanitoba.cs.binsalef.persistence.exceptions.PersistenceException;

public interface CykePersistence {
    Cyke saveCyke(Cyke cyke) throws PersistenceException;

    Cyke loadCyke() throws NotFoundException, PersistenceException;
}
