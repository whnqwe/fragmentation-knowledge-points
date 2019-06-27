package com.yizhilu.os.edu.service.impl.clazz.threadLocalDemo;

import java.util.ArrayList;
import java.util.List;

public class ThreadLocalMemory11 {

    // Thread local variable containing each thread's ID
    public static ThreadLocal<List<Object>> threadId = new ThreadLocal<List<Object>>() {
        @Override
        protected List<Object> initialValue() {
            List<Object> list = new ArrayList<Object>();
            for (int i = 0; i < 10000; i++) {
                list.add(String.valueOf(i));
            }
            return list;
        }
    };
    // Returns the current thread's unique ID, assigning it if necessary
    public static List<Object> get() {
        return threadId.get();
    }
    // remove currentid
    public static void remove() {
        threadId.remove();
    }
}
