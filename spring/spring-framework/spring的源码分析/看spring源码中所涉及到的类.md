### 类



#### AbstractBeanFactory  

> 获取Bean的相关类

#### AbstractAutowireCapableBeanFactory

> 获取Bean的相关类

#### ContextRefreshedEvent

> 容器刷新事件

####  ContextClosedEvent

> 容器关闭事件

####  ContextStartedEvent

> 容器开始事件

#### ContextStoppedEvent

> 容器停止时间



### 接口





#### MergedBeanDefinitionPostProcessor

> @value  @autowire相关类

#### AutowiredAnnotationBeanPostProcessor

> @value  @autowire相关类

#### BeanNameAware

> Bean的生命周期相关

#### BeanClassLoaderAware

> Bean的生命周期相关

#### BeanFactoryAware

> Bean的生命周期相关

#### ApplicationContextAware

> Bean的生命周期相关

#### ApplicationContextAwareProcessor

> ApplicationContextAware生命周期复制类

#### BeanPostProcessor

> 后置处理器

#### InitializingBean

> 生命周期相关类

#### InstantiationAwareBeanPostProcessor

> @value  @autowire注入相关值 要实现的接口

#### InjectionMetadata

> Bean中有@value  @autowire注解要封装成的类

#### HandlerMethodArgumentResolver

> spring mvc 参数绑定的基类



#### BeanFactoryPostProcessor

> beanFactory的后置处理器

> 所有的bean定义已经保存加载到beanFactory，但是bean的实例还未创建

#### BeanDefinitionRegistryPostProcessor

> 优先于BeanFactoryPostProcessor执行；
>
> 利用BeanDefinitionRegistryPostProcessor给容器中再额外添加一些组件；



#### ApplicationListener

> 监听某个事件（ApplicationEvent及其子类）

#### ApplicationEventMulticaster

> 获取事件的多播器

# 注解

#### @Controller

#### @Value

#### @Autowired

#### @Resource

#### @Qualifier

#### @Scope

#### @RequestMapping

#### @PathVariable

#### @RequestParam

#### @Repository

#### @RequestBody

#### @ResponseBody

#### @Bean

#### @Configuration

#### @Component

#### @Primary

#### @ConditionalOnMissingClass

#### @ConditionalOnClass

#### @ConditionalOnBean

#### @ConditionalOnMissingBean

#### @RestController

#### @ DeleteMapping

#### @RequestHeader

#### @CookieValue

#### @GetMapping

#### @PostMapping

####  @PutMapping

#### @ControllerAdvice

#### @ExceptionHandler

#### @Import

