package ca.umanitoba.cs.binsalef.model;


import ca.umanitoba.cs.binsalef.model.exceptions.InvalidRouteException;
import ca.umanitoba.cs.binsalef.tests.TestResults;
import ca.umanitoba.cs.binsalef.tests.TestSuite;

public class TestRoute implements TestSuite {

    private final TestResults results = new TestResults();

    @Override
    public String name() {
        return "TestRoute";
    }

    @Override
    public TestResults runTests() {
        testAddFirstCoordinate();
        testAddValidHorizontalMove();
        testAddValidVerticalMove();
        testRejectDiagonalMove();
        testRejectJump();
        testRouteSizeUpdates();

        return results;
    }

    private void testAddFirstCoordinate() {
        try {
            Route route = new Route();
            route.addCoordinate(new Coordinate(0, 0));

            results.pass("First coordinate added successfully.");
        } catch (Exception e) {
            results.fail("Failed to add first coordinate.");
        }
    }

    private void testAddValidHorizontalMove() {
        try {
            Route route = new Route();
            route.addCoordinate(new Coordinate(0, 0));
            route.addCoordinate(new Coordinate(1, 0));

            results.pass("Valid horizontal move accepted.");
        } catch (Exception e) {
            results.fail("Valid horizontal move was rejected.");
        }
    }

    private void testAddValidVerticalMove() {
        try {
            Route route = new Route();
            route.addCoordinate(new Coordinate(0, 0));
            route.addCoordinate(new Coordinate(0, 1));

            results.pass("Valid vertical move accepted.");
        } catch (Exception e) {
            results.fail("Valid vertical move was rejected.");
        }
    }

    private void testRejectDiagonalMove() {
        try {
            Route route = new Route();
            route.addCoordinate(new Coordinate(0, 0));
            route.addCoordinate(new Coordinate(1, 1));

            results.fail("Diagonal move was accepted.");
        } catch (InvalidRouteException e) {
            results.pass("Diagonal move correctly rejected.");
        } catch (Exception e) {
            results.fail("Wrong exception for diagonal move.");
        }
    }

    private void testRejectJump() {
        try {
            Route route = new Route();
            route.addCoordinate(new Coordinate(0, 0));
            route.addCoordinate(new Coordinate(2, 0));

            results.fail("Jump move was accepted.");
        } catch (InvalidRouteException e) {
            results.pass("Jump move correctly rejected.");
        } catch (Exception e) {
            results.fail("Wrong exception for jump move.");
        }
    }

    private void testRouteSizeUpdates() {
        try {
            Route route = new Route();
            route.addCoordinate(new Coordinate(0, 0));
            route.addCoordinate(new Coordinate(1, 0));

            if (route.getCoordinates().size() == 2) {
                results.pass("Route size updates correctly.");
            } else {
                results.fail("Route size is incorrect.");
            }
        } catch (Exception e) {
            results.fail("Unknown exception when checking the route size.");
        }
    }
}