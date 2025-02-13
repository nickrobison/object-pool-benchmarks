package com.github.chrishantha.microbenchmark.objectpool;

import com.github.chrishantha.microbenchmark.objectpool.object.TestObject;
import org.openjdk.jmh.infra.Blackhole;

import java.util.function.Supplier;

public class LockFreePoolBenchmark extends ObjectPoolBenchmark<TestObject> {

    private LockFreePool<TestObject> objectPool;

    @Override
    public void setupObjectPool() {
        final Supplier<TestObject> supplier = () -> new TestObject(true);
        this.objectPool = new LockFreePool<>(supplier);
    }

    @Override
    public void tearDownObjectPool() throws Exception {
        this.objectPool.close();
    }

    @Override
    protected TestObject borrowObject() throws Exception {
        return this.objectPool.borrow();
    }

    @Override
    protected void releaseObject(TestObject object) throws Exception {
        this.objectPool.offer(object);
    }

    @Override
    protected void useObject(TestObject object, Blackhole blackhole) {
        blackhole.consume(object.getData());
    }
}
