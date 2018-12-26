####   @Secured

> @EnableGlobalMethodSecurity注解，securedEnabled属性设置成了true

> 使用@Secured注解限制方法调用

#### @RolesAllowed

> 使用Java标准定义的注解限制方法调用(JSR-250)

>  @EnableGlobalMethodSecurity注解，jsr250Enabled属性设置成了true

> 但需要说明的一点是这与securedEnabled并不冲突。这两种注解风格可以同时启用。

#### @PreAuthorize / @PostAuthorize

> @EnableGlobalMethod-Security注解的prePostEnabled属性设置为true

>  @PreAuthorize 在方法调用之前，基于SpEL表达式的计算结果来限制对方法的访问

> @PostAuthorize 允许方法调用，但是如果表达式计算结果为false，将抛出一个安全性异常

> @PreAuthorize和@PostAuthorize之间的关键区别在于表达式执行的时机。
>
> @PreAuthorize的表达式会在方法调用之前执行，如果表达式的计算结果不为true的话，将会阻止方法执行。
>
> @PostAuthorize的表达式直到方法返回才会执行，然后决定是否抛出安全性的异常。

```java
@PreAuthorize("hasRole('ROLE_ADMIN')")
```

> 类同@Secured   @RolesAllowed 的使用
>
>
>
> 使用表达式来保护方法的话，那使用@PreAuthorize和@PostAuthorize是非常好的方案。 



#### @PostFilter

允许方法调用，但必须按照表达式来过滤方法的结果



#### @PreFilter 

允许方法调用，但必须在进入方法之前过滤输入值

