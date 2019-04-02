# Spring MVC 中几种获取request和response的方式

#### 最简单方式：处理方法入参

```java
@RequestMapping("/test")
@ResponseBody
public void saveTest(HttpServletRequest request, HttpServletResponse response){
　　
}
```

#### RequestContextHolder



在Spring API中提供了一个非常便捷的工具类RequestContextHolder，能够在Controller中获取request对象和response对象，使用方法如下

```java
HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
HttpServletResponse resp = ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
```

需要注意的是如果直接使用这个工具类，则会抛出一个空指针异常。原因是需要先在web.xml配置RequestContextListener监听器:

```xml
<listener>
      <listener-class>
          org.springframework.web.context.request.RequestContextListener
      </listener-class>
</listener>
```

RequestContextListener实现了ServletRequestListener ,在其覆盖的requestInitialized(ServletRequestEvent 

requestEvent)方法中,将request最终设置到了RequestContextHolder中.

```java
public class RequestContextListener implements ServletRequestListener {

    private static final String REQUEST_ATTRIBUTES_ATTRIBUTE =
            RequestContextListener.class.getName() + ".REQUEST_ATTRIBUTES";


    @Override
    public void requestInitialized(ServletRequestEvent requestEvent) {
        if (!(requestEvent.getServletRequest() instanceof HttpServletRequest)) {
            throw new IllegalArgumentException(
                    "Request is not an HttpServletRequest: " + requestEvent.getServletRequest());
        }
        HttpServletRequest request = (HttpServletRequest) requestEvent.getServletRequest();//从事件对象中获取request对象
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);//将request设置到servletRequestAttributes中
        request.setAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE, attributes);//反过来将servletRequestAttributes设置到request中
        LocaleContextHolder.setLocale(request.getLocale());
        RequestContextHolder.setRequestAttributes(attributes);//再将servletRequestAttributes设置到requestContextHolder中
    }

```

