# stream



## 如果获得流

### 集合获得流

```java
public interface Collection<E> extends Iterable<E> {
        default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

        default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
```



### 静态方法获取数组流

```java
public class Arrays {   
    public static <T> Stream<T> stream(T[] array) {
            return stream(array, 0, array.length);
        }
}
```



#### 重载方法

##### 获取IntStream

```java
    public static IntStream stream(int[] array) {
        return stream(array, 0, array.length);
    }
```

#####  获取LongStream

```java
    public static LongStream stream(long[] array) {
        return stream(array, 0, array.length);
    }
```

#####  获取DoubleStream

```java
    public static DoubleStream stream(double[] array) {
        return stream(array, 0, array.length);
    }

```



## 简单的流实例

### 终端操作

```java
 ArrayList<Integer> myList = new ArrayList<>();
        myList.add(7);
        myList.add(18);
        myList.add(20);
        myList.add(73);
        myList.add(3);

        //获取stream
        Stream<Integer> myStream = myList.stream();

```

#### min

```java
        Optional<Integer> minVal =  myStream.min(Integer::compare);

        if(minVal.isPresent()){
            System.out.println(minVal.get());
        }

```

#### max

```java
        Optional<Integer> maxVal =  myStream.max(Integer::compare);

        if(maxVal.isPresent()){
            System.out.println(maxVal.get());
        }
```

#### count

```java
   System.out.println(myStream.count());
```



### 中间操作

#### sorted

```java
        Stream<Integer>  sorted = myStream.sorted();
        sorted.forEach(n -> System.out.println(n));
```



#### filter

```java
        Stream<Integer>  filter = myStream.sorted().filter(n-> n>20);
        filter.forEach(n -> System.out.println(n));
```



## 缩减操作

> 在min() 与 max()中，基于流的的元素返回结果，有流API的属于叫做缩减操作，每个操作都将流缩为了一个值，流API将这两种操作成为特例缩减（count也成为特例缩减）



> 流API泛化了缩减的概念，提供了reduce()方法，可以基于任意条件，从流中返回一个值。



> 所有的泛化操作都是终端操作



### reduce

> accumulator 表示操作两个值得到结果的函数

```java
public interface Stream<T> extends BaseStream<T, Stream<T>> {
    
    //t 类型是流中元素的类型
     T reduce(T identity, BinaryOperator<T> accumulator);
    
    //返回optional
     Optional<T> reduce(BinaryOperator<T> accumulator);
    
    <U> U reduce(U identity,
                 BiFunction<U, ? super T, U> accumulator,
                 BinaryOperator<U> combiner);
}
```



####  返回Optional



#### 返回流中元素的类型





