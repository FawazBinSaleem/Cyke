package ca.umanitoba.cs.binsalef.tests;

import ca.umanitoba.cs.binsalef.logic.TestCykeService;
import ca.umanitoba.cs.binsalef.logic.TestFeedService;
import ca.umanitoba.cs.binsalef.logic.TestPathFinder;
import ca.umanitoba.cs.binsalef.model.*;

import ca.umanitoba.cs.binsalef.stack.TestStack;
import ca.umanitoba.cs.binsalef.ui.output.ConsoleColors;

public class TestHarness {
    public static void main(String[] args) {
        System.out.println(ConsoleColors.CYAN + "Test Harness ---------" + ConsoleColors.RESET);

        TestSuite[] suites = {
                new TestStack(),
                new TestExerciseSession(),
                new TestRoute(),
                new TestProfile(),
                new TestCyke(),
                new TestWorldMap(),
                new TestFeedService(),
                new TestPathFinder(),
                new TestCykeService()
        };

        boolean[] suiteFailures = new boolean[suites.length];

        boolean failures = false;

        for (int i = 0; i < suites.length; i++) {
            TestSuite suite = suites[i];
            System.out.println(ConsoleColors.CYAN + suite.name() + "-----" + ConsoleColors.RESET);
            TestResults results = suite.runTests();

            System.out.printf("Suite tests: %d\n", results.totalTests());
            System.out.printf("\tSuccesses: %d\n", results.successes());
            System.out.printf("\tFailures: %d\n", results.failures());

            if (results.failures() > 0) {
                System.out.println(ConsoleColors.RED + "There were test failures." + ConsoleColors.RESET);
                suiteFailures[i] = true;
            } else {
                System.out.println(ConsoleColors.GREEN + "All tests passed!" + ConsoleColors.RESET);

            }

            System.out.println(ConsoleColors.YELLOW + "-----------" + ConsoleColors.RESET);
            failures = failures || results.failures() > 0;
        }

        System.out.println(ConsoleColors.CYAN + "Overall Results -------" + ConsoleColors.RESET);

        for (int i = 0; i < suites.length; i++) {
            if (suiteFailures[i]) {
                System.out.println(suites[i].name() + " : " + ConsoleColors.RED + "FAILED" + ConsoleColors.RESET);
            } else {
                System.out.println(suites[i].name() + " : " + ConsoleColors.GREEN + "PASSED" + ConsoleColors.RESET);
            }
        }
    }

}

