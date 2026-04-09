package ca.umanitoba.cs.binsalef.logic;

import ca.umanitoba.cs.binsalef.logic.exceptions.InvalidSystemException;
import ca.umanitoba.cs.binsalef.logic.exceptions.NoCurrentProfileException;
import ca.umanitoba.cs.binsalef.model.Coordinate;
import ca.umanitoba.cs.binsalef.model.Cyke;
import ca.umanitoba.cs.binsalef.model.ExerciseSession;
import ca.umanitoba.cs.binsalef.model.Profile;
import ca.umanitoba.cs.binsalef.model.Route;
import ca.umanitoba.cs.binsalef.model.WorldMap;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidCoordinateException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidNameException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidRouteException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidSessionException;
import ca.umanitoba.cs.binsalef.model.exceptions.MapNotFoundException;
import ca.umanitoba.cs.binsalef.persistence.CykePersistence;
import ca.umanitoba.cs.binsalef.persistence.FakePersistence;
import ca.umanitoba.cs.binsalef.tests.TestResults;
import ca.umanitoba.cs.binsalef.tests.TestSuite;

public class TestCykeService implements TestSuite {

    @Override
    public String name() {
        return "TestCykeService";
    }

    @Override
    public TestResults runTests() {
        TestResults results = new TestResults();

        testRejectNullSystem(results);
        testCreateProfile(results);
        testSelectProfile(results);
        testRenameProfile(results);
        testFollowProfile(results);
        testUnfollowProfile(results);
        testAddActivity(results);
        testAddActivityRejectsNoCurrentProfile(results);
        testAddObstacle(results);
        testRemoveObstacle(results);
        testGetCopiedRoute(results);

        return results;
    }

    private void testRejectNullSystem(TestResults results) {
        CykePersistence persistence = new FakePersistence();
        try {
            new CykeService(null, persistence);
            results.fail("CykeService accepted a null Cyke system.");
        } catch (InvalidSystemException e) {
            results.pass("CykeService rejected a null Cyke system.");
        } catch (Exception e) {
            results.fail("CykeService threw the wrong exception for a null Cyke system.");
        }
    }

    private void testCreateProfile(TestResults results) {
        try {
            CykeService service = createService();
            Profile profile = createProfile("Fawaz");

            service.createProfile(profile);

            if (service.getProfiles().size() == 1
                    && service.getProfiles().get(0).getUsername().equals("Fawaz")) {
                results.pass("CykeService created the profile correctly.");
            } else {
                results.fail("CykeService did not create profile correctly.");
            }
        } catch (Exception e) {
            results.fail("testCreateProfile() threw an exception.");
        }
    }

    private void testSelectProfile(TestResults results) {
        try {
            CykeService service = createService();
            Profile profileA = createProfile("A");
            Profile profileB = createProfile("B");

            service.createProfile(profileA);
            service.createProfile(profileB);
            service.selectProfile("B");

            if (service.getCurrentProfile() == profileB) {
                results.pass("CykeService selected profile correctly.");
            } else {
                results.fail("CykeService did not select the correct profile.");
            }
        } catch (Exception e) {
            results.fail("testSelectProfile() threw an exception.");
        }
    }

    private void testRenameProfile(TestResults results) {
        try {
            CykeService service = createService();
            Profile profile = createProfile("OldName");

            service.createProfile(profile);
            service.renameProfile("OldName", "NewName");

            if (service.getCurrentProfile().getUsername().equals("NewName")) {
                results.pass("CykeService renamed profile correctly.");
            } else {
                results.fail("CykeService did not rename profile correctly.");
            }
        } catch (Exception e) {
            results.fail("testRenameProfile() threw an exception.");
        }
    }

    private void testFollowProfile(TestResults results) {
        try {
            CykeService service = createService();
            Profile profileA = createProfile("A");
            Profile profileB = createProfile("B");

            service.createProfile(profileA);
            service.createProfile(profileB);
            service.selectProfile("A");
            service.followProfile("B");

            if (service.getCurrentProfile().getFollowingList().contains(profileB)) {
                results.pass("CykeService followed profile correctly.");
            } else {
                results.fail("CykeService did not follow profile correctly.");
            }
        } catch (Exception e) {
            results.fail("testFollowProfile() threw an exception.");
        }
    }

    private void testUnfollowProfile(TestResults results) {
        try {
            CykeService service = createService();
            Profile profileA = createProfile("A");
            Profile profileB = createProfile("B");

            service.createProfile(profileA);
            service.createProfile(profileB);
            service.selectProfile("A");
            service.followProfile("B");
            service.unfollowProfile("B");

            if (!service.getCurrentProfile().getFollowingList().contains(profileB)) {
                results.pass("CykeService unfollowed profile correctly.");
            } else {
                results.fail("CykeService did not unfollow profile correctly.");
            }
        } catch (Exception e) {
            results.fail("testUnfollowProfile() threw an exception.");
        }
    }

