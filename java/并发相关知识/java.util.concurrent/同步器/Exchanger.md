# Exchanger

> 简化两个线程之间的数据交换

````java
public class MakeString extends Thread {

    Exchanger<String> ex;
    String name;


    MakeString(Exchanger<String> ex){
       this.ex = ex;
       this.name = new String();
       new Thread(this).start();
    }

    @Override
    public void run() {
        char ch='A';

        for(int i=0;i<5;i++){
            name += ch++;
        }

        try {
            name = ex.exchange(name);
            System.out.println("make: "+name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


public class UseString extends Thread {

    Exchanger<String> ex;
    String name;


    UseString(Exchanger<String> ex){
        this.ex = ex;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            name = ex.exchange(new String());
            System.out.println("Got: "+name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


public class ExchangerDemo {

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        new  MakeString(exchanger);

        new UseString(exchanger);
    }
}
````



