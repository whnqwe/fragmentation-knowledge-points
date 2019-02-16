#### 引入两个pom文件

> https://blog.csdn.net/qq_31289187/article/details/86104522 

```xml
 <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.4</version>
  </dependency>
  <dependency>
            <groupId>com.opencsv</groupId>
            <artifactId>opencsv</artifactId>
            <version>4.2</version>
</dependency>
```

#### 创建文件：user.txt



> 内容如下

```html
tiger|M|25|shanghai
hanzi|W|23|beijing
xiaoming|M|18|
xiaohua||12|
```

> 例如：xiaohua的性别和地址不详，字段值都会为空值，方便后期文件解析以及处理。



#### 创建UserBean

```java
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @CsvBindByPosition(position = 0)
    private String name;
    @CsvBindByPosition(position = 1)
    private String gender;
    @CsvBindByPosition(position = 2)
    private int age;
    @CsvBindByPosition(position = 3)
    private String address;
}

```

> 这里使用了@CsvBindByPosition（position = 位置下标），和文件中的字段一一对应。



#### ParseFileToBeanUtils

```java
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;


public class ParseFileToBeanUtils {

    /**
     * 解析文件并返回beanList
     * @param filePath 文件路径
     * @param beanClass 对应的beanClass
     * @param separator 文件中使用的分割符
     * @return  返回beanList
     * */
    public static List<User> parseFileToUserBean(String filePath,Class beanClass,char separator) throws FileNotFoundException {
         List<User> users = new CsvToBeanBuilder(new FileReader(filePath))
                .withType(beanClass)
                 .withSeparator(separator)
                 .build()
                 .parse();
         return users;
    }

    public static void main(String[] args) throws Exception{
        String filePath = "F:/user.txt";
        List<User> users = parseFileToUserBean(filePath,User.class,'|');

        users.forEach(user ->{
            System.out.println(user.toString());
        });
    }
}
```

#### 运行结果

```html
User(name=tiger, gender=M, age=25, address=shanghai)
User(name=hanzi, gender=W, age=23, address=beijing)
User(name=xiaoming, gender=M, age=18, address=)
User(name=xiaohua, gender=, age=12, address=)
```

