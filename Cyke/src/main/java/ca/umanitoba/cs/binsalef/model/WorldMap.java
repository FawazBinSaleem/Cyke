package ca.umanitoba.cs.binsalef.model;

import ca.umanitoba.cs.binsalef.model.exceptions.InvalidCoordinateException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidObstacleException;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;


public class WorldMap {
    private final int width;
    private final int height;
    private final List<Coordinate> obstacles;

    public WorldMap(int width, int height) {
        Preconditions.checkArgument(width >= 1, "width must be >=1");
        Preconditions.checkArgument(height >= 1, "height must be >=1");
        this.width = width;
        this.height = height;
        this.obstacles = new ArrayList<>();
    }


    public List<Coordinate> getObstacles() {

        return obstacles;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    private void checkCoordinate(Coordinate coordinate) throws InvalidCoordinateException {
        if (coordinate == null) {
            throw new InvalidCoordinateException();
        }
    }

    public boolean isValidCoordinate(Coordinate coordinate) throws InvalidCoordinateException {
        checkCoordinate(coordinate);
        return (coordinate.getX() >= 0 && coordinate.getX() < width) && (coordinate.getY() >= 0 && coordinate.getY() < height);
    }

    public boolean isObstacle(Coordinate coordinate) throws InvalidCoordinateException {
        checkCoordinate(coordinate);
        boolean obstacleFound = false;
        for (Coordinate obst : obstacles) {
            if (!obstacleFound && obst.getX() == coordinate.getX() && obst.getY() == coordinate.getY()) {
                obstacleFound = true;
            }
        }

        return obstacleFound;

    }

    public int addObstacle(Coordinate coordinate) throws InvalidCoordinateException, InvalidObstacleException {
        int index = 0;
        checkCoordinate(coordinate);
        if (!isValidCoordinate(coordinate)) {
            throw new InvalidCoordinateException();
        }
        if (isObstacle(coordinate)) {
            throw new InvalidObstacleException();
        }
        obstacles.add(coordinate);
        index = obstacles.size() - 1; //the obstacle stored at the end of list is assigned to index

        return index;
    }

    public void removeObstacle(int index) throws InvalidObstacleException {
        if (index < 0 || index >= obstacles.size()) {
            throw new InvalidObstacleException();
        }
        obstacles.remove(index);
    }


}

