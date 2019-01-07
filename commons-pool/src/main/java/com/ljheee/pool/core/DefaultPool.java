package com.ljheee.pool.core;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public abstract class DefaultPool<T> implements Pool<T> {

    private static final int MAX_SIZE = 10;
    int poolTimeToWait = 1000;
    private PoolState<T> poolState = new PoolState();


    @Override
    public T get() {

        T t = null;
        while (t == null){
            synchronized (this.poolState){
                // 空闲队列还有，直接取
                t = poolState.idle.poll();
                if (t != null) {
                    poolState.active.offer(t);
                    return t;
                }

                // 空闲队列没有，但能新建
                if (poolState.active.size() < MAX_SIZE) {
                    t = doCreate();
                    poolState.active.offer(t);
                    return t;
                }

                try {
                    // 连接用尽，请排队等待
                    t = poolState.idle.poll(poolTimeToWait, TimeUnit.MILLISECONDS); //等待1秒，超过1秒返回null
                    if (null == t) {
                        throw new RuntimeException("等待超时");
                    }
                    System.out.println("等待得到了连接对象");
                    return t;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
    }

//    protected  ObjectWraper doGetWraper(){
//        ObjectWraper wraper = null;
//        while(wraper == null){
//            synchronized (poolState){
//                if (!this.poolState.idle.isEmpty()) {
//                    // 有空闲 连接
//                    wraper = this.poolState.idle.peek();
//                } else if (this.poolState.active.size() < MAX_SIZE) {
//                    //无空闲连接，但可以创建新连接
//                    T t = doCreate();
//                    wraper = new ObjectWraper(t);
//                } else {
//
//                    //获取 最旧 的连接，看是否可以复用
//                    ObjectWraper oldestActive = this.poolState.active.peekLast();
//                    if (!oldestActive.busy) {
//                        wraper = new ObjectWraper(oldestActive.origin);
//                        this.poolState.active.remove(oldestActive);
//                    } else {
//                        //没有可复用的，且不能再创建新的
//                        try {
//                            //调用wait，导致当前线程（用T表示）阻塞，释放锁，进入等待状态
//                            //等待时间（timeout）后，自动唤醒
//                            this.poolState.wait((long) this.poolTimeToWait);
//                        } catch (InterruptedException e) {
//                            break;
//                        }
//                    }
//                }
//                if (wraper != null) {
//                    if (!wraper.busy) {
//                        wraper.busy = true;
//                        this.poolState.active.offer(wraper);
//                    }
//                }
//
//            }
//        }
//        return wraper;
//    }


    @Override
    public void release(T t) {
        synchronized (this.poolState) {
            this.poolState.active.remove(t);
            this.poolState.idle.offer(t);
        }
    }


    protected abstract T doCreate();


}
