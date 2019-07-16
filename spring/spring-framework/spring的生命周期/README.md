# bean的生命周期

![](image/beanzhouqi.png)



#### 实例化

> Spring对bean进行实例化

#### bean注入

> Spring将值和bean的引用注入到bean对应的属性中


#### 实现BeanNameAware接口

> 如果bean实现了BeanNameAware接口，Spring将bean的ID传递给
setBean-Name()方法；

```java
public interface BeanNameAware extends Aware {
	void setBeanName(String name);

}
```

#### 实现BeanFactoryAware接口

>  如果bean实现了BeanFactoryAware接口，Spring将调
> 用setBeanFactory()方法，将BeanFactory容器实例传入；

```java
public interface BeanFactoryAware extends Aware {
  void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
```

#### 实现ApplicationContextAware接口

>  如果bean实现了ApplicationContextAware接口，Spring将调
> 用setApplicationContext()方法，将bean所在的应用上下文的
> 引用传入进来；

```java
public interface ApplicationContextAware extends Aware {
	void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
```

####  bean实现BeanPostProcessor接口

> 如果bean实现了BeanPostProcessor接口，Spring将调用它们
> 的post-ProcessBeforeInitialization()方法；

```java
public interface BeanPostProcessor {
	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;
    
	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}

```

#### bean实现InitializingBean接口

> 如果bean实现了InitializingBean接口，Spring将调用它们的after-PropertiesSet()方法。类似地，如果bean使用init-method声明了初始化方法，该方法也会被调用；

```java
public interface InitializingBean {
	void afterPropertiesSet() throws Exception;
}
```

#### bean实现BeanPostProcessor接口

> 如果bean实现了BeanPostProcessor接口，Spring将调用它们的post-ProcessAfterInitialization()方法；

````java
public interface BeanPostProcessor {
	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;
    
	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}
````

#### bean已经准备就绪

>  此时，bean已经准备就绪，可以被应用程序使用了，它们将一直
> 驻留在应用上下文中，直到该应用上下文被销毁；

#### bean实现DisposableBean接口

> 如果bean实现了DisposableBean接口，Spring将调用它的destroy()接口方法。同样，如果bean使用destroy-method声明了销毁方法，该方法也会被调用。

```java
public interface DisposableBean {
   void destroy() throws Exception;
}
```



# 代码分析

#### AbstractAutowireCapableBeanFactory

##### createBean

```java
	@Override
	protected Object createBean(final String beanName, final RootBeanDefinition mbd, final Object[] args)
			throws BeanCreationException {
    ......

		Object beanInstance = doCreateBean(beanName, mbd, args);
	......
		return beanInstance;
	}
```



##### doCreateBean

```java
	protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final Object[] args) {
    ......
     	Object exposedObject = bean;
		try {
			populateBean(beanName, mbd, instanceWrapper);
			if (exposedObject != null) {
				exposedObject = initializeBean(beanName, exposedObject, mbd);
			}
		}
    ......
    
    }
```

##### initializeBean

```java
	protected Object initializeBean(final String beanName, final Object bean, RootBeanDefinition mbd) {
		if (System.getSecurityManager() != null) {
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				public Object run() {
					invokeAwareMethods(beanName, bean);
					return null;
				}
			}, getAccessControlContext());
		}
		else {
			invokeAwareMethods(beanName, bean);
		}

		Object wrappedBean = bean;
		if (mbd == null || !mbd.isSynthetic()) {
			wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
		}

		try {
			invokeInitMethods(beanName, wrappedBean, mbd);
		}
		catch (Throwable ex) {
			throw new BeanCreationException(
					(mbd != null ? mbd.getResourceDescription() : null),
					beanName, "Invocation of init method failed", ex);
		}

		if (mbd == null || !mbd.isSynthetic()) {
			wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
		}
		return wrappedBean;
	}

```



##### invokeAwareMethods

    ###### BeanNameAware  

###### BeanClassLoaderAware  

###### BeanFactoryAware

```java
	private void invokeAwareMethods(final String beanName, final Object bean) {
		if (bean instanceof Aware) {
			if (bean instanceof BeanNameAware) {
				((BeanNameAware) bean).setBeanName(beanName);
			}
			if (bean instanceof BeanClassLoaderAware) {
				((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
			}
			if (bean instanceof BeanFactoryAware) {
				((BeanFactoryAware) bean).setBeanFactory(AbstractAutowireCapableBeanFactory.this);
			}
		}
	}
```

##### applyBeanPostProcessorsBeforeInitialization

````java
	public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
			throws BeansException {

		Object result = existingBean;
		for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
			result = beanProcessor.postProcessBeforeInitialization(result, beanName);
			if (result == null) {
				return result;
			}
		}
		return result;
	}
````



##### invokeInitMethods

```java
	protected void invokeInitMethods(String beanName, final Object bean, RootBeanDefinition mbd)
			throws Throwable {
......
		((InitializingBean) bean).afterPropertiesSet();
......

......

		invokeCustomInitMethod(beanName, bean, mbd);
......
```

##### applyBeanPostProcessorsAfterInitialization

````java

	public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
			throws BeansException {

		Object result = existingBean;
		for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
			result = beanProcessor.postProcessAfterInitialization(result, beanName);
			if (result == null) {
				return result;
			}
		}
		return result;
	}
````

