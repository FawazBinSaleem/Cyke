package ca.umanitoba.cs.binsalef.model;

import ca.umanitoba.cs.binsalef.model.exceptions.InvalidCoordinateException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidObstacleException;
import ca.umanitoba.cs.binsalef.tests.TestResults;
import ca.umanitoba.cs.binsalef.tests.TestSuite;

public class TestWorldMap implements TestSuite {

    @Override
    public String name() {
        return "TestWorldMap";
    }

    @Override
    public TestResults runTests() {
        TestResults results = new TestResults();

        testValidCoordinate(results);
        testInvalidCoordinateOutOfBounds(results);
        testNullCoordinateRejected(results);
        testAddObstacle(results);
        testRejectDuplicateObstacle(results);
        testRejectOutOfBoundsObstacle(results);
        testRemoveObstacle(results);
        testRejectInvalidObstacleIndex(results);
        testIsObstacle(results);

        return results;
    }

    private void testValidCoordinate(TestResults results) {
        try {
            WorldMap map = new WorldMap(5, 5);
            Coordinate coordinate = new Coordinate(2, 3);

            if (map.isValidCoordinate(coordinate)) {
                results.pass("WorldMap accepts valid in-bounds coordinates.");
            } else {
                results.fail("WorldMap rejected a valid in-bounds coordinate.");
            }
        } catch (Exception e) {
            results.fail("testValidCoordinate() threw an unknown exception.");
        }
    }

    private void testInvalidCoordinateOutOfBounds(TestResults results) {
        try {
            WorldMap map = new WorldMap(5, 5);
            Coordinate coordinate = new Coordinate(5, 5);

            if (!map.isValidCoordinate(coordinate)) {
                results.pass("WorldMap rejects out-of-bounds coordinates.");
            } else {
                results.fail("WorldMap accepted an out-of-bounds coordinate.");
            }
        } catch (Exception e) {
            results.fail("testInvalidCoordinateOutOfBounds() threw an unknown exception.");
        }
    }

    private void testNullCoordinateRejected(TestResults results) {
        try {
            WorldMap map = new WorldMap(5, 5);
            map.isValidCoordinate(null);

            results.fail("WorldMap accepted a null coordinate.");
        } catch (InvalidCoordinateException e) {
            results.pass("WorldMap rejects null coordinates.");
        } catch (Exception e) {
            results.fail("WorldMap threw the wrong exception for a null coordinate.");
        }
    }

    private void testAddObstacle(TestResults results) {
        try {
            WorldMap map = new WorldMap(5, 5);
            Coordinate obstacle = new Coordinate(1, 1);
            int index = map.addObstacle(obstacle);

            if (index == 0 && map.getObstacles().contains(obstacle)) {
                results.pass("WorldMap adds an obstacle correctly.");
            } else {
                results.fail("WorldMap did not add obstacle correctly.");
            }
        } catch (Exception e) {
            results.fail("testAddObstacle() threw an unexpected exception.");
        }
    }

    private void testRejectDuplicateObstacle(TestResults results) {
        try {
            WorldMap map = new WorldMap(5, 5);
            Coordinate obstacle = new Coordinate(1, 1);

            map.addObstacle(obstacle);
            map.addObstacle(obstacle);

            results.fail("WorldMap accepted a duplicate obstacle.");
        } catch (InvalidObstacleException e) {
            results.pass("WorldMap rejects duplicate obstacles.");
        } catch (Exception e) {
            results.fail("WorldMap threw the wrong exception for a duplicate obstacle.");
        }
    }

    private void testRejectOutOfBoundsObstacle(TestResults results) {
        try {
            WorldMap map = new WorldMap(5, 5);
            Coordinate obstacle = new Coordinate(9, 9);

            map.addObstacle(obstacle);

            results.fail("WorldMap accepted an out-of-bounds obstacle.");
        } catch (InvalidCoordinateException e) {
            results.pass("WorldMap rejects out-of-bounds obstacles.");
        } catch (Exception e) {
            results.fail("WorldMap threw the wrong exception for an out-of-bounds obstacle.");
        }
    }

    private void testRemoveObstacle(TestResults results) {
        try {
            WorldMap map = new WorldMap(5, 5);
            Coordinate obstacle = new Coordinate(2, 2);

            map.addObstacle(obstacle);
            map.removeObstacle(0);

            if (!map.getObstacles().contains(obstacle)) {
                results.pass("WorldMap removes obstacles correctly.");
            } else {
                results.fail("WorldMap did not remove obstacle correctly.");
            }
        } catch (Exception e) {
            results.fail("testRemoveObstacle() threw an unknown exception.");
        }
    }

    private void testRejectInvalidObstacleIndex(TestResults results) {
        try {
            WorldMap map = new WorldMap(5, 5);
            map.removeObstacle(0);

            results.fail("WorldMap accepted an invalid obstacle index.");
        } catch (InvalidObstacleException e) {
            results.pass("WorldMap rejects invalid obstacle indices.");
        } catch (Exception e) {
            results.fail("WorldMap threw the wrong exception for testRejectInvalidObstacleIndex()");
        }
    }

    private void testIsObstacle(TestResults results) {
        try {
            WorldMap map = new WorldMap(5, 5);
            Coordinate obstacle = new Coordinate(3, 3);

            map.addObstacle(obstacle);

            if (map.isObstacle(obstacle)) {
                results.pass("WorldMap correctly identifies obstacles.");
            } else {
                results.fail("WorldMap failed to identify an obstacle.");
            }
        } catch (Exception e) {
            results.fail("testIsObstacle() threw an unknown exception.");
        }
    }
}