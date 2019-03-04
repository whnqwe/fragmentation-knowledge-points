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

