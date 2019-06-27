package com.yizhilu.os.edu.service.impl.clazz.threadLocalDemo;

import java.util.Random;

public class threadLocalTest {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();
    private static ThreadLocal<myThreadScopeData> threadLoca2 = new ThreadLocal<myThreadScopeData>();

    public static void main(String[] args) {
       for(int i=0;i<2;i++){
           new Thread(new Runnable() {
               @Override
               public void run() {
                   int random = new Random().nextInt();
                   System.out.println(Thread.currentThread().getName()+"   threadLocal  put data "+random);
                   threadLocal.set(random);


                   //myThreadScopeData myThreadScopeData =  new myThreadScopeData().setAge(10).setName("ddd");
                   //threadLoca2.set(myThreadScopeData);

                   new A().get();

                   new B().get();

               }
           }).start();
       }
    }

    static  class A{
        public void get(){
            Integer a = threadLocal.get();
            System.out.println("A    " + Thread.currentThread().getName()+" threadLocal value " + a);
        }
    }

    static  class B{
        public void get(){
            Integer a = threadLocal.get();
            System.out.println(" B   " + Thread.currentThread().getName()+" threadLocal value " + a);
        }
    }
}

class myThreadScopeData{

    private myThreadScopeData(){}

    public  static  myThreadScopeData instance(){
        myThreadScopeData myThreadScopeData =  threadLoca2.get();
        if(myThreadScopeData  == null){

        }
        return null;
    }



    private static ThreadLocal<myThreadScopeData> threadLoca2 = new ThreadLocal<myThreadScopeData>();



    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public myThreadScopeData setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public myThreadScopeData setAge(int age) {
        this.age = age;
        return this;
    }
}

