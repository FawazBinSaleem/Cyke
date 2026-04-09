package ca.umanitoba.cs.binsalef.model;


import ca.umanitoba.cs.binsalef.model.exceptions.InvalidFollowException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidNameException;
import ca.umanitoba.cs.binsalef.model.exceptions.InvalidSessionException;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class Profile {

    private String username;


    private List<ExerciseSession> sessions;
    private List<Profile> followingList;

    public Profile(String username) throws InvalidNameException {
        this.username = username;
        this.sessions = new ArrayList<>();
        this.followingList = new ArrayList<>();

        checkProfile();

    }

    public static class ProfileBuilder {
        private String username;

        public ProfileBuilder() {
            username = null;
        }

        public ProfileBuilder username(String username) throws InvalidNameException {
            if (username != null && !username.isBlank()) {
                this.username = username;
            } else {
                throw new InvalidNameException();
            }

            return this;
        }

        public Profile build() throws InvalidNameException {
            return new Profile(username);
        }


    }

    private void checkProfile() {
        Preconditions.checkNotNull(username, "username must not be null");
        Preconditions.checkState(!username.isBlank(), "username must not be blank");
        Preconditions.checkNotNull(sessions, "sessions must not be null");
        Preconditions.checkNotNull(followingList, "following list must not be null");
    }

    public String getUsername() {
        return username;
    }

    public List<ExerciseSession> getSessions() {
        return sessions;
    }

    public List<Profile> getFollowingList() {
        return followingList;
    }

    public void setUsername(String newName) throws InvalidNameException {
        String trimmedName; // used to prevent something like "  Fawaz"
        if (newName == null) {
            throw new InvalidNameException();
        }
        trimmedName = newName.trim();
        if (trimmedName.isEmpty()) {
            throw new InvalidNameException();
        }
        username = trimmedName;

    }

    public void addSession(ExerciseSession session) throws InvalidSessionException {
        boolean duplicate;
        int index;
        int newSessionID;

        if (session == null) {
            throw new InvalidSessionException();
        }
        duplicate = false;
        index = 0;
        newSessionID = session.getSessionID();
        while (index < sessions.size() && !duplicate) {
            if (sessions.get(index).getSessionID() == newSessionID) {
                duplicate = true;
            } else {
                index++;
            }
        }
        if (duplicate) {
            throw new InvalidSessionException();
        }
        sessions.add(session);
    }


    public ExerciseSession getSession(int sessionID) throws InvalidSessionException {
        ExerciseSession currentSession = null;
        boolean found = false;
        int index = 0;

        while (index < sessions.size() && !found) {
            if (sessions.get(index).getSessionID() == sessionID) {
                found = true;
                currentSession = sessions.get(index);
            } else {
                index++;
            }
        }
        if (!found) {
            throw new InvalidSessionException();
        }

        return currentSession;
    }

    public void follow(Profile profile) throws InvalidFollowException {
        if (profile == null) {
            throw new InvalidFollowException();
        }
        if (username.equals((profile.getUsername()))) {
            throw new InvalidFollowException();
        }
        if (isFollowing(profile)) {
            throw new InvalidFollowException();
        }
        followingList.add(profile);
    }

    public void unfollow(Profile profile) throws InvalidFollowException {
        boolean found = false;
        int index = 0;
        int indexToRemove = -1;

        if (profile == null) {
            throw new InvalidFollowException();
        }
        while (index < followingList.size() && !found) {
            if (followingList.get(index).getUsername().equals(profile.getUsername())) {
                found = true;
                indexToRemove = index;
            } else {
                index++;
            }
        }
        if (!found) {
            throw new InvalidFollowException();
        }
        followingList.remove(indexToRemove);
    }

    public boolean isFollowing(Profile profile) {
        boolean following = false;
        int index = 0;

        if (profile != null) {
            while (index < followingList.size() && !following) {
                if (followingList.get(index).getUsername().equals(profile.getUsername())) {
                    following = true;
                } else {
                    index++;
                }
            }
        }

        return following;
    }

    public int getNumberOfSessions() {
        return sessions.size();
    }

    public int getNumberFollowing() {
        return followingList.size();
    }

    public String toString() {
        return "Username= " + username + " , Sessions= " + sessions.size()
                + " , Following= " + followingList.size();
    }
}