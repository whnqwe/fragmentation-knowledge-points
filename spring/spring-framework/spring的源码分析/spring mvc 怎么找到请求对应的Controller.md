







```properties

<!--注册HandlerMethod-->

//AbstractHandlerMethodMapping是个InitializingBean

//生命周期执行afterPropertiesSet

//获取环境变量中所有的Bean

//获取其中包含Controller，RequestMapping的Bean

//在RequestMappingHandlerMapping将信息保存成RequestMappingInfo
	//RequestMappingInfo里面有很多Condition
	
//将请求与RequestMappingInfo放入MultiValueMap<String, T> urlLookup


<!--获取HandlerMethod-->

//DispatcherServlet#doDispatch-- getHandler(processedRequest);

//AbstractHandlerMapping#getHandler()

//AbstractHandlerMethodMapping#getHandlerInternal

	//AbstractHandlerMethodMapping#lookupHandlerMethod
		
		//在urlLookup中查看RequestMappingInfo



```





