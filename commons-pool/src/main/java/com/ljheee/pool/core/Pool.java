package com.ljheee.pool.core;

/**
 */
public interface Pool<T> {
    T get();
    void release(T t);
}
