package ca.umanitoba.cs.binsalef.persistence.json;

import ca.umanitoba.cs.binsalef.model.Coordinate;
import ca.umanitoba.cs.binsalef.model.Cyke;
import ca.umanitoba.cs.binsalef.model.ExerciseSession;
import ca.umanitoba.cs.binsalef.model.Profile;
import ca.umanitoba.cs.binsalef.model.Route;
import ca.umanitoba.cs.binsalef.model.WorldMap;
import ca.umanitoba.cs.binsalef.model.exceptions.DuplicateProfileException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidCoordinateException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidFollowException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidNameException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidObstacleException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidRouteException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidSessionException;

import ca.umanitoba.cs.binsalef.model.exceptions.ProfileNotFoundException;
import ca.umanitoba.cs.binsalef.persistence.CykePersistence;
import ca.umanitoba.cs.binsalef.persistence.exceptions.PersistenceException;
import ca.umanitoba.cs.binsalef.persistence.exceptions.NotFoundException;
import com.google.common.base.Preconditions;

import javax.json.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class CykePersistenceJson implements CykePersistence {

    private final Path cykeStorage;

    public CykePersistenceJson(Path cykeStorage) {
        Preconditions.checkNotNull(cykeStorage, "Must pass a file reference!");
        this.cykeStorage = cykeStorage;

        checkCykePersistenceJson();
    }

    private void checkCykePersistenceJson() {
        Preconditions.checkNotNull(cykeStorage);
    }

    @Override
    public Cyke saveCyke(Cyke cyke) throws PersistenceException {
        Preconditions.checkNotNull(cyke);
        checkCykePersistenceJson();

        try {
            JsonWriter writer = Json.createWriter(Files.newOutputStream(cykeStorage));
            JsonObjectBuilder cykeJsonBuilder = Json.createObjectBuilder();

            cykeJsonBuilder.add("worldMap", worldMapToJson(cyke.getWorldMap()));
            cykeJsonBuilder.add("profiles", profilesToJson(cyke));

            if (cyke.getCurrentProfile() == null) {
                cykeJsonBuilder.add("currentProfile", "");
            } else {
                cykeJsonBuilder.add("currentProfile", cyke.getCurrentProfile().getUsername());
            }

            writer.writeObject(cykeJsonBuilder.build());
            writer.close();
        } catch (Exception e) {
            throw new PersistenceException();
        }

        return cyke;
    }

    @Override
    public Cyke loadCyke() throws NotFoundException, PersistenceException {
        checkCykePersistenceJson();

        if (!Files.exists(cykeStorage)) {
            throw new NotFoundException();
        }

        try {
            JsonReader reader = Json.createReader(Files.newInputStream(cykeStorage));
            JsonObject cykeJson = reader.readObject();
            reader.close();

            return cykeFromJson(cykeJson);
        } catch (Exception e) {
            throw new PersistenceException();
        }
    }

    private JsonObject worldMapToJson(WorldMap worldMap) {
        JsonObjectBuilder worldMapBuilder = Json.createObjectBuilder();
        JsonArrayBuilder obstaclesBuilder = Json.createArrayBuilder();
        int index = 0;

        worldMapBuilder.add("width", worldMap.getWidth());
        worldMapBuilder.add("height", worldMap.getHeight());

        while (index < worldMap.getObstacles().size()) {
            obstaclesBuilder.add(coordinateToJson(worldMap.getObstacles().get(index)));
            index++;
        }

        worldMapBuilder.add("obstacles", obstaclesBuilder.build());

        return worldMapBuilder.build();
    }

    private JsonObject profileToJson(Profile profile) {
        JsonObjectBuilder profileBuilder = Json.createObjectBuilder();
        JsonArrayBuilder followingBuilder = Json.createArrayBuilder();
        JsonArrayBuilder sessionsBuilder = Json.createArrayBuilder();
        int index = 0;

        profileBuilder.add("username", profile.getUsername());

        while (index < profile.getFollowingList().size()) {
            followingBuilder.add(profile.getFollowingList().get(index).getUsername());
            index++;
        }

        index = 0;
        while (index < profile.getSessions().size()) {
            sessionsBuilder.add(sessionToJson(profile.getSessions().get(index)));
            index++;
        }

        profileBuilder.add("following", followingBuilder.build());
        profileBuilder.add("sessions", sessionsBuilder.build());

        return profileBuilder.build();
    }

    private JsonArray profilesToJson(Cyke cyke) {
        JsonArrayBuilder profilesBuilder = Json.createArrayBuilder();
        int index = 0;

        while (index < cyke.getProfiles().size()) {
            profilesBuilder.add(profileToJson(cyke.getProfiles().get(index)));
            index++;
        }

        return profilesBuilder.build();
    }


    private JsonObject sessionToJson(ExerciseSession session) {
        JsonObjectBuilder sessionBuilder = Json.createObjectBuilder();
        JsonArrayBuilder routeBuilder = Json.createArrayBuilder();
        int index = 0;

        sessionBuilder.add("sessionID", session.getSessionID());
        sessionBuilder.add("minutes", session.getMinutes());
        sessionBuilder.add("calories", session.getCalories());

        while (index < session.getRoute().getCoordinates().size()) {
            routeBuilder.add(coordinateToJson(session.getRoute().getCoordinates().get(index)));
            index++;
        }

        sessionBuilder.add("route", routeBuilder.build());

        return sessionBuilder.build();
    }

    private JsonObject coordinateToJson(Coordinate coordinate) {
        JsonObjectBuilder coordinateBuilder = Json.createObjectBuilder();

        coordinateBuilder.add("x", coordinate.getX());
        coordinateBuilder.add("y", coordinate.getY());

        return coordinateBuilder.build();
    }

    private Cyke cykeFromJson(JsonObject cykeJson) throws Exception {
        JsonObject worldMapJson = cykeJson.getJsonObject("worldMap");
        JsonArray profilesJson = cykeJson.getJsonArray("profiles");
        Cyke cyke = new Cyke(worldMapFromJson(worldMapJson));

        String currentProfileName;


        loadProfiles(cyke, profilesJson);
        loadSessions(cyke, profilesJson);
        loadFollowing(cyke, profilesJson);

        currentProfileName = cykeJson.getString("currentProfile");
        if (!currentProfileName.isBlank()) {
            cyke.selectProfile(currentProfileName);
        }

        return cyke;
    }

    private WorldMap worldMapFromJson(JsonObject worldMapJson) throws InvalidCoordinateException, InvalidObstacleException {
        WorldMap worldMap = new WorldMap(worldMapJson.getInt("width"), worldMapJson.getInt("height"));
        JsonArray obstaclesJson = worldMapJson.getJsonArray("obstacles");

        JsonObject obstacleJson;
        int index = 0;


        while (index < obstaclesJson.size()) {
            obstacleJson = obstaclesJson.getJsonObject(index);

            worldMap.addObstacle(new Coordinate(
                    obstacleJson.getInt("x"),
                    obstacleJson.getInt("y")
            ));

            index++;
        }

        return worldMap;
    }

    private void loadProfiles(Cyke cyke, JsonArray profilesJson) throws InvalidNameException, DuplicateProfileException, InvalidSessionException {
        JsonObject profileJson;
        Profile profile;
        int index = 0;

        while (index < profilesJson.size()) {
            profileJson = profilesJson.getJsonObject(index);
            profile = buildProfile(profileJson);
            cyke.addProfile(profile);
            index++;
        }
    }

    private void loadSessions(Cyke cyke, JsonArray profilesJson) throws ProfileNotFoundException, InvalidNameException, InvalidSessionException, InvalidCoordinateException, InvalidRouteException {
        JsonObject profileJson;
        JsonArray sessionsJson;
        JsonObject sessionJson;
        Profile profile;
        ExerciseSession session;
        int profileIndex = 0;
        int sessionIndex;

        while (profileIndex < profilesJson.size()) {
            profileJson = profilesJson.getJsonObject(profileIndex);
            profile = cyke.getProfile(profileJson.getString("username"));
            sessionsJson = profileJson.getJsonArray("sessions");
            sessionIndex = 0;

            while (sessionIndex < sessionsJson.size()) {
                sessionJson = sessionsJson.getJsonObject(sessionIndex);
                session = buildSession(sessionJson);
                profile.addSession(session);
                sessionIndex++;
            }

            profileIndex++;
        }
    }

    private void loadFollowing(Cyke cyke, JsonArray profilesJson) throws ProfileNotFoundException, InvalidNameException, InvalidFollowException {
        JsonObject profileJson;
        JsonArray followingJson;
        Profile profile;
        Profile followedProfile;
        String followedUsername;
        int profileIndex = 0;
        int followingIndex;

        while (profileIndex < profilesJson.size()) {
            profileJson = profilesJson.getJsonObject(profileIndex);
            profile = cyke.getProfile(profileJson.getString("username"));
            followingJson = profileJson.getJsonArray("following");
            followingIndex = 0;

            while (followingIndex < followingJson.size()) {
                followedUsername = followingJson.getString(followingIndex);
                followedProfile = cyke.getProfile(followedUsername);
                profile.follow(followedProfile);
                followingIndex++;
            }

            profileIndex++;
        }
    }

    private Profile buildProfile(JsonObject profileJson) throws InvalidNameException {
        Profile.ProfileBuilder pb = new Profile.ProfileBuilder();

        setUsername(pb, profileJson);

        return pb.build();
    }

    private void setUsername(Profile.ProfileBuilder pb, JsonObject profileJson) throws InvalidNameException {
        pb.username(profileJson.getString("username"));
    }

    private ExerciseSession buildSession(JsonObject sessionJson) throws InvalidSessionException, InvalidRouteException, InvalidCoordinateException {
        ExerciseSession.ExerciseSessionBuilder sb = new ExerciseSession.ExerciseSessionBuilder();

        setSessionID(sb, sessionJson);
        setMinutes(sb, sessionJson);
        setCalories(sb, sessionJson);
        setRoute(sb, sessionJson);

        return sb.build();
    }

    private void setSessionID(ExerciseSession.ExerciseSessionBuilder sb, JsonObject sessionJson) throws InvalidSessionException {
        sb.sessionID(sessionJson.getInt("sessionID"));
    }

    private void setMinutes(ExerciseSession.ExerciseSessionBuilder sb, JsonObject sessionJson) throws InvalidSessionException {
        sb.minutes(sessionJson.getInt("minutes"));
    }

    private void setCalories(ExerciseSession.ExerciseSessionBuilder sb, JsonObject sessionJson) throws InvalidSessionException {
        sb.calories(sessionJson.getInt("calories"));
    }

    private void setRoute(ExerciseSession.ExerciseSessionBuilder sb, JsonObject sessionJson) throws InvalidCoordinateException, InvalidRouteException {
        JsonArray routeJson = sessionJson.getJsonArray("route");
        Route route = new Route();
        JsonObject coordinateJson;
        int index = 0;


        while (index < routeJson.size()) {
            coordinateJson = routeJson.getJsonObject(index);

            route.addCoordinate(new Coordinate(
                    coordinateJson.getInt("x"),
                    coordinateJson.getInt("y")
            ));

            index++;
        }

        sb.route(route);
    }
}