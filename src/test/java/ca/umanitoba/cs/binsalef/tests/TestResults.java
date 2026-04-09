package ca.umanitoba.cs.binsalef.tests;

import ca.umanitoba.cs.binsalef.ui.output.ConsoleColors;

public class TestResults {
    private int totalTests = 0;
    private int successes = 0;
    private int failures = 0;

    public int totalTests() {
        return this.totalTests;
    }

    public int successes() {
        return this.successes;
    }

    public int failures() {
        return this.failures;
    }

    public void pass(String msg) {
        System.out.println(ConsoleColors.GREEN + "PASS" + ConsoleColors.RESET + " : " + msg);
        successes++;
        totalTests++;
    }

    public void fail(String msg) {
        System.out.println(ConsoleColors.RED + "FAIL" + ConsoleColors.RESET + " : " + msg);
        failures++;
        totalTests++;
    }
}
