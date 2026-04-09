package ca.umanitoba.cs.binsalef.logic;

import java.util.ArrayList;
import java.util.List;

import ca.umanitoba.cs.binsalef.model.Coordinate;
import ca.umanitoba.cs.binsalef.model.Route;
import ca.umanitoba.cs.binsalef.model.WorldMap;
import ca.umanitoba.cs.binsalef.logic.exceptions.PathNotFoundException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidCoordinateException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidRouteException;
import ca.umanitoba.cs.binsalef.stack.LinkedStack;

public class PathFinder {

    public Route findRoute(Coordinate start, Coordinate end, WorldMap worldMap, List<Coordinate> allowedCoordinates) throws PathNotFoundException, InvalidCoordinateException, InvalidRouteException {

        LinkedStack<Coordinate> stack = new LinkedStack<>();
        List<Coordinate> visited = new ArrayList<>();
        List<Coordinate> discovered = new ArrayList<>();
        List<Coordinate> parentKeys = new ArrayList<>();
        List<Coordinate> parentValues = new ArrayList<>();
        List<Coordinate> neighbours;
        Coordinate neighbour;
        Route foundRoute;

        validateInputs(start, end, worldMap, allowedCoordinates);


        Coordinate current = start;
        discovered.add(start);

        while (!sameCoordinate(current, end)) {
            if (!containsCoordinate(visited, current)) {
                visited.add(current);
            }

            neighbours = getNeighbours(current);
            int index = 0;

            while (index < neighbours.size()) {
                neighbour = neighbours.get(index);

                if (isVisitable(neighbour, worldMap, allowedCoordinates, visited, discovered)) {
                    stack.push(neighbour);
                    discovered.add(neighbour);
                    addParent(neighbour, current, parentKeys, parentValues);
                }

                index++;
            }

            if (stack.isEmpty()) {
                throw new PathNotFoundException();
            }

            current = stack.pop();
        }

        foundRoute = reconstructRoute(start, end, parentKeys, parentValues);

        return foundRoute;
    }

    private void validateInputs(Coordinate start, Coordinate end, WorldMap worldMap, List<Coordinate> allowedCoordinates) throws PathNotFoundException, InvalidCoordinateException {
        if (start == null) {
            throw new PathNotFoundException();
        }

        if (end == null) {
            throw new PathNotFoundException();
        }

        if (worldMap == null) {
            throw new PathNotFoundException();
        }

        if (allowedCoordinates == null) {
            throw new PathNotFoundException();
        }

        try {
            if (!worldMap.isValidCoordinate(start)) {
                throw new PathNotFoundException();
            }

            if (!worldMap.isValidCoordinate(end)) {
                throw new PathNotFoundException();
            }

            if (worldMap.isObstacle(start)) {
                throw new PathNotFoundException();
            }

            if (worldMap.isObstacle(end)) {
                throw new PathNotFoundException();
            }
        } catch (InvalidCoordinateException e) {
            throw new PathNotFoundException();
        }

        if (!containsCoordinate(allowedCoordinates, start)) {
            throw new PathNotFoundException();
        }

        if (!containsCoordinate(allowedCoordinates, end)) {
            throw new PathNotFoundException();
        }
    }

    private List<Coordinate> getNeighbours(Coordinate coordinate) {
        List<Coordinate> neighbours;

        neighbours = new ArrayList<>();
        neighbours.add(new Coordinate(coordinate.getX(), coordinate.getY() - 1));
        neighbours.add(new Coordinate(coordinate.getX() + 1, coordinate.getY()));
        neighbours.add(new Coordinate(coordinate.getX(), coordinate.getY() + 1));
        neighbours.add(new Coordinate(coordinate.getX() - 1, coordinate.getY()));

        return neighbours;
    }

    private boolean isVisitable(Coordinate coordinate, WorldMap worldMap, List<Coordinate> allowedCoordinates, List<Coordinate> visited, List<Coordinate> discovered) throws InvalidCoordinateException {
        boolean visitable;

        visitable = coordinate != null;

        try {
            if (visitable && !worldMap.isValidCoordinate(coordinate)) {
                visitable = false;
            }

            if (visitable && worldMap.isObstacle(coordinate)) {
                visitable = false;
            }
        } catch (InvalidCoordinateException e) {
            visitable = false;
        }

        if (visitable && !containsCoordinate(allowedCoordinates, coordinate)) {
            visitable = false;
        }

        if (visitable && containsCoordinate(visited, coordinate)) {
            visitable = false;
        }

        if (visitable && containsCoordinate(discovered, coordinate)) {
            visitable = false;
        }


        return visitable;
    }

    private boolean containsCoordinate(List<Coordinate> coordinates, Coordinate target) {
        boolean found = false;
        int index = 0;


        while (index < coordinates.size() && !found) {
            if (sameCoordinate(coordinates.get(index), target)) {
                found = true;
            } else {
                index++;
            }
        }

        return found;
    }

    private boolean sameCoordinate(Coordinate first, Coordinate second) {
        boolean same = false;


        if (first != null && second != null) {
            if (first.getX() == second.getX() && first.getY() == second.getY()) {
                same = true;
            }
        }

        return same;
    }

    private void addParent(Coordinate child, Coordinate parent, List<Coordinate> parentKeys, List<Coordinate> parentValues) {
        parentKeys.add(child);
        parentValues.add(parent);
    }

    private Coordinate getParent(Coordinate child, List<Coordinate> parentKeys, List<Coordinate> parentValues) {
        Coordinate parent = null;
        boolean found = false;
        int index = 0;


        while (index < parentKeys.size() && !found) {
            if (sameCoordinate(parentKeys.get(index), child)) {
                parent = parentValues.get(index);
                found = true;
            } else {
                index++;
            }
        }

        return parent;
    }

    private Route reconstructRoute(Coordinate start, Coordinate end, List<Coordinate> parentKeys,
                                   List<Coordinate> parentValues) throws PathNotFoundException, InvalidCoordinateException, InvalidRouteException {
        List<Coordinate> reversePath = new ArrayList<>();
        Coordinate current = end;
        Route route = new Route();
        int index;

        reversePath.add(current);

        while (!sameCoordinate(current, start)) {
            current = getParent(current, parentKeys, parentValues);

            if (current == null) {
                throw new PathNotFoundException();
            }

            reversePath.add(current);
        }

        index = reversePath.size() - 1;

        while (index >= 0) {
            try {
                route.addCoordinate(reversePath.get(index));
            } catch (InvalidCoordinateException | InvalidRouteException e) {
                throw new PathNotFoundException();
            }

            index--;
        }

        return route;
    }
}