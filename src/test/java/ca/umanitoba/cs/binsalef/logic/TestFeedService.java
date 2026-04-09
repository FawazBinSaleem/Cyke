package ca.umanitoba.cs.binsalef.logic;

import ca.umanitoba.cs.binsalef.logic.exceptions.EmptyProfileException;
import ca.umanitoba.cs.binsalef.model.Coordinate;
import ca.umanitoba.cs.binsalef.model.ExerciseSession;
import ca.umanitoba.cs.binsalef.model.Profile;
import ca.umanitoba.cs.binsalef.model.Route;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidCoordinateException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidNameException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidRouteException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidSessionException;
import ca.umanitoba.cs.binsalef.tests.TestResults;
import ca.umanitoba.cs.binsalef.tests.TestSuite;

import java.util.List;

public class TestFeedService implements TestSuite {

    @Override
    public String name() {
        return "TestFeedService";
    }

    @Override
    public TestResults runTests() {
        TestResults results = new TestResults();

        testFeedRejectNullProfile(results);
        testFeedIncludesOwnSessions(results);
        testFeedIncludesFollowedSessions(results);
        testBuildMyCoordinates(results);
        testFeedCoordinates(results);

        return results;
    }

    private void testFeedRejectNullProfile(TestResults results) {
        try {
            FeedService feedService = new FeedService();
            feedService.buildFeed(null);

            results.fail("FeedService accepted a null profile.");
        } catch (EmptyProfileException e) {
            results.pass("FeedService rejects a null profile.");
        } catch (Exception e) {
            results.fail("testFeedRejectNullProfile() threw an exception.");
        }
    }

    private void testFeedIncludesOwnSessions(TestResults results) {
        try {
            FeedService feedService = new FeedService();
            Profile profile = createProfile("A");
            ExerciseSession session = createSession(1, createRoute(0, 0, 1, 0), 20, 100);

            profile.addSession(session);

            List<FeedItem> feed = feedService.buildFeed(profile);

            if (feed.size() == 1 && feed.get(0).getOwner() == profile && feed.get(0).getSession() == session) {
                results.pass("FeedService includes the current profile's own sessions.");
            } else {
                results.fail("FeedService did not include the current profile's own sessions correctly.");
            }
        } catch (Exception e) {
            results.fail("testFeedIncludesOwnSessions() threw an exception.");
        }
    }

    private void testFeedIncludesFollowedSessions(TestResults results) {
        try {
            FeedService feedService = new FeedService();
            Profile profileA = createProfile("A");
            Profile profileB = createProfile("B");

            ExerciseSession sessionA = createSession(1, createRoute(0, 0, 1, 0), 20, 100);
            ExerciseSession sessionB = createSession(2, createRoute(2, 0, 2, 1), 25, 120);

            profileA.addSession(sessionA);
            profileB.addSession(sessionB);
            profileA.follow(profileB);

            List<FeedItem> feed = feedService.buildFeed(profileA);

            if (feed.size() == 2) {
                results.pass("FeedService includes followed profiles' sessions.");
            } else {
                results.fail("FeedService did not include followed profiles' sessions correctly.");
            }
        } catch (Exception e) {
            results.fail("testFeedIncludesFollowedSessions() threw an exception.");
        }
    }

    private void testBuildMyCoordinates(TestResults results) {
        try {
            FeedService feedService = new FeedService();
            Profile profile = createProfile("A");

            ExerciseSession session1 = createSession(1, createRoute(0, 0, 1, 0), 20, 100);
            ExerciseSession session2 = createSession(2, createRoute(1, 0, 1, 1), 25, 120);

            profile.addSession(session1);
            profile.addSession(session2);

            List<Coordinate> coordinates = feedService.buildMyCoordinates(profile);

            if (coordinates.size() == 3) {
                results.pass("FeedService builds unique coordinates from a profile's own sessions.");
            } else {
                results.fail("FeedService did not build unique coordinates from own sessions correctly.");
            }
        } catch (Exception e) {
            results.fail("testBuildMyCoordinates() threw an exception.");
        }
    }

    private void testFeedCoordinates(TestResults results) {
        try {
            FeedService feedService = new FeedService();
            Profile profileA = createProfile("A");
            Profile profileB = createProfile("B");

            ExerciseSession sessionA = createSession(1, createRoute(0, 0, 1, 0), 20, 100);
            ExerciseSession sessionB = createSession(2, createRoute(2, 0, 2, 1), 25, 120);

            profileA.addSession(sessionA);
            profileB.addSession(sessionB);
            profileA.follow(profileB);

            List<Coordinate> coordinates = feedService.buildFeedCoordinates(profileA);

            if (coordinates.size() == 4) {
                results.pass("FeedService builds coordinates from own and followed sessions.");
            } else {
                results.fail("FeedService did not build feed coordinates correctly.");
            }
        } catch (Exception e) {
            results.fail("testFeedCoordinates() threw an exception.");
        }
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