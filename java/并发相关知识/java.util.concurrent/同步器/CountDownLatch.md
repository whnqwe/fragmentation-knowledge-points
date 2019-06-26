# CountDownLatch

> 线程需要等待，直到发生指定的事件数量为止。

#### 构造函数

```java
/**
*指定为打开锁存器发生的事件数量
*/
public CountDownLatch(int count) {}
```

#### 等待锁存器

```java
/**
*直到与CountDownLatch关联的计数器为0 结束等待
*/
public void await() throws InterruptedException {}


/**
*等待timeout指定的时间，unit为timeout的时间单位
*/
public boolean await(long timeout, TimeUnit unit){}
```

#### 触发事件

```java
  /**
     每次调用，与之关联的计数器减一
     */
public void countDown() {}
```

#### 代码演示

```java
public class countDownLatchThread extends Thread  {


    CountDownLatch countDownLatch;

    countDownLatchThread(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
        new Thread(this).start();
    }



    @Override
    public void run() {
       for(int i= 0;i<5;i++){
           System.out.println(countDownLatch.getCount());
           countDownLatch.countDown();
       }
    }
}


public class client {


    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(5);


        new countDownLatchThread(countDownLatch);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("done");

    }
}

```

