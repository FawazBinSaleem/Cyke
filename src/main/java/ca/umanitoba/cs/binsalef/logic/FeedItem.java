package ca.umanitoba.cs.binsalef.logic;

import ca.umanitoba.cs.binsalef.model.ExerciseSession;
import ca.umanitoba.cs.binsalef.model.Profile;

public class FeedItem {
    private Profile owner;
    private ExerciseSession session;

    public FeedItem(Profile owner, ExerciseSession session) {
        this.owner = owner;
        this.session = session;
    }

    public Profile getOwner() {
        return owner;
    }

    public ExerciseSession getSession() {
        return session;
    }
}
