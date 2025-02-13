package com.github.chrishantha.microbenchmark.objectpool;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class LockFreePool<T> {

    private final AtomicReference<Node<T>> head = new AtomicReference<>();

    private final Supplier<T> supplier;

    public LockFreePool(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T borrow() {
        for (int i = 0; i < 3; i++) {
            final Node<T> currentHead = head.get();
            if (currentHead == null) {
                return supplier.get();
            }
            if (head.compareAndSet(currentHead, currentHead.next)) {
                return currentHead.val;
            }
        }
        return supplier.get();
    }

    public void offer(T obj) {
        final Node<T> newHead = new Node<>(obj);
        for (int i = 0; i < 3; i++) {
           newHead.next = head.get();
           if (head.compareAndSet(newHead.next, newHead)) {
               return;
           }
        }
    }

    public void close() {
        final Node<T> currentHead = head.get();
        for (int i = 0; i < 3; i++) {
            if (head.compareAndSet(currentHead, null)) {
                return;
            }
        }
    }


    public static class Node<T> {
        final T val;
        Node<T> next;

        Node(T value) {
            this.val = value;
        }
    }
}
