# Cyke!

This is the README for an Exercise Tracker called Cyke!

A command-line exercise tracking application focused on cycling, supporting profile management, activity tracking, route finding, and persistence.


## Running the program

* The functional application can be started by running the `main` method in
  `Main.java`.
* All tests can be run by running the `main` method in `TestHarness.java`
  in the test folder.

## Features

- Profile management (create, select, rename)
- Activity tracking with routes and statistics
- Social feed system (follow/unfollow users)
- Route finding using stack-based pathfinding
- JSON-based persistence (save/load application state)
- Modular architecture (model, logic, UI layers)
- Comprehensive automated test suite



## Domain Model

```mermaid 
classDiagram

class Cyke{
    -WorldMap worldMap
    -List~Profile~ profiles
    
    +addProfile(Profile profile): void
    +getProfile(String username): Profile
    +selectProfile(String username): Profile
    +renameProfile(String oldUsername, newUsername): void
}

note for Cyke "Invariants:
    *worldMap!=null
    *profiles !=null
    *all profile usernames are unique
"

class Profile{
    -String username
    -List~ExerciseSession~ sessions
    -List~Profile~ followingList
    
    +addSession(ExerciseSession session): void
    +removeSession(int sessionID): void
    +getSession(sessionID): ExerciseSession
    +follow(Profile profile): void
    +unfollow(Profile profile): void
    +isFollowing(Profile profile): boolean
    +setUsername(String newName): void
} 
 note for Profile "Invariants:
    *username!=null and username is not empty
    *sessions !=null
    *followingList !=null
    *session IDs are unique in a profile
    *Profile cant follow itself
    *No duplicates in followingList
    "

class ExerciseSession {
  -int sessionID
  -Route route
  -int minutes
  -int calories



}

note for ExerciseSession "Invariants:
  *sessionID >=1
  *route!=null
  *route.coordinate.size >=1


"
class Route {
  -List~Coordinate~ coordinates

  +addCoordinate(Coordinate): void
  
}

note for Route "Invariants:
  *coordinates!=null
  *route is continuous:
    each next coordinate is exactly 1 step away
    horizontally OR vertically (there cant be any jumps)
  *coordinates.size()>=1
  *all coordinates are within worldMap bounds
  *route does not pass through obstacles  

"

class Coordinate {
  -int x
  -int y
}

class WorldMap{
  -int width
  -int height
  -List~Coordinate~ obstacles
    
  +isValidCoordinate(Coordinate): boolean 
  +isObstacle(Coordinate): boolean
  +addObstacle(Coordinate): void
  +removeObstacle(index): void
  
}

note for WorldMap "invariants
   
  *width>=1
  *height>=1
  *obstacles!=null;
  *obstacle coordinate is within bounds:
  0<=x<width AND 0<=y<height
"

class Stack ~Coordinate~ {
    <<interface>>
    +push(Coordinate coord ): void
    +pop() : Coordinate
    +peek(): Coordinate
    +size(): int 
    +isEmpty(): boolean
}
   
note for Stack "Preconditions / Postconditions/ Invariants:
  *push(Coordinate coord): coord != null
  *pop(): pre !isEmpty(), post size decreases by 1
  *peek(): pre !isEmpty(), post size unchanged
  *size(): result >= 0
  *isEmpty(): result == (size == 0)
"

class LinkedStack ~Coordinate~{
  -StackNode top
  -int size

  +push(Coordinate coord) void
  +pop(): Coordinate
  +peek(): Coordinate
  +size(): int
  +isEmpty(): boolean
}

note for LinkedStack "Invariants:
  *size >= 0
  *size == 0 iff top == null
  *size > 0 iff top != null
"

class StackNode ~Coordinate~{
  -Coordinate data
  -StackNode next
}

note for StackNode "Invariants:
  *data != null
"


Cyke --* WorldMap
Cyke --o Profile
Profile --* ExerciseSession
ExerciseSession --* Route
Route --* Coordinate   
WorldMap --o Coordinate


LinkedStack --* StackNode
LinkedStack ..|> Stack

```

## Flows of Interaction(s)

### Profiles

