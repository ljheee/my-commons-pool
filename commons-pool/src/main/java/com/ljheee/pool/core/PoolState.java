package com.ljheee.pool.core;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 维护 活跃队列 和 空闲队列
 */
public class PoolState<T> {

    protected final LinkedBlockingQueue<T> idle = new LinkedBlockingQueue();
    protected final LinkedBlockingQueue<T> active = new LinkedBlockingQueue();

}
