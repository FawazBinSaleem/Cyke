package ca.umanitoba.cs.binsalef.model;

import ca.umanitoba.cs.binsalef.model.exceptions.DuplicateProfileException;
import ca.umanitoba.cs.binsalef.model.exceptions.MapNotFoundException;
import ca.umanitoba.cs.binsalef.model.exceptions.ProfileNotFoundException;
import ca.umanitoba.cs.binsalef.tests.TestResults;
import ca.umanitoba.cs.binsalef.tests.TestSuite;

public class TestCyke implements TestSuite {

    @Override
    public String name() {
        return "TestCyke";
    }

    @Override
    public TestResults runTests() {
        TestResults results = new TestResults();

        testRejectNullWorldMap(results);
        testAddProfile(results);
        testFirstProfileBecomesCurrent(results);
        testRejectDuplicateProfile(results);
        testGetProfile(results);
        testRejectMissingProfile(results);
        testSelectProfile(results);
        testRenameProfile(results);
        testRejectRenameToDuplicate(results);

        return results;
    }

    private void testRejectNullWorldMap(TestResults results) {
        try {
            new Cyke(null);
            results.fail("Cyke accepted a null world map.");
        } catch (MapNotFoundException e) {
            results.pass("Cyke rejects a null world map.");
        } catch (Exception e) {
            results.fail("Cyke threw the wrong exception for a null world map.");
        }
    }

    private void testAddProfile(TestResults results) {
        try {
            Cyke cyke = new Cyke(new WorldMap(5, 5));
            Profile profile = new Profile.ProfileBuilder().username("Fawaz").build();

            cyke.addProfile(profile);

            if (cyke.getProfiles().contains(profile)) {
                results.pass("Cyke adds profiles correctly.");
            } else {
                results.fail("Cyke did not add profile correctly.");
            }
        } catch (Exception e) {
            results.fail("testAddProfile() threw an unknown exception.");
        }
    }

    private void testFirstProfileBecomesCurrent(TestResults results) {
        try {
            Cyke cyke = new Cyke(new WorldMap(5, 5));
            Profile profile = new Profile.ProfileBuilder().username("Fawaz").build();

            cyke.addProfile(profile);

            if (cyke.getCurrentProfile() == profile) {
                results.pass("First added profile becomes current profile.");
            } else {
                results.fail("First added profile did not become current profile.");
            }
        } catch (Exception e) {
            results.fail("testFirstProfileBecomesCurrent() threw an unknown exception.");
        }
    }

    private void testRejectDuplicateProfile(TestResults results) {
        try {
            Cyke cyke = new Cyke(new WorldMap(5, 5));
            Profile profile1 = new Profile.ProfileBuilder().username("Fawaz").build();
            Profile profile2 = new Profile.ProfileBuilder().username("Fawaz").build();

            cyke.addProfile(profile1);
            cyke.addProfile(profile2);

            results.fail("Cyke accepted a duplicate profile username.");
        } catch (DuplicateProfileException e) {
            results.pass("Cyke rejects duplicate profile usernames.");
        } catch (Exception e) {
            results.fail("Cyke threw the wrong exception for a duplicate profile.");
        }
    }

    private void testGetProfile(TestResults results) {
        try {
            Cyke cyke = new Cyke(new WorldMap(5, 5));
            Profile profile = new Profile.ProfileBuilder().username("Fawaz").build();

            cyke.addProfile(profile);

            if (cyke.getProfile("Fawaz") == profile) {
                results.pass("Cyke retrieves existing profiles correctly.");
            } else {
                results.fail("Cyke did not retrieve the correct profile.");
            }
        } catch (Exception e) {
            results.fail("testGetProfile() threw an unknown exception.");
        }
    }

    private void testRejectMissingProfile(TestResults results) {
        try {
            Cyke cyke = new Cyke(new WorldMap(5, 5));
            cyke.getProfile("Missing");

            results.fail("Cyke returned a missing profile.");
        } catch (ProfileNotFoundException e) {
            results.pass("Cyke rejects missing profiles.");
        } catch (Exception e) {
            results.fail("Cyke threw the wrong exception for a missing profile.");
        }
    }

    private void testSelectProfile(TestResults results) {
        try {
            Cyke cyke = new Cyke(new WorldMap(5, 5));
            Profile profile1 = new Profile.ProfileBuilder().username("A").build();
            Profile profile2 = new Profile.ProfileBuilder().username("B").build();

            cyke.addProfile(profile1);
            cyke.addProfile(profile2);
            cyke.selectProfile("B");

            if (cyke.getCurrentProfile() == profile2) {
                results.pass("Cyke selects profiles correctly.");
            } else {
                results.fail("Cyke did not select the correct profile.");
            }
        } catch (Exception e) {
            results.fail("testSelectProfile() threw an unexpected exception.");
        }
    }

    private void testRenameProfile(TestResults results) {
        try {
            Cyke cyke = new Cyke(new WorldMap(5, 5));
            Profile profile = new Profile.ProfileBuilder().username("OldName").build();

            cyke.addProfile(profile);
            cyke.renameProfile("OldName", "NewName");

            if (cyke.getProfile("NewName") == profile && profile.getUsername().equals("NewName")) {
                results.pass("Cyke renames profiles correctly.");
            } else {
                results.fail("Cyke did not rename profile correctly.");
            }
        } catch (Exception e) {
            results.fail("testRenameProfile() threw an unknown exception.");
        }
    }

    private void testRejectRenameToDuplicate(TestResults results) {
        try {
            Cyke cyke = new Cyke(new WorldMap(5, 5));
            Profile profile1 = new Profile.ProfileBuilder().username("A").build();
            Profile profile2 = new Profile.ProfileBuilder().username("B").build();

            cyke.addProfile(profile1);
            cyke.addProfile(profile2);
            cyke.renameProfile("A", "B");

            results.fail("Cyke allowed rename to a duplicate username.");
        } catch (DuplicateProfileException e) {
            results.pass("Cyke rejects renaming to a duplicate username.");
        } catch (Exception e) {
            results.fail("Cyke threw the wrong exception for duplicate rename.");
        }
    }
}