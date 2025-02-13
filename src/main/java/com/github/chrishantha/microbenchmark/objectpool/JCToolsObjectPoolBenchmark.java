package com.github.chrishantha.microbenchmark.objectpool;

import com.github.chrishantha.microbenchmark.objectpool.object.TestObject;
import org.openjdk.jmh.infra.Blackhole;

import java.util.function.Supplier;

public class JCToolsObjectPoolBenchmark extends ObjectPoolBenchmark<TestObject> {

    JCToolsObjectPool<TestObject> objectPool;

    @Override
    public void setupObjectPool() {

        final Supplier<TestObject> supplier = () -> new TestObject(true);

        objectPool = new JCToolsObjectPool<>(supplier);
    }

    @Override
    public void tearDownObjectPool() throws Exception {
        objectPool.close();
    }

    @Override
    protected TestObject borrowObject() throws Exception {
        return objectPool.borrow();
    }

    @Override
    protected void releaseObject(TestObject object) throws Exception {
        objectPool.offer(object);
    }

    @Override
    protected void useObject(TestObject object, Blackhole blackhole) {
        blackhole.consume(object.getData());
    }
}
