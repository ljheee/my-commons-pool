package com.ljheee.pool;

import com.ljheee.pool.core.DefaultPool;

/**
 */

class MyConnection{}
public class BigObjectPool extends DefaultPool<MyConnection> {

    @Override
    protected MyConnection doCreate() {
        return new MyConnection();
    }


    public static void main(String[] args) {
        BigObjectPool pool = new BigObjectPool();
        MyConnection myConnection = pool.get();
        pool.release(myConnection);
        pool.get();
    }
}
