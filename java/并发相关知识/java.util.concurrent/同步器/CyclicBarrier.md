# CyclicBarrier

> 具有两个或多个线程的线程组必须在预定的执行点进行等待，知道线程组中所有的线程都到达执行点为止。

#### 构造函数

```java

/**
继续执行之前必须到达临界点的线程数量
*/
public CyclicBarrier(int parties) {}

/**
barrierAction 指定了到达临界点时要执行的线程
*/
public CyclicBarrier(int parties, Runnable barrierAction) {}
```

#### 执行await()方法

```java
 
/**
执行线程挂起，直到临界点为止
*/
public int await() throws InterruptedException, BrokenBarrierException {}

/**
等待由timeout指定的时间，时间单位由unit指定
*/
public int await(long timeout, TimeUnit unit){}
```

#### 代码演示

```java
public class newThread extends Thread {


    CyclicBarrier cyclicBarrier;
    String name;

    newThread( CyclicBarrier cyclicBarrier,String name){
       this.cyclicBarrier = cyclicBarrier;
       this.name = name;
       new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println(name);
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}


public class newThread2 extends Thread {

    @Override
    public void run() {
        System.out.println("done zhang");
    }
}

public class Client {

    public static void main(String[] args) {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3,new newThread2());

        System.out.println("start cyclicBarrier");

        new newThread(cyclicBarrier,"A");
        new newThread(cyclicBarrier,"B");
        new newThread(cyclicBarrier,"C");
    }
}

```

####  CyclicBarrier重用

```java

/**
start cyclicBarrier
A
B
C
done zhang
D
E
F
done zhang

*/
       

CyclicBarrier cyclicBarrier = new CyclicBarrier(3,new newThread2());

        System.out.println("start cyclicBarrier");

        new newThread(cyclicBarrier,"A");
        new newThread(cyclicBarrier,"B");
        new newThread(cyclicBarrier,"C");
        new newThread(cyclicBarrier,"D");
        new newThread(cyclicBarrier,"E");
        new newThread(cyclicBarrier,"F");
```

