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

> identity 是这样一个值，对于涉及到identity 与流中任意元素的累计操作，得到的结果就是元素本身。
>
> > 例如：累加操作，identity是0，因为0+x 是x 
> >
> > 例如:  乘法操作，identity是1，因为1 * x 是 x 

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

> BiFunction定义了如下的抽象方法

```java
@FunctionalInterface
public interface BiFunction<T, U, R> {
     R apply(T val, U val2);
}
```

> 其中，R指定了结果的类型，T 是第一个操作数的类型，U 是第二个操作数的类型，apply() 对其两个操作数应用于一个函数，并返回结果，

>  BinaryOperator扩展了BiFunction函数式接口， 为所有的类型参数指定了相同的类型，因此对于BinaryOperator来说，apply()如下

```java
@FunctionalInterface
public interface BinaryOperator<T> extends BiFunction<T,T,T> {
    T apply(T val, T val2);
}
```

> 在reduce中，val 将包含前一个结果，val2包含下一个元元素。

> accumulator操作必须满足以下的三个条件
>
> - 无状态
> - 不干预
> - 结合性









