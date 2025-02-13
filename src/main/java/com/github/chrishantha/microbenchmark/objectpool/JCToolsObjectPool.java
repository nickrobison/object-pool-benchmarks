package com.github.chrishantha.microbenchmark.objectpool;

import org.jctools.queues.MpmcArrayQueue;
import org.jctools.queues.MpmcUnboundedXaddArrayQueue;

import java.util.function.Supplier;

public class JCToolsObjectPool<T> {

    private final MpmcArrayQueue<T> queue;

    private final Supplier<T> factory;

    public JCToolsObjectPool(Supplier<T> factory, int poolSize) {
        // I'm not sure what siz
        this.queue = new MpmcArrayQueue<>(poolSize);
        this.factory = factory;
    }

    public T borrow() {
        final T obj = queue.poll();
        return obj == null ? factory.get() : obj;
    }

    public void offer(T obj) {
        queue.offer(obj);
    }

    public void close() {
        queue.clear();
    }
}
