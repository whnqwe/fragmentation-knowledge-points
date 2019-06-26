# Semaphore

> 实现经典的信号量，通过计数器控制对共享资源的访问,计数器大于0，允许访问。如果为0，拒绝访问。

#### 构造函数

```java
/**
* permits 指定访问共享资源的线程个数
*/
public Semaphore(int permits) {}

/**
* fair 为true表示，以他们要求访问的顺序获取许可证
*/
public Semaphore(int permits, boolean fair) {}

```

#### 获取许可证

````java
/**
* 获取一个许可证
*/
public void acquire() throws InterruptedException {}

/**
* 获取permits个许可证
*/
public void acquire(int permits) throws InterruptedException {}

````

#### 释放许可证

```java
/**
* 释放一个许可证
*/
public void release() {}

/**
* 释放permits个许可证
*/
public void release(int permits) {}
```

#### 代码演示

````java
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);

        new IncrThread(semaphore,"A");
        new DecThread(semaphore,"B");
    }

public class IncrThread extends Thread {
    Semaphore sem;
    String name;

    IncrThread(Semaphore sem,String name){
         this.sem = sem;
         this.name = name;
        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("IncrThread start   "+name);

        try {
            System.out.println("IncrThread 等待获取许可证");
            sem.acquire();
            System.out.println("IncrThread 获取许可证");

            for(int i=0;i<5;i++){
                Shared.count++;
                System.out.println("IncrThread shared "+Shared.count);

                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        System.out.println("IncrThread" + name + "release the");
        sem.release();
    }
}


public class DecThread extends Thread {

    Semaphore sem;
    String name;

    DecThread(Semaphore sem,String name){
        this.sem = sem;
        this.name = name;
        new Thread(this).start();
    }



    @Override
    public void run() {
        System.out.println("DecThread start   "+name);

        try {
            System.out.println("DecThread 等待获取许可证");
            sem.acquire();
            System.out.println("DecThread 获取许可证");

            for(int i=0;i<5;i++){
                Shared.count--;
                System.out.println("DecThread shared "+Shared.count);

                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        System.out.println("DecThread" + name + "release the");
        sem.release();
    }
}


public class Shared {
    static int count = 0;
}

````

#### 信号量实现生产者消费者

```java
public class Q {
    int n;

    static Semaphore semCon = new Semaphore(0);
    static Semaphore semPro = new Semaphore(1);

    void get(){
        try {
            semCon.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("GET  "+n);
        semPro.release();
    }

    void put(int n) {
        try {
            semPro.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.n = n;
        System.out.println("PUT： "+n);

        semCon.release();
    }

}


public class ProduceQ extends  Thread {

    Q q;

    ProduceQ(Q q){
        this.q = q;
        new Thread(this).start();
    }

    @Override
    public void run() {
        for(int i=0;i<200;i++){
            q.put(i);
        }
    }
}

public class ConsumerQ  extends Thread{

    Q q;


    ConsumerQ(Q q){
        this.q = q;
        new Thread(this).start();
    }

    @Override
    public void run() {
        for(int i=0;i<200;i++){
             q.get();
        }
    }
}


public class Client {

    public static void main(String[] args) {
        Q q = new Q();

        ProduceQ pro = new ProduceQ(q);
        ConsumerQ con = new ConsumerQ(q);

    }
}
```

