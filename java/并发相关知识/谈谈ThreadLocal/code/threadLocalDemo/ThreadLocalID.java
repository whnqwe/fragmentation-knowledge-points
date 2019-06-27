package com.yizhilu.os.edu.service.impl.clazz.threadLocalDemo;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalID {

    private final static AtomicInteger ai = new AtomicInteger(0);

    private static final ThreadLocal<Integer> threadId = new ThreadLocal<Integer>(){

        @Override
        protected Integer initialValue() {
            return ai.getAndDecrement();
        }
    };


    public static int get(){
        return threadId.get();
    }


    public static void remove(){
        threadId.remove();
    }



}
