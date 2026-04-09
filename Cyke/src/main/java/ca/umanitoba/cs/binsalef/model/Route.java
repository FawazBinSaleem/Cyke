package ca.umanitoba.cs.binsalef.model;

import ca.umanitoba.cs.binsalef.model.exceptions.InvalidCoordinateException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidRouteException;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;


public class Route {
    private final List<Coordinate> coordinates;

    public Route() {
        this.coordinates = new ArrayList<>();
    }

    public void addCoordinate(Coordinate coordinate) throws InvalidRouteException, InvalidCoordinateException {

        if (coordinate == null) {
            throw new InvalidCoordinateException();
        }

        Preconditions.checkNotNull(coordinates, "Coordinate list cant be null");

        if (!coordinates.isEmpty()) {
            Coordinate last = coordinates.get(coordinates.size() - 1);

            int deltaX = Math.abs(coordinate.getX() - last.getX());
            int deltaY = Math.abs(coordinate.getY() - last.getY());

            if (!((deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1))) {
                throw new InvalidRouteException();
            }

        }
        coordinates.add(coordinate);

    }

    public int getSize() {

        return coordinates.size();

    }

    public List<Coordinate> getCoordinates() {
        return coordinates;

    }
}
