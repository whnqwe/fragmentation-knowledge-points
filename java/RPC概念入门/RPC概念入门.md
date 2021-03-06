#### 什么是 RPC ？

RPC 是一种框架或者说一种架构，主要目标就是让远程服务调用更简单、透明，**调用远程就像调用本地一样。**

```html
RPC（Remote Procedure Call） - 远程过程调用，它是一种通过网络从远程计算机程序上请求服务，而不需要了解底层网络技术的协议。RPC协议假定某些传输协议的存在，如TCP或UDP，为通信程序之间携带信息数据。在OSI网络通信模型中，RPC跨越了传输层和应用层。RPC使得开发包括网络分布式多程序在内的应用程序更加容易。
```



> #### 什么情况下使用 RPC ？

> 如果我们开发简单的应用，业务流程简单、流量不大，根本用不着 RPC。
>
> 当我们的应用访问量增加和业务增加时，发现单机已无法承受，此时可以根据不同的业务（划分清楚业务逻辑）拆分成几个互不关联的应用，分别部署在不同的机器上，此时可能也不需要用到 RPC 。
>
> 随着我们的业务越来越多，应用也越来越多，应用与应用相互关联调用，发现有些功能已经不能简单划分开，此时可能就需要用到 RPC。
>
> 比如，我们开发电商系统，需要拆分出用户服务、商品服务、优惠券服务、支付服务、订单服务、物流服务、售后服务等等，这些服务之间都相互调用，这时内部调用最好使用 RPC ，同时每个服务都可以独立部署，独立上线。
>
> 也就说当我们的项目太大，需要解耦服务，扩展性强、部署灵活，这时就要用到 RPC ，主要解决了分布式系统中，服务与服务之间的调用问题。
>
> 

#### RPC 框架原理

![](image/001.png)

RPC 架构主要包括三部分：

- 服务注册中心（Registry），负责将本地服务发布成远程服务，管理远程服务，提供给服务消费者使用。
- 服务提供者（Server），提供服务接口定义与服务实现类。
- 服务消费者（Client），通过远程代理对象调用远程服务



服务提供者启动后主动向服务注册中心（Registry）注册机器IP、端口以及提供的服务列表；

服务消费者启动时向服务注册中心（Registry）获取服务提供方地址列表。

服务注册中心（Registry）可实现负载均衡和故障切换。



#### RPC 调用过程

![](image/002.png)

(1) 客户端（client）以本地调用方式调用服务；

(2) 客户端存根（client stub）接收到调用后，负责将方法、参数等组装成能够进行网络传输的消息体（将消息体对象序列化为二进制）；

(3) 客户端通过 sockets 将消息发送到服务端；

(4) 服务端存根（server stub）收到消息后进行解码（将消息对象反序列化）；

(5) 服务端存根（server stub）根据解码结果调用本地的服务；

(6) 本地服务执行并将结果返回给服务端存根（server stub）；

(7) 服务端存根（server stub）将返回结果打包成消息（将结果消息对象序列化）；

(8) 服务端（server）通过 sockets 将消息发送到客户端；

(9) 客户端存根（client stub）接收到结果消息，并进行解码（将结果消息发序列化）；

(10) 客户端（client）得到最终结果。

RPC 就是要把 2、3、4、7、8、9 这些步骤都封装起来。



#### RPC 优点

- 跨语言（C++、PHP、Java、Python ...）
- 协议私密，安全性较高
- 数据传输效率高
- 支持动态扩展