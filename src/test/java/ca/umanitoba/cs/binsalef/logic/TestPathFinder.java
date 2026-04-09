package ca.umanitoba.cs.binsalef.logic;

import ca.umanitoba.cs.binsalef.logic.exceptions.PathNotFoundException;
import ca.umanitoba.cs.binsalef.model.Coordinate;
import ca.umanitoba.cs.binsalef.model.Route;
import ca.umanitoba.cs.binsalef.model.WorldMap;
import ca.umanitoba.cs.binsalef.tests.TestResults;
import ca.umanitoba.cs.binsalef.tests.TestSuite;

import java.util.ArrayList;
import java.util.List;

public class TestPathFinder implements TestSuite {

    @Override
    public String name() {
        return "TestPathFinder";
    }

    @Override
    public TestResults runTests() {
        TestResults results = new TestResults();

        testFindRouteSimplePath(results);
        testRejectNullStart(results);
        testRejectStartNotAllowed(results);
        testRejectBlockedPath(results);
        testStartEqualsEnd(results);

        return results;
    }

    private void testFindRouteSimplePath(TestResults results) {
        try {
            PathFinder pathFinder = new PathFinder();
            WorldMap worldMap = new WorldMap(3, 3);
            Coordinate start = new Coordinate(0, 0);
            Coordinate end = new Coordinate(2, 0);
            List<Coordinate> allowed = createAllowedCoordinates(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0));

            Route route = pathFinder.findRoute(start, end, worldMap, allowed);

            if (route.getCoordinates().size() == 3 && route.getCoordinates().get(0).getX() == 0 && route.getCoordinates().get(0).getY() == 0 && route.getCoordinates().get(2).getX() == 2 && route.getCoordinates().get(2).getY() == 0) {
                results.pass("PathFinder found a simple valid route.");
            } else {
                results.fail("PathFinder returned an incorrect simple route.");
            }
        } catch (Exception e) {
            results.fail("testFindRouteSimplePath() threw an exception.");
        }
    }

    private void testRejectNullStart(TestResults results) {
        try {
            PathFinder pathFinder = new PathFinder();
            WorldMap worldMap = new WorldMap(3, 3);
            Coordinate end = new Coordinate(1, 0);
            List<Coordinate> allowed = createAllowedCoordinates(new Coordinate(0, 0), new Coordinate(1, 0));

            pathFinder.findRoute(null, end, worldMap, allowed);

            results.fail("PathFinder accepted a null start coordinate.");
        } catch (PathNotFoundException e) {
            results.pass("PathFinder rejected a null start coordinate.");
        } catch (Exception e) {
            results.fail("PathFinder threw the wrong exception for a null start coordinate.");
        }
    }

    private void testRejectStartNotAllowed(TestResults results) {
        try {
            PathFinder pathFinder = new PathFinder();
            WorldMap worldMap = new WorldMap(3, 3);
            Coordinate start = new Coordinate(0, 0);
            Coordinate end = new Coordinate(1, 0);
            List<Coordinate> allowed = createAllowedCoordinates(new Coordinate(1, 0));

            pathFinder.findRoute(start, end, worldMap, allowed);

            results.fail("PathFinder accepted a start coordinate that was not allowed.");
        } catch (PathNotFoundException e) {
            results.pass("PathFinder rejected a start coordinate that is not allowed.");
        } catch (Exception e) {
            results.fail("PathFinder threw the wrong exception for a start coordinate that is not allowed.");
        }
    }

    private void testRejectBlockedPath(TestResults results) {
        try {
            PathFinder pathFinder = new PathFinder();
            WorldMap worldMap = new WorldMap(3, 1);
            worldMap.addObstacle(new Coordinate(1, 0));

            Coordinate start = new Coordinate(0, 0);
            Coordinate end = new Coordinate(2, 0);

            List<Coordinate> allowed = createAllowedCoordinates(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0));

            pathFinder.findRoute(start, end, worldMap, allowed);

            results.fail("PathFinder found a route through a blocked path.");
        } catch (PathNotFoundException e) {
            results.pass("PathFinder rejected blocked paths.");
        } catch (Exception e) {
            results.fail("PathFinder threw the wrong exception for a blocked path.");
        }
    }

    private void testStartEqualsEnd(TestResults results) {
        try {
            PathFinder pathFinder = new PathFinder();
            WorldMap worldMap = new WorldMap(3, 3);
            Coordinate start = new Coordinate(1, 1);
            List<Coordinate> allowed = createAllowedCoordinates(new Coordinate(1, 1));

            Route route = pathFinder.findRoute(start, start, worldMap, allowed);

            if (route.getCoordinates().size() == 1 && route.getCoordinates().get(0).getX() == 1 && route.getCoordinates().get(0).getY() == 1) {
                results.pass("PathFinder handled testStartEqualsEnd() correctly.");
            } else {
                results.fail("PathFinder returned an incorrect route when testStartEqualsEnd() ");
            }
        } catch (Exception e) {
            results.fail("testStartEqualsEnd() threw an unexpected exception.");
        }
    }

    private List<Coordinate> createAllowedCoordinates(Coordinate... coordinates) {
        List<Coordinate> allowed = new ArrayList<>();
        int index = 0;

        while (index < coordinates.length) {
            allowed.add(coordinates[index]);
            index++;
        }

        return allowed;
    }
}