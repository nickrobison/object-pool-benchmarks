package com.github.chrishantha.microbenchmark.objectpool;

import com.github.chrishantha.microbenchmark.objectpool.object.TestObject;
import org.openjdk.jmh.infra.Blackhole;
import stormpot.*;

import java.util.concurrent.TimeUnit;

public class StormpotPoolBenchmark extends ObjectPoolBenchmark<StormpotPoolBenchmark.PoolableTestObject> {

    private Pool<PoolableTestObject> objectPool;

    private final Timeout timeout = new Timeout(2, TimeUnit.MINUTES);

    @Override
    public void setupObjectPool() {
        final Allocator<PoolableTestObject> allocator = new Allocator<>() {

            @Override
            public PoolableTestObject allocate(Slot slot) throws Exception {
                return new PoolableTestObject(slot);
            }

            @Override
            public void deallocate(PoolableTestObject poolableTestObject) throws Exception {
                // Not used
            }
        };

        objectPool = Pool.from(allocator).build();


    }

    @Override
    public void tearDownObjectPool() throws Exception {
        objectPool.shutdown().await(timeout);
    }

    @Override
    protected PoolableTestObject borrowObject() throws Exception {
        return objectPool.claim(timeout);
    }

    @Override
    protected void releaseObject(PoolableTestObject object) throws Exception {
        object.release();
    }

    @Override
    protected void useObject(PoolableTestObject object, Blackhole blackhole) {
        blackhole.consume(object.getData());
    }

    public static class PoolableTestObject extends TestObject implements Poolable {

        private final Slot slot;

        public PoolableTestObject(Slot slot) {
            super(true);
            this.slot = slot;
        }

        @Override
        public void release() {
            slot.release(this);
        }
    }
}
