package ca.umanitoba.cs.binsalef.logic;

import ca.umanitoba.cs.binsalef.logic.exceptions.*;
import ca.umanitoba.cs.binsalef.model.*;
import ca.umanitoba.cs.binsalef.model.exceptions.*;
import ca.umanitoba.cs.binsalef.persistence.CykePersistence;
import ca.umanitoba.cs.binsalef.persistence.exceptions.PersistenceException;

import java.util.List;

public class CykeService {
    private Cyke cyke;
    private FeedService feedService;
    private PathFinder pathFinder;
    private CykePersistence persistence;

    public CykeService(Cyke cyke, CykePersistence persistence) throws InvalidSystemException {
        if (cyke == null) {
            throw new InvalidSystemException();
        }

        this.cyke = cyke;
        this.feedService = new FeedService();
        this.pathFinder = new PathFinder();
        this.persistence = persistence;
    }

    public WorldMap getWorldMap() {
        return cyke.getWorldMap();
    }

    public void createProfile(Profile profile) throws InvalidNameException, DuplicateProfileException, InvalidSessionException, PersistenceException {
        cyke.addProfile(profile);
        persistState();
    }

    public void selectProfile(String username) throws ProfileNotFoundException, InvalidNameException {
        cyke.selectProfile(username);
    }

    public void renameProfile(String oldUsername, String newUsername)
            throws InvalidNameException, DuplicateProfileException, ProfileNotFoundException, PersistenceException {
        cyke.renameProfile(oldUsername, newUsername);
        persistState();
    }

    public Profile getCurrentProfile() {
        return cyke.getCurrentProfile();
    }

    public List<Profile> getProfiles() {
        return cyke.getProfiles();
    }

    public void followProfile(String usernameToFollow)
            throws ProfileNotFoundException, InvalidNameException, InvalidFollowException, NoCurrentProfileException, PersistenceException {
        Profile currentProfile = requireCurrentProfile();
        Profile targetProfile = cyke.getProfile(usernameToFollow);

        currentProfile.follow(targetProfile);
        persistState();
    }

    public void unfollowProfile(String usernameToUnfollow)
            throws ProfileNotFoundException, InvalidNameException, InvalidFollowException, NoCurrentProfileException {
        Profile currentProfile = requireCurrentProfile();
        Profile targetProfile = cyke.getProfile(usernameToUnfollow);

        currentProfile.unfollow(targetProfile);
    }

    public List<FeedItem> getFeed() throws EmptyProfileException, NoCurrentProfileException {
        Profile currentProfile = requireCurrentProfile();
        return feedService.buildFeed(currentProfile);
    }


    public void addActivity(ExerciseSession session) throws NoCurrentProfileException, InvalidSessionException, PersistenceException {
        Profile currentProfile = requireCurrentProfile();
        currentProfile.addSession(session);
        persistState();
    }


    public void addObstacle(Coordinate coordinate)
            throws InvalidCoordinateException, InvalidObstacleException {
        cyke.getWorldMap().addObstacle(coordinate);
    }

    public void removeObstacle(int index) throws InvalidObstacleException {
        cyke.getWorldMap().removeObstacle(index);
    }

    public Route getCopiedRoute(int oldSessionID)
            throws NoCurrentProfileException, InvalidSessionException,
            NoRouteException, InvalidCoordinateException, InvalidRouteException {

        Profile currentProfile = requireCurrentProfile();
        ExerciseSession oldSession = currentProfile.getSession(oldSessionID);

        return copyRoute(oldSession.getRoute());
    }

    public Route findRouteUsingMyRoutes(Coordinate start, Coordinate end)
            throws EmptyProfileException, PathNotFoundException,
            NoCurrentProfileException, InvalidCoordinateException, InvalidRouteException {

        Profile currentProfile = requireCurrentProfile();
        List<Coordinate> allowedCoordinates;

        allowedCoordinates = feedService.buildMyCoordinates(currentProfile);

        return pathFinder.findRoute(start, end, cyke.getWorldMap(), allowedCoordinates);
    }

    public Route findRouteUsingFeedRoutes(Coordinate start, Coordinate end)
            throws EmptyProfileException, PathNotFoundException,
            NoCurrentProfileException, InvalidCoordinateException, InvalidRouteException {

        Profile currentProfile = requireCurrentProfile();
        List<Coordinate> allowedCoordinates;

        allowedCoordinates = feedService.buildFeedCoordinates(currentProfile);

        return pathFinder.findRoute(start, end, cyke.getWorldMap(), allowedCoordinates);
    }

    public void saveFoundRouteAsActivity(ExerciseSession session) throws NoCurrentProfileException, InvalidSessionException, PersistenceException {
        Profile currentProfile;

        currentProfile = requireCurrentProfile();
        currentProfile.addSession(session);
        persistState();
    }


    private Profile requireCurrentProfile() throws NoCurrentProfileException {
        Profile currentProfile = cyke.getCurrentProfile();

        if (currentProfile == null) {
            throw new NoCurrentProfileException();
        }

        return currentProfile;
    }

    private Route copyRoute(Route originalRoute)
            throws NoRouteException, InvalidCoordinateException, InvalidRouteException {

        Route copiedRoute;
        List<Coordinate> originalCoordinates;
        int index;

        if (originalRoute == null) {
            throw new NoRouteException();
        }

        copiedRoute = new Route();
        originalCoordinates = originalRoute.getCoordinates();
        index = 0;

        while (index < originalCoordinates.size()) {
            copiedRoute.addCoordinate(originalCoordinates.get(index));
            index++;
        }

        return copiedRoute;
    }

    private void persistState() throws PersistenceException {
        if (persistence != null) {
            persistence.saveCyke(cyke);
        }
    }

}