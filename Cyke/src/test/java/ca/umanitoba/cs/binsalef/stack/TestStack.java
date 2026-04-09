package ca.umanitoba.cs.binsalef.stack;

import ca.umanitoba.cs.binsalef.tests.TestResults;
import ca.umanitoba.cs.binsalef.tests.TestSuite;

import ca.umanitoba.cs.comp2450.stack.Stack;
import ca.umanitoba.cs.comp2450.stack.impl.*;

public class TestStack implements TestSuite {

    @Override
    public String name() {
        return "TestStack";
    }

    @Override
    public TestResults runTests() {
        TestResults results = new TestResults();

        runAllTests("LinkedStack", new LinkedStack<>(), results);
        runAllTests("BadStack1", new BadStack1<>(), results);
        runAllTests("BadStack2", new BadStack2<>(), results);
        runAllTests("BadStack3", new BadStack3<>(), results);
        runAllTests("BadStack4", new BadStack4<>(), results);
        runAllTests("BadStack5", new BadStack5<>(), results);

        return results;
    }

    private void runAllTests(String stackName, Stack<String> stack, TestResults results) {
        testPushOnEmptyStack(stackName, stack, results);
        testPushMultipleItems(stackName, createStringStack(stackName), results);
        testPopReturnsTop(stackName, createStringStack(stackName), results);
        testPopOnEmptyStack(stackName, createStringStack(stackName), results);
        testPeekReturnsTop(stackName, createStringStack(stackName), results);
        testPeekDoesNotRemove(stackName, createStringStack(stackName), results);
        testPeekOnEmptyStack(stackName, createStringStack(stackName), results);
        testSizeAfterPushAndPop(stackName, createStringStack(stackName), results);
        testIsEmptyOnEmptyStack(stackName, createStringStack(stackName), results);
        testIsEmptyAfterPush(stackName, createStringStack(stackName), results);
    }

    private Stack<String> createStringStack(String stackName) {
        return switch (stackName) {
            case "LinkedStack" -> new LinkedStack<>();
            case "BadStack1" -> new BadStack1<>();
            case "BadStack2" -> new BadStack2<>();
            case "BadStack3" -> new BadStack3<>();
            case "BadStack4" -> new BadStack4<>();
            default -> new BadStack5<>();
        };
    }

    private void testPushOnEmptyStack(String stackName, Stack<String> stack, TestResults results) {
        try {
            stack.push("hello");
            if (stack.size() == 1 && !stack.isEmpty() && stack.peek().equals("hello")) {
                results.pass(stackName + ": testPushOnEmptyStack() on an empty stack adds one item to the top.");
            } else {
                results.fail(stackName + ": testPushOnEmptyStack() on an empty stack did not update size, top, or empty correctly.");

            }
        } catch (Exception e) {
            results.fail(stackName + ": testPushOnEmptyStack() caused an exception.");
        }
    }

    private void testPushMultipleItems(String stackName, Stack<String> stack, TestResults results) {
        try {
            stack.push("A");
            stack.push("B");
            stack.push("C");

            if (stack.size() == 3 && stack.peek().equals("C")) {
                results.pass(stackName + ": testPushMultipleItems() keeps the most recent item on top.");
            } else {
                results.fail(stackName + ": testPushMultipleItems() did not preserve stack order.");
            }
        } catch (Exception e) {
            results.fail(stackName + ": testPushMultipleItems() caused an exception.");
        }
    }

    private void testPopReturnsTop(String stackName, Stack<String> stack, TestResults results) {
        String popped;
        try {
            stack.push("A");
            stack.push("B");
            popped = stack.pop();

            if (popped.equals("B")) {
                results.pass(stackName + ": testPopReturnsTop() returns the top item.");
            } else {
                results.fail(stackName + ": testPopReturnsTop() did not return the correct top item.");
            }
        } catch (Exception e) {
            results.fail(stackName + ": testPopReturnsTop() caused an exception.");
        }


    }

    private void testPopOnEmptyStack(String stackName, Stack<String> stack, TestResults results) {
        boolean passed = false;

        try {
            stack.pop();
        } catch (Stack.EmptyStackException e) {
            passed = true;
        }

        if (passed) {
            results.pass(stackName + ": testPopOnEmptyStack() throws EmptyStackException.");
        } else {
            results.fail(stackName + ": testPopOnEmptyStack() did not throw EmptyStackException.");
        }
    }

    private void testPeekReturnsTop(String stackName, Stack<String> stack, TestResults results) {
        try {
            stack.push("A");
            stack.push("B");

            if (stack.peek().equals("B")) {
                results.pass(stackName + ": testPeekReturnsTop() returns the top item");
            } else {
                results.fail(stackName + ":testPeekReturnsTop() did not return the correct item");
            }
        } catch (Exception e) {
            results.fail(stackName + ": testPeekReturnsTop() caused an exception");
        }

    }

    private void testPeekDoesNotRemove(String stackName, Stack<String> stack, TestResults results) {
        try {
            stack.push("A");
            stack.push("B");
            stack.peek();

            if (stack.size() == 2 && stack.peek().equals("B")) {
                results.pass(stackName + ": testPeekDoesNotRemove() does not remove the top item.");
            } else {
                results.fail(stackName + ": testPeekDoesNotRemove() changed the stack when it should not have.");
            }
        } catch (Exception e) {
            results.fail(stackName + ": testPeekDoesNotRemove() caused an exception.");
        }
    }

    private void testPeekOnEmptyStack(String stackName, Stack<String> stack, TestResults results) {
        boolean passed = false;

        try {
            stack.peek();
        } catch (Stack.EmptyStackException e) {
            passed = true;
        }

        if (passed) {
            results.pass(stackName + ": testPeekOnEmptyStack() on an empty stack throws EmptyStackException.");
        } else {
            results.fail(stackName + ": testPeekOnEmptyStack() on an empty stack did not throw EmptyStackException.");
        }
    }

    private void testSizeAfterPushAndPop(String stackName, Stack<String> stack, TestResults results) {
        try {
            stack.push("A");
            stack.push("B");
            stack.pop();

            if (stack.size() == 1) {
                results.pass(stackName + ": testSizeAfterPushAndPop() updates correctly after push() and pop().");
            } else {
                results.fail(stackName + ": testSizeAfterPushAndPop() did not update correctly after push() and pop().");
            }
        } catch (Exception e) {
            results.fail(stackName + ": testSizeAfterPushAndPop()  caused an exception.");
        }
    }

    private void testIsEmptyOnEmptyStack(String stackName, Stack<String> stack, TestResults results) {

        try {
            if (stack.isEmpty()) {
                results.pass(stackName + ": testIsEmptyOnEmptyStack() on an empty stack returns true.");
            } else {
                results.fail(stackName + ": testIsEmptyOnEmptyStack() on an empty stack did not return true.");
            }
        } catch (Exception e) {
            results.fail(stackName + ": testIsEmptyOnEmptyStack() threw some other exception. ");
        }

    }

    private void testIsEmptyAfterPush(String stackName, Stack<String> stack, TestResults results) {
        try {
            stack.push("A");

            if (!stack.isEmpty()) {
                results.pass(stackName + ": testIsEmptyAfterPush() returns false.");
            } else {
                results.fail(stackName + ": testIsEmptyAfterPush() did not return false.");
            }
        } catch (Exception e) {
            results.fail(stackName + ": testIsEmptyAfterPush() caused an exception.");
        }
    }
}