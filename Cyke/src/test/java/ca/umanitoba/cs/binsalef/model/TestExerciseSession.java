package ca.umanitoba.cs.binsalef.model;

import ca.umanitoba.cs.binsalef.model.exceptions.InvalidCoordinateException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidRouteException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidSessionException;
import ca.umanitoba.cs.binsalef.tests.TestResults;
import ca.umanitoba.cs.binsalef.tests.TestSuite;

public class TestExerciseSession implements TestSuite {

    @Override
    public String name() {
        return "TestExerciseSession";
    }

    @Override
    public TestResults runTests() {
        TestResults results = new TestResults();

        testCreateValidSession(results);
        testRejectInvalidSessionID(results);
        testRejectNegativeCalories(results);
        testRejectNegativeMinutes(results);
        testRejectNullRoute(results);
        testRejectEmptyRoute(results);

        return results;
    }

    private void testCreateValidSession(TestResults results) {
        try {
            Route route = createValidRoute();
            ExerciseSession.ExerciseSessionBuilder builder = new ExerciseSession.ExerciseSessionBuilder();
            builder.sessionID(1);
            builder.route(route);
            builder.minutes(30);
            builder.calories(200);

            ExerciseSession session = builder.build();

            if (session.getSessionID() == 1 && session.getRoute() == route && session.getMinutes() == 30 && session.getCalories() == 200) {
                results.pass("A valid exercise session is created successfully.");
            } else {
                results.fail("A valid exercise session was created, but one or more fields were incorrect.");
            }

        } catch (InvalidCoordinateException e) {
            results.fail("testCreateValidSession() could not create a valid coordinate.");
        } catch (InvalidRouteException e) {
            results.fail("testCreateValidSession() could not create a valid route.");
        } catch (Exception e) {
            results.fail("testCreateValidSession() caused an unknown exception.");
        }
    }

    private void testRejectInvalidSessionID(TestResults results) {
        try {
            ExerciseSession.ExerciseSessionBuilder builder = new ExerciseSession.ExerciseSessionBuilder();
            builder.sessionID(0);

            results.fail("ExerciseSessionBuilder accepted an invalid session ID.");
        } catch (InvalidSessionException e) {
            results.pass("ExerciseSessionBuilder rejects session IDs less than 1.");
        } catch (Exception e) {
            results.fail("ExerciseSessionBuilder threw the wrong exception for an invalid session ID.");
        }
    }

    private void testRejectNegativeCalories(TestResults results) {
        try {
            ExerciseSession.ExerciseSessionBuilder builder = new ExerciseSession.ExerciseSessionBuilder();
            builder.calories(-220);

            results.fail("ExerciseSessionBuilder accepted negative calories.");
        } catch (InvalidSessionException e) {
            results.pass("ExerciseSessionBuilder rejects negative calories.");
        } catch (Exception e) {
            results.fail("ExerciseSessionBuilder threw the wrong exception for negative calories.");
        }
    }

    private void testRejectNegativeMinutes(TestResults results) {
        try {
            ExerciseSession.ExerciseSessionBuilder builder = new ExerciseSession.ExerciseSessionBuilder();
            builder.minutes(-100);

            results.fail("ExerciseSessionBuilder accepted negative minutes.");
        } catch (InvalidSessionException e) {
            results.pass("ExerciseSessionBuilder rejects negative minutes.");
        } catch (Exception e) {
            results.fail("ExerciseSessionBuilder threw the wrong exception for negative minutes.");
        }
    }

    private void testRejectNullRoute(TestResults results) {
        try {
            ExerciseSession.ExerciseSessionBuilder builder = new ExerciseSession.ExerciseSessionBuilder();
            builder.route(null);

            results.fail("ExerciseSessionBuilder accepted a null route.");
        } catch (InvalidRouteException e) {
            results.pass("ExerciseSessionBuilder rejects a null route.");
        } catch (Exception e) {
            results.fail("ExerciseSessionBuilder threw the wrong exception for a null route.");
        }
    }

    private void testRejectEmptyRoute(TestResults results) {
        try {
            Route route = new Route();
            ExerciseSession.ExerciseSessionBuilder builder = new ExerciseSession.ExerciseSessionBuilder();
            builder.route(route);

            results.fail("ExerciseSessionBuilder accepted an empty route.");
        } catch (InvalidRouteException e) {
            results.pass("ExerciseSessionBuilder rejects an empty route.");
        } catch (Exception e) {
            results.fail("ExerciseSessionBuilder threw the wrong exception for an empty route.");
        }
    }

    private Route createValidRoute() throws InvalidCoordinateException, InvalidRouteException {
        Route route = new Route();

        route.addCoordinate(new Coordinate(0, 0));
        route.addCoordinate(new Coordinate(1, 0));

        return route;
    }
}
