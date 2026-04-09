package ca.umanitoba.cs.binsalef.tests;


public interface TestSuite {
    String name();

    TestResults runTests();

}
