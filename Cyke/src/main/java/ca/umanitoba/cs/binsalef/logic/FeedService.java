package ca.umanitoba.cs.binsalef.logic;

import ca.umanitoba.cs.binsalef.logic.exceptions.EmptyProfileException;
import ca.umanitoba.cs.binsalef.model.Coordinate;
import ca.umanitoba.cs.binsalef.model.ExerciseSession;
import ca.umanitoba.cs.binsalef.model.Profile;
import ca.umanitoba.cs.binsalef.model.Route;


import java.util.ArrayList;
import java.util.List;

public class FeedService {


    public List<FeedItem> buildFeed(Profile profile) throws EmptyProfileException {
        List<FeedItem> feedList = new ArrayList<>();
        List<ExerciseSession> currentSessions;
        List<Profile> followingProfiles;
        List<ExerciseSession> followedSessions;
        Profile followedProfile;
        int index;
        int sessionIndex;

        validateProfile(profile);

        currentSessions = profile.getSessions();
        index = 0;
        while (index < currentSessions.size()) {
            feedList.add(new FeedItem(profile, currentSessions.get(index)));
            index++;
        }
        followingProfiles = profile.getFollowingList();
        index = 0;
        while (index < followingProfiles.size()) {
            followedProfile = followingProfiles.get(index);
            followedSessions = followedProfile.getSessions();
            sessionIndex = 0;

            while (sessionIndex < followedSessions.size()) {
                feedList.add(new FeedItem(followedProfile, followedSessions.get(sessionIndex)));
                sessionIndex++;
            }
            index++;
        }


        return feedList;
    }

    public List<Coordinate> buildMyCoordinates(Profile profile) throws EmptyProfileException {
        List<Coordinate> myCoordinates = new ArrayList<>();
        List<ExerciseSession> mySessions;
        Route currentRoute;
        List<Coordinate> routeCoordinates;
        int sessionIndex;
        int coordinateIndex;

        validateProfile(profile);
        mySessions = profile.getSessions();
        sessionIndex = 0;

        while (sessionIndex < mySessions.size()) {
            currentRoute = mySessions.get(sessionIndex).getRoute();
            routeCoordinates = currentRoute.getCoordinates();
            coordinateIndex = 0;

            while (coordinateIndex < routeCoordinates.size()) {
                addUniqueCoordinate(myCoordinates, routeCoordinates.get(coordinateIndex));
                coordinateIndex++;
            }
            sessionIndex++;
        }

        return myCoordinates;
    }

    public List<Coordinate> buildFeedCoordinates(Profile profile) throws EmptyProfileException {
        List<Coordinate> feedCoordinates = new ArrayList<>();
        List<FeedItem> feedItems;
        Route currentRoute;
        List<Coordinate> routeCoordinates;
        int sessionIndex = 0;
        int coordinateIndex;

        validateProfile(profile);

        feedItems = buildFeed(profile);

        while (sessionIndex < feedItems.size()) {
            currentRoute = feedItems.get(sessionIndex).getSession().getRoute();
            routeCoordinates = currentRoute.getCoordinates();
            coordinateIndex = 0;

            while (coordinateIndex < routeCoordinates.size()) {
                addUniqueCoordinate(feedCoordinates, routeCoordinates.get(coordinateIndex));
                coordinateIndex++;
            }
            sessionIndex++;
        }
        return feedCoordinates;
    }

    private void validateProfile(Profile profile) throws EmptyProfileException {
        if (profile == null) {
            throw new EmptyProfileException();
        }
    }

    private void addUniqueCoordinate(List<Coordinate> CoordinateList, Coordinate coordinate) {
        if (!containsCoordinate(CoordinateList, coordinate)) {
            CoordinateList.add(coordinate);
        }

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
            same = first.getX() == second.getX() && first.getY() == second.getY();
        }
        return same;
    }

}
