package ca.umanitoba.cs.binsalef.stack;

import ca.umanitoba.cs.comp2450.stack.Stack;
import com.google.common.base.Preconditions;

public class LinkedStack<T> implements Stack<T> {

    private StackNode<T> top;
    private int size;

    public LinkedStack() {
        top = null;
        size = 0;
    }

    @Override
    public void push(T item) {
        StackNode<T> temp;

        Preconditions.checkNotNull(item, "item cannot be null.");

        temp = new StackNode<>(item, top);
        top = temp;
        size++;
    }

    @Override
    public T pop() throws EmptyStackException {
        T topItem;

        if (isEmpty()) {
            throw new EmptyStackException("Cannot pop from an empty stack.");
        }

        topItem = top.data;
        top = top.next;
        size--;

        return topItem;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public T peek() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException("Cannot peek at an empty stack.");
        }

        return top.data;
    }

    private static class StackNode<T> {
        private T data;
        private StackNode<T> next;

        public StackNode(T data, StackNode<T> next) {
            Preconditions.checkNotNull(data, "Node data cannot be null.");
            this.data = data;
            this.next = next;
        }
    }
}