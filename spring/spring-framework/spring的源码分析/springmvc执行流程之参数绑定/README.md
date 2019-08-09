# springmvc参数绑定

### 接口HandlerMethodArgumentResolver

> https://www.cnblogs.com/w-y-c-m/p/8443892.html

> RequestParamMethodArgumentResolver
>
> > AbstractNamedValueMethodArgumentResolver
> >
> > > HandlerMethodArgumentResolver

>  方法参数解析器接口，这个接口是SpringMVC参数解析绑定的核心接口。不同的参数类型绑定都是通过实现这个接口来实现。也可以通过实现这个接口来自定义参数解析器。这个接口中有如下两个方法

```java
public interface HandlerMethodArgumentResolver {

    //该解析器是否支持parameter参数的解析
    boolean supportsParameter(MethodParameter parameter);

    //将方法参数从给定请求(webRequest)解析为参数值并返回
    Object resolveArgument(MethodParameter parameter,
                          ModelAndViewContainer mavContainer,
                          NativeWebRequest webRequest,
                          WebDataBinderFactory binderFactory) throws Exception;

}
```

### 初始化

#### RequestMappingHandlerAdapter#afterPropertiesSet

> 初始化相关方法参数解析器。

```java
if (this.argumentResolvers == null) {
     initControllerAdviceCache();
        //初始化SpringMVC默认的方法参数解析器，并添加至argumentResolvers（HandlerMethodArgumentResolverComposite）
        List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
        this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
    }
    if (this.initBinderArgumentResolvers == null) {
        //初始化SpringMVC默认的初始化绑定器(@InitBinder)参数解析器，并添加至initBinderArgumentResolvers（HandlerMethodArgumentResolverComposite）
        List<HandlerMethodArgumentResolver> resolvers = getDefaultInitBinderArgumentResolvers();
        this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
    }
    if (this.returnValueHandlers == null) {
        //获取默认的方法返回值解析器
        List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
        this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
    }
}
```

####RequestMappingHandlerAdapter#getDefaultArgumentResolvers

> 想具体确认某个参数会交个哪个参数解析器处理，可以通过以下解析器的supportsParameter(MethodParameter parameter)方法得知

```java
//默认的参数解析，创建了默认的24个参数解析器，并添加至resolvers
private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
    List<HandlerMethodArgumentResolver> resolvers = new ArrayList<HandlerMethodArgumentResolver>();

    // 基于注解的参数解析器

    //一般用于带有@RequestParam注解的简单参数绑定，简单参数比如byte、int、long、double、String以及对应的包装类型
    resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
    //用于处理带有@RequestParam注解，且参数类型为Map的解析绑定
    resolvers.add(new RequestParamMapMethodArgumentResolver());
    //一般用于处理带有@PathVariable注解的默认参数绑定
    resolvers.add(new PathVariableMethodArgumentResolver());
    //也是用于带有@PathVariable注解的Map相关参数绑定，后续还有一些默认的参数解析器。后续还有一些参数解析器，我这里都不一一解释了。
    resolvers.add(new PathVariableMapMethodArgumentResolver());
    resolvers.add(new MatrixVariableMethodArgumentResolver());
    resolvers.add(new MatrixVariableMapMethodArgumentResolver());
    resolvers.add(new ServletModelAttributeMethodProcessor(false));
    resolvers.add(new RequestResponseBodyMethodProcessor(getMessageConverters()));
    resolvers.add(new RequestPartMethodArgumentResolver(getMessageConverters()));
    resolvers.add(new RequestHeaderMethodArgumentResolver(getBeanFactory()));
    resolvers.add(new RequestHeaderMapMethodArgumentResolver());
    resolvers.add(new ServletCookieValueMethodArgumentResolver(getBeanFactory()));
    resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));

    // 基于类型的参数解析器
    resolvers.add(new ServletRequestMethodArgumentResolver());
    resolvers.add(new ServletResponseMethodArgumentResolver());
    resolvers.add(new HttpEntityMethodProcessor(getMessageConverters()));
    resolvers.add(new RedirectAttributesMethodArgumentResolver());
    resolvers.add(new ModelMethodProcessor());
    resolvers.add(new MapMethodProcessor());
    resolvers.add(new ErrorsMethodArgumentResolver());
    resolvers.add(new SessionStatusMethodArgumentResolver());
    resolvers.add(new UriComponentsBuilderMethodArgumentResolver());

    // Custom arguments
    if (getCustomArgumentResolvers() != null) {
        resolvers.addAll(getCustomArgumentResolvers());
    }
```

