package ca.umanitoba.cs.binsalef.model;

import ca.umanitoba.cs.binsalef.model.exceptions.*;


import java.util.ArrayList;
import java.util.List;

public class Cyke {

    private WorldMap worldMap;
    private List<Profile> profiles;
    private Profile currentProfile;

    public Cyke(WorldMap worldMap) throws MapNotFoundException {

        if (worldMap == null) {
            throw new MapNotFoundException();
        }
        this.worldMap = worldMap;
        profiles = new ArrayList<>();
        currentProfile = null;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }


    public void addProfile(Profile profile) throws InvalidNameException, DuplicateProfileException, InvalidSessionException {
        boolean duplicate = false;
        int index = 0;
        String trimmedUsername;
        Profile newProfile;

        if (profile == null) {
            throw new InvalidSessionException();

        }
        trimmedUsername = profile.getUsername();

        if (trimmedUsername == null) {
            throw new InvalidNameException();
        }
        while (index < profiles.size() && !duplicate) {
            if (profiles.get(index).getUsername().equals(trimmedUsername)) {
                duplicate = true;
            } else {
                index++;
            }
        }
        if (duplicate) {
            throw new DuplicateProfileException();

        }

        profiles.add(profile);
        if (currentProfile == null) {
            currentProfile = profile;
        }
    }

    public Profile getProfile(String username) throws ProfileNotFoundException, InvalidNameException {
        boolean found = false;
        int index = 0;
        Profile foundProfile = null;
        String trimmedUsername;

        if (username == null) {
            throw new InvalidNameException();

        }
        trimmedUsername = username.trim();

        if (trimmedUsername.isEmpty()) {
            throw new InvalidNameException();
        }
        while (index < profiles.size() && !found) {
            if (profiles.get(index).getUsername().equals(trimmedUsername)) {
                found = true;
                foundProfile = profiles.get(index);
            } else {
                index++;
            }
        }

        if (!found) {
            throw new ProfileNotFoundException();
        }

        return foundProfile;

    }

    public void selectProfile(String username) throws ProfileNotFoundException, InvalidNameException {
        Profile selectedProfile;

        selectedProfile = getProfile(username);
        currentProfile = selectedProfile;
    }

    public void renameProfile(String oldUsername, String newUsername) throws ProfileNotFoundException, InvalidNameException, DuplicateProfileException {
        Profile profileToRename;
        boolean duplicate = false;
        int index = 0;
        String trimmedNewUsername;

        if (newUsername == null) {
            throw new InvalidNameException();
        }
        trimmedNewUsername = newUsername.trim();

        if (trimmedNewUsername.isEmpty()) {
            throw new InvalidNameException();
        }

        profileToRename = getProfile(oldUsername);

        while (index < profiles.size() && !duplicate) {
            if (profiles.get(index).getUsername().equals(trimmedNewUsername) && profiles.get(index) != profileToRename) {
                duplicate = true;
            } else {
                index++;
            }


        }
        if (duplicate) {
            throw new DuplicateProfileException();
        }

        profileToRename.setUsername(trimmedNewUsername);

    }


    public String toString() {
        String currentProfileName;
        if (currentProfile == null) {
            currentProfileName = "none";
        } else {
            currentProfileName = currentProfile.getUsername();
        }
        return "CurrentProfile = " + currentProfileName;
    }

}
