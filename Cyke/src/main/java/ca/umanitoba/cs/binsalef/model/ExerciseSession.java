package ca.umanitoba.cs.binsalef.model;

import ca.umanitoba.cs.binsalef.model.exceptions.InvalidRouteException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidSessionException;
import com.google.common.base.Preconditions;

import java.time.LocalDate;

public class ExerciseSession {

    private final int sessionID;
    private final Route route;
    private final int minutes;
    private final int calories;
    private final LocalDate sessionDate;

    public ExerciseSession(int sessionID, Route route, int minutes, int calories) {
        checkSession(sessionID, route, minutes, calories);

        this.sessionID = sessionID;
        this.route = route;
        this.minutes = minutes;
        this.calories = calories;
        this.sessionDate = LocalDate.now();
    }

    public static class ExerciseSessionBuilder {
        private int sessionID;
        private Route route;
        private int minutes;
        private int calories;

        public ExerciseSessionBuilder sessionID(int sessionID) throws InvalidSessionException {
            if (sessionID >= 1) {
                this.sessionID = sessionID;
            } else {
                throw new InvalidSessionException();
            }
            return this;
        }

        public ExerciseSessionBuilder route(Route route) throws InvalidRouteException {
            if (route != null && route.getSize() >= 1) {
                this.route = route;
            } else {
                throw new InvalidRouteException();
            }
            return this;
        }

        public ExerciseSessionBuilder minutes(int minutes) throws InvalidSessionException {
            if (minutes >= 0) {
                this.minutes = minutes;
            } else {
                throw new InvalidSessionException();
            }
            return this;
        }

        public ExerciseSessionBuilder calories(int calories) throws InvalidSessionException {
            if (calories >= 0) {
                this.calories = calories;
            } else {
                throw new InvalidSessionException();
            }
            return this;
        }

        public ExerciseSession build() {
            return new ExerciseSession(sessionID, route, minutes, calories);
        }

    }

    private void checkSession(int sessionID, Route route, int minutes, int calories) {
        Preconditions.checkArgument(sessionID >= 1, "sessionID must be >= 1");
        Preconditions.checkNotNull(route, "route must not be null");
        Preconditions.checkArgument(minutes >= 0, "minutes must be >= 0");
        Preconditions.checkArgument(calories >= 0, "calories must be >= 0");
        Preconditions.checkArgument(route.getSize() >= 1, "route must have at least 1 coordinate");
    }

    public int getSessionID() {
        return sessionID;
    }

    public int getMinutes() {

        return minutes;
    }

    public int getCalories() {

        return calories;
    }

    public LocalDate getSessionDate() {

        return sessionDate;
    }

    public Route getRoute() {

        return route;
    }


}
