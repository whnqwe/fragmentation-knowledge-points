package com.yizhilu.os.edu.service.impl.clazz.threadLocalDemo;

public class ThreadLocalIDClient {

    private static void incrThreadLoaclID(){
        try{

            for(int i=0;i<5;i++){
                System.out.println(Thread.currentThread()+" "+ i +" ThreadLocalID  "+ ThreadLocalID.get());
            }
        }finally {
            ThreadLocalID.remove();
        }
    }

    public static void main(String[] args) {
        incrThreadLoaclID();

        new Thread(){
            @Override
            public void run() {
                incrThreadLoaclID();
            }
        }.start();


        new Thread(){
            @Override
            public void run() {
                incrThreadLoaclID();
            }
        }.start();
    }
}