```mermaid
flowchart
    subgraph Profiles
        direction TB
        profileMenu[[Profile menu]]
        createProfile[Create Profile]
        selectProfile[Select Profile]
        editProfile[Edit Profile]
        saveProfile{Save profile}
        endProfiles[[Profiles complete]]
        profileMenu == Create new profile ==> createProfile
        createProfile -. Invalid or duplicate information .-> profileMenu
        createProfile -. Correct information .-> saveProfile
        saveProfile -. Profile created .-> endProfiles
        selectProfile -. Profile selected .-> endProfiles
        selectProfile -. Invalid or cancelled action .-> profileMenu
        profileMenu == Edit existing profile ==> editProfile
        editProfile -. Invalid changes entered .-> profileMenu
        editProfile -. Valid changes entered .-> saveProfile
    end
```

### Feed & Follow Profiles

```mermaid
flowchart
    subgraph Feed
        direction TB
        feedMenu[[Feed]]
        viewActivity[View Activity]
        followProfile[Follow Profile]
        updateFollowing{Update Following List}
        viewFeed{Build and Show Feed}
        unfollowProfile[Unfollow Profile]
        endFeed[[Feed Completed]]
        feedMenu == Open Feed ==> viewFeed
        viewFeed -. My activities and followed activities shown .-> feedMenu
        feedMenu == View activity ==> viewActivity
        viewActivity -. Invalid or cancelled activity .-> feedMenu
        viewActivity -. Activity details .-> feedMenu
        feedMenu == Follow profile ==> followProfile
        followProfile -. Invalid or duplicate .-> feedMenu
        followProfile -. Valid profile .-> updateFollowing
        updateFollowing -. Following list updated .-> feedMenu
        feedMenu == Unfollow profile ==> unfollowProfile
        unfollowProfile -. Invalid or not currently followed .-> feedMenu
        unfollowProfile -. Valid profile .-> updateFollowing
        updateFollowing -. Following list updated .-> feedMenu
        feedMenu == Exit feed ==> endFeed
    end


```

### Add Activity

```mermaid
flowchart
    subgraph AddActivity
        direction TB
        addMenu[[Add Activity]]
        enterManualRoute[Enter Manual Route]
        duplicateRoute[Duplicate Route]
        processRoute{Process Route}
        recordObstacle[Record Obstacle]
        saveActivity{Save Activity}
        endAdd[[Activity Added]]
        addMenu == Enter manual route ==> enterManualRoute
        enterManualRoute -. Route entered .-> processRoute
        processRoute -. Invalid route .-> addMenu
        processRoute -. Valid route .-> recordObstacle
        addMenu == Duplicate route ==> duplicateRoute
        duplicateRoute -. Invalid selection .-> addMenu
        duplicateRoute -. Route copied from previous activity .-> recordObstacle
        recordObstacle == Add obstacle ==> recordObstacle
        recordObstacle -. Invalid obstacle coordinate .-> recordObstacle
        recordObstacle == Skip obstacle ==> saveActivity
        saveActivity -. Activity saved to profile .-> endAdd

    end
```

### Find New Route

```mermaid
flowchart
    subgraph FindRoute
        direction TB
        routeMenu[[Find Route]]
        findUsingOwnRoutes{Find Route Using Own Routes}
        findUsingFeedRoutes{Find Route using followed routes in Feed}
        saveRoute{Save Found Route}
        endRoute[[Route Finding complete]]
        routeMenu == Find route with own routes ==> findUsingOwnRoutes
        findUsingOwnRoutes -. Invalid or out of bounds .-> routeMenu
        findUsingOwnRoutes -. No route found .-> routeMenu
        findUsingOwnRoutes -. Route found and shown .-> saveRoute
        routeMenu == Find route with feed routes ==> findUsingFeedRoutes
        findUsingFeedRoutes -. Invalid or out of bounds .-> routeMenu
        findUsingFeedRoutes -. No route found .-> routeMenu
        findUsingFeedRoutes -. Route found and shown .-> saveRoute
        saveRoute == Save as activity ==> endRoute
        saveRoute == Do not save ==> endRoute
    end

```