#### HandlerMethodArgumentResolverComposite#addResolvers

```java
//它的元素在RequestMappingHandlerAdapter类的afterPropertiesSet方法中被添加，存放的是SpringMVC一些默认的HandlerMethodArgumentResolver参数解析器
private final List<HandlerMethodArgumentResolver> argumentResolvers =
            new LinkedList<HandlerMethodArgumentResolver>();
//存放已经解析过的参数，已经对应的HandlerMethodArgumentResolver解析器。加快查找过程
private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache =
        new ConcurrentHashMap<MethodParameter, HandlerMethodArgumentResolver>(256);
```



### 绑定过程

```java
@Controller
@RequestMapping("/ParameterBind")
public class ParameterBindTestController {
    @ResponseBody
    @RequestMapping("/test1")
    public String test1(int id){
        System.out.println(id);
        return "test1";
    }
}
```

![](image/001.png)

> 请求进入DispatcherServlet的doDispatch后，获取HandlerMethod。然后根据HandlerMethod来确认HandlerApapter，确认后执行HandlerAdapter的handle方法。这里确认HandlerApater为RequestMappingHandlerAdapter，在执行handlerMethod之前，需要处理参数的绑定。然后看看详细的参数绑定过程

> 执行HandlerAdapter的handler方法后，进入RequestMappingHandlerAdapter的invokeHandleMethod方法

#### RequestMappingHandlerAdapter#invokeHandleMethod

```java
private ModelAndView invokeHandleMethod(HttpServletRequest request,
            HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {

    ServletWebRequest webRequest = new ServletWebRequest(request, response);

    WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
    ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory);
    //根据handlerMethod和binderFactory创建一个ServletInvocableHandlerMethod。后续把请求直接交给ServletInvocableHandlerMethod执行。
    //createRequestMappingMethod方法比较简单，把之前RequestMappingHandlerAdapter初始化的argumentResolvers和returnValueHandlers添加至ServletInvocableHandlerMethod中
    ServletInvocableHandlerMethod requestMappingMethod = createRequestMappingMethod(handlerMethod, binderFactory);

    ModelAndViewContainer mavContainer = new ModelAndViewContainer();
    mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
    modelFactory.initModel(webRequest, mavContainer, requestMappingMethod);
    mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);

    AsyncWebRequest asyncWebRequest = WebAsyncUtils.createAsyncWebRequest(request, response);
    asyncWebRequest.setTimeout(this.asyncRequestTimeout);

    final WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
    asyncManager.setTaskExecutor(this.taskExecutor);
    asyncManager.setAsyncWebRequest(asyncWebRequest);
    asyncManager.registerCallableInterceptors(this.callableInterceptors);
    asyncManager.registerDeferredResultInterceptors(this.deferredResultInterceptors);

    if (asyncManager.hasConcurrentResult()) {
        Object result = asyncManager.getConcurrentResult();
        mavContainer = (ModelAndViewContainer) asyncManager.getConcurrentResultContext()[0];
        asyncManager.clearConcurrentResult();

        if (logger.isDebugEnabled()) {
            logger.debug("Found concurrent result value [" + result + "]");
        }
        requestMappingMethod = requestMappingMethod.wrapConcurrentResult(result);
    }

    //执行invokeForRequest
    requestMappingMethod.invokeAndHandle(webRequest, mavContainer);

    if (asyncManager.isConcurrentHandlingStarted()) {
        return null;
    }

    return getModelAndView(mavContainer, modelFactory, webRequest);
}
```