    private void testAddActivity(TestResults results) {
        try {
            CykeService service = createService();
            Profile profile = createProfile("Fawaz");
            ExerciseSession session = createSession(1, createRoute(0, 0, 1, 0), 20, 100);

            service.createProfile(profile);
            service.addActivity(session);

            if (service.getCurrentProfile().getSessions().size() == 1
                    && service.getCurrentProfile().getSessions().get(0) == session) {
                results.pass("CykeService added activities correctly.");
            } else {
                results.fail("CykeService did not add activity correctly.");
            }
        } catch (Exception e) {
            results.fail("testAddActivity() threw an exception.");
        }
    }

    private void testAddActivityRejectsNoCurrentProfile(TestResults results) {
        try {
            CykeService service = createService();
            ExerciseSession session = createSession(1, createRoute(0, 0, 1, 0), 20, 100);

            service.addActivity(session);

            results.fail("CykeService accepted an activity with no current profile.");
        } catch (NoCurrentProfileException e) {
            results.pass("CykeService rejected an activity when no current profile is selected.");
        } catch (Exception e) {
            results.fail("CykeService threw the wrong exception for adding an activity with no current profile.");
        }
    }

    private void testAddObstacle(TestResults results) {
        try {
            CykeService service = createService();
            Coordinate obstacle = new Coordinate(1, 1);

            service.addObstacle(obstacle);

            if (service.getWorldMap().getObstacles().contains(obstacle)) {
                results.pass("CykeService added obstacles correctly.");
            } else {
                results.fail("CykeService did not add obstacle correctly.");
            }
        } catch (Exception e) {
            results.fail("testAddObstacle() threw an exception.");
        }
    }

    private void testRemoveObstacle(TestResults results) {
        try {
            CykeService service = createService();
            Coordinate obstacle = new Coordinate(1, 1);

            service.addObstacle(obstacle);
            service.removeObstacle(0);

            if (!service.getWorldMap().getObstacles().contains(obstacle)) {
                results.pass("CykeService removed obstacles correctly.");
            } else {
                results.fail("CykeService did not remove obstacle correctly.");
            }
        } catch (Exception e) {
            results.fail("testRemoveObstacle() threw an exception.");
        }
    }

    private void testGetCopiedRoute(TestResults results) {
        try {
            CykeService service = createService();
            Profile profile = createProfile("Fawaz");
            Route originalRoute = createRoute(0, 0, 1, 0);
            ExerciseSession session = createSession(1, originalRoute, 20, 100);

            service.createProfile(profile);
            service.addActivity(session);

            Route copiedRoute = service.getCopiedRoute(1);

            if (copiedRoute != null
                    && copiedRoute != originalRoute
                    && copiedRoute.getCoordinates().size() == originalRoute.getCoordinates().size()
                    && copiedRoute.getCoordinates().get(0).getX() == originalRoute.getCoordinates().get(0).getX()
                    && copiedRoute.getCoordinates().get(1).getX() == originalRoute.getCoordinates().get(1).getX()) {
                results.pass("CykeService copied routes correctly.");
            } else {
                results.fail("CykeService did not copy route correctly.");
            }
        } catch (Exception e) {
            results.fail("testGetCopiedRoute() threw an unexpected exception.");
        }
    }

    private CykeService createService() throws MapNotFoundException, InvalidSystemException {
        Cyke cyke = new Cyke(new WorldMap(5, 5));
        CykePersistence persistence = new FakePersistence();
        return new CykeService(cyke, persistence);
    }

    private Profile createProfile(String username) throws InvalidNameException {
        Profile.ProfileBuilder builder = new Profile.ProfileBuilder();
        builder.username(username);
        return builder.build();
    }

    private Route createRoute(int x1, int y1, int x2, int y2) throws InvalidCoordinateException, InvalidRouteException {
        Route route = new Route();
        route.addCoordinate(new Coordinate(x1, y1));
        route.addCoordinate(new Coordinate(x2, y2));
        return route;
    }

    private ExerciseSession createSession(int id, Route route, int minutes, int calories) throws InvalidSessionException, InvalidRouteException {
        ExerciseSession.ExerciseSessionBuilder builder = new ExerciseSession.ExerciseSessionBuilder();
        builder.sessionID(id);
        builder.route(route);
        builder.minutes(minutes);
        builder.calories(calories);
        return builder.build();
    }
}