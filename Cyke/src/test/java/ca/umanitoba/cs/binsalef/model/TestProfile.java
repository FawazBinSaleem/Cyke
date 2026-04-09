package ca.umanitoba.cs.binsalef.model;

import ca.umanitoba.cs.binsalef.model.exceptions.InvalidNameException;
import ca.umanitoba.cs.binsalef.tests.TestResults;
import ca.umanitoba.cs.binsalef.tests.TestSuite;

public class TestProfile implements TestSuite {

    @Override
    public String name() {
        return "TestProfile";
    }

    @Override
    public TestResults runTests() {
        TestResults results = new TestResults();

        testCreateValidProfile(results);
        testRejectNullUsername(results);
        testRejectEmptyUsername(results);
        testFollowValidProfile(results);
        testRejectFollowSelf(results);
        testRejectDuplicateFollow(results);
        testUnfollow(results);
        testUnfollowNotFollowed(results);

        return results;
    }

    private void testCreateValidProfile(TestResults results) {
        try {
            Profile.ProfileBuilder builder = new Profile.ProfileBuilder();
            builder.username("Fawaz");
            Profile p = builder.build();

            if (p.getUsername().equals("Fawaz")) {
                results.pass("Valid profile created successfully.");
            } else {
                results.fail("Username does not match.");
            }
        } catch (Exception e) {
            results.fail("Unknown exception thrown for testCreateValidProfile().");
        }
    }

    private void testRejectNullUsername(TestResults results) {
        try {
            Profile.ProfileBuilder builder = new Profile.ProfileBuilder();
            builder.username(null);

            results.fail("Accepted a null username.");
        } catch (InvalidNameException e) {
            results.pass("Rejected a null username.");
        } catch (Exception e) {
            results.fail("Unknown exception thrown for testRejectNullUsername().");
        }
    }

    private void testRejectEmptyUsername(TestResults results) {
        try {
            Profile.ProfileBuilder builder = new Profile.ProfileBuilder();
            builder.username("");

            results.fail("Accepted an empty username.");
        } catch (InvalidNameException e) {
            results.pass("Rejected an empty username.");
        } catch (Exception e) {
            results.fail("Unknown exception thrown for testRejectEmptyUsername().");
        }
    }

    private void testFollowValidProfile(TestResults results) {
        try {
            Profile.ProfileBuilder builder1 = new Profile.ProfileBuilder();
            Profile.ProfileBuilder builder2 = new Profile.ProfileBuilder();

            builder1.username("A");
            builder2.username("B");

            Profile p1 = builder1.build();
            Profile p2 = builder2.build();

            p1.follow(p2);

            if (p1.getFollowingList().contains(p2)) {
                results.pass("Follow works correctly.");
            } else {
                results.fail("Follow did not add profile.");
            }
        } catch (Exception e) {
            results.fail("testFollowValidProfile() threw an unknown exception.");
        }
    }

    private void testRejectFollowSelf(TestResults results) {
        try {
            Profile.ProfileBuilder builder = new Profile.ProfileBuilder();
            builder.username("A");
            Profile p = builder.build();

            p.follow(p);

            results.fail("Allowed following  my own account.");
        } catch (Exception e) {
            results.pass("Rejected following my own account.");
        }
    }

    private void testRejectDuplicateFollow(TestResults results) {
        try {
            Profile.ProfileBuilder builder1 = new Profile.ProfileBuilder();
            Profile.ProfileBuilder builder2 = new Profile.ProfileBuilder();

            builder1.username("A");
            builder2.username("B");

            Profile p1 = builder1.build();
            Profile p2 = builder2.build();

            p1.follow(p2);
            p1.follow(p2);

            results.fail("Allowed duplicate follow.");
        } catch (Exception e) {
            results.pass("Rejected duplicate follow.");
        }
    }

    private void testUnfollow(TestResults results) {
        try {
            Profile.ProfileBuilder builder1 = new Profile.ProfileBuilder();
            Profile.ProfileBuilder builder2 = new Profile.ProfileBuilder();

            builder1.username("A");
            builder2.username("B");

            Profile p1 = builder1.build();
            Profile p2 = builder2.build();

            p1.follow(p2);
            p1.unfollow(p2);

            if (!p1.getFollowingList().contains(p2)) {
                results.pass("Unfollow works correctly.");
            } else {
                results.fail("Unfollow did not remove profile.");
            }
        } catch (Exception e) {
            results.fail("testUnfollow() threw an unknown exception.");
        }
    }

    private void testUnfollowNotFollowed(TestResults results) {
        try {
            Profile.ProfileBuilder builder1 = new Profile.ProfileBuilder();
            Profile.ProfileBuilder builder2 = new Profile.ProfileBuilder();

            builder1.username("A");
            builder2.username("B");

            Profile p1 = builder1.build();
            Profile p2 = builder2.build();

            p1.unfollow(p2);

            results.fail("Unfollowed a profile that was not followed.");
        } catch (Exception e) {
            results.pass("Rejected unfollow of a non followed profile.");
        }
    }
}