package com.github.chrishantha.microbenchmark.objectpool;

import org.agrona.concurrent.ManyToManyConcurrentArrayQueue;

import java.util.function.Supplier;

public class AgronaObjectPool<T> {

    private final ManyToManyConcurrentArrayQueue<T> queue;
    private final Supplier<T> supplier;

    public AgronaObjectPool(Supplier<T> supplier, int poolSize) {
        this.queue = new ManyToManyConcurrentArrayQueue<>(poolSize);
        this.supplier = supplier;
    }

    public T borrow() {
        final T obj = this.queue.poll();
        return obj == null ? this.supplier.get() : obj;
    }

    public void offer(T obj) {
        this.queue.offer(obj);
    }

    public void close() {
        this.queue.clear();
    }


}
