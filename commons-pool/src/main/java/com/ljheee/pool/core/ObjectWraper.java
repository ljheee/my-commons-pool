package com.ljheee.pool.core;

/**
 * 包装类
 * 增加额外的对象信息
 */
public class ObjectWraper<T> {

    /**
     * 标识 是否可复用
     */
    public Boolean busy;
    public T origin;

    public ObjectWraper(T origin) {
        this.origin = origin;
    }
}