#### ServletInvocableHandlerMethod#invokeForRequest

> 然后进入invokeAndHanldle方法，然后进入invokeForRequest方法，这个方法的职责是从request中解析出HandlerMethod方法所需要的参数，然后通过反射调用HandlerMethod中的method。

```java
public final Object invokeForRequest(NativeWebRequest request,
                                        ModelAndViewContainer mavContainer,
                                        Object... providedArgs) throws Exception {
        //从request中解析出HandlerMethod方法所需要的参数，并返回Object[]
        Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);

        if (logger.isTraceEnabled()) {
            StringBuilder builder = new StringBuilder("Invoking [");
            builder.append(this.getMethod().getName()).append("] method with arguments ");
            builder.append(Arrays.asList(args));
            logger.trace(builder.toString());
        }
        //通过反射执行HandleMethod中的method，方法参数为args。并返回方法执行的返回值
        Object returnValue = invoke(args);

        if (logger.isTraceEnabled()) {
            logger.trace("Method [" + this.getMethod().getName() + "] returned [" + returnValue + "]");
        }

        return returnValue;
    }
```

> 直接进入getMethodArgumentValues方法看看其过程

#### InvocableHandlerMethod#getMethodArgumentValues

```java
/**
* 获取当前请求的方法参数值。
*/
private Object[] getMethodArgumentValues(
        NativeWebRequest request, ModelAndViewContainer mavContainer,
        Object... providedArgs) throws Exception {
    //获取方法参数数组
    MethodParameter[] parameters = getMethodParameters();
    //创建一个参数数组，保存从request解析出的方法参数
    Object[] args = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
        MethodParameter parameter = parameters[i];
        parameter.initParameterNameDiscovery(parameterNameDiscoverer);
        GenericTypeResolver.resolveParameterType(parameter, getBean().getClass());

        args[i] = resolveProvidedArgument(parameter, providedArgs);
        if (args[i] != null) {
            continue;
        }
        //判断之前RequestMappingHandlerAdapter初始化的那24个HandlerMethodArgumentResolver（参数解析器），是否存在支持该参数解析的解析器
        if (argumentResolvers.supportsParameter(parameter)) {
            try {
                args[i] = argumentResolvers.resolveArgument(parameter, mavContainer, request, dataBinderFactory);
                continue;
            } catch (Exception ex) {
                if (logger.isTraceEnabled()) {
                    logger.trace(getArgumentResolutionErrorMessage("Error resolving argument", i), ex);
                }
                throw ex;
            }
        }

        if (args[i] == null) {
            String msg = getArgumentResolutionErrorMessage("No suitable resolver for argument", i);
            throw new IllegalStateException(msg);
        }
    }
    return args;
}
```

> 进入HandlerMethodArgumentResolverComposite的resolveArgument方法

#### HandlerMethodArgumentResolverComposite#resolveArgument

```java
public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        //首先获取参数解析器，这里获取的逻辑是首先从argumentResolverCache缓存中获取该MethodParameter匹配的HandlerMethodArgumentResolver。如果为空，遍历初始化定义的那24个。查找匹配的HandlerMethodArgumentResolver，然后添加至argumentResolverCache缓存中
        HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
        Assert.notNull(resolver, "Unknown parameter type [" + parameter.getParameterType().getName() + "]");
        //解析参数
        return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
    }
```

> 然后进入RequestParamMethodArgumentResolver的resolverArgument方法

#### RequestParamMethodArgumentResolver#resolveArgument

```java
String[] paramValues = webRequest.getParameterValues(name);
if (paramValues != null) {
    arg = paramValues.length == 1 ? paramValues[0] : paramValues;
}
```

