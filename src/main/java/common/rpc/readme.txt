RPC的内容涵盖的信息有点多，包含Hadoop的序列化机制，RPC，代理，NIO等。
若对Hadoop序列化不了解的同学，可以参考《Hadoop2源码分析－序列化篇》。今天这篇博客为大家介绍的内容目录如下：
	RPC概述
	第三方RPC
	Hadoop V2的RPC简述
RPC概述
2.1什么是RPC
	RPC的全程是Remote Procedure Call，中文释为远程过程调用。
	也就是说，调用的过程代码（业务服务代码）并不在调用者本地运行，而是要实现调用着和被调用着之间的连接通信，
	有同学可能已经发现，这个和C/S模式很像。
	没错，RPC的基础通信模式是基于C/S进程间相互通信的模式来实现的，它对Client端提供远程接口服务，其RPC原理图如下所示：
		Client(listen ip and port)--request-->net<--response--Server(start server)
2.2RPC的功能
	我们都知道，在过去的编程概念中，过程是由开发人员在本地编译完成的，并且只能局限在本地运行的某一段代码，
	即主程序和过程程序是一种本地调用关系。
	因此，这种结构在如今网络飞速发展的情况下已无法适应实际的业务需求。
	而且，传统过程调用模式无法充分利用网络上其他主机的资源，如CPU，内存等，也无法提高代码在Bean之间的共享，使得资源浪费较大。

	而RPC的出现，正好有效的解决了传统过程中存在的这些不足。
	通过RPC，我们可以充分利用非共享内存的机器，可以简便的将应用分布在多台机器上，类似集群分布。
	这样方便的实现过程代码共享，提高系统资源的利用率。减少单个集群的压力，实现负载均衡。
3.第三方RPC
	在学习Hadoop V2的RPC机制之前，我们先来熟悉第三方的RPC机制是如何工作的，下面我以Thrift框架为例子。
	Thrift是一个软件框架，用来进行可扩展且跨语言的服务开发协议。
	它拥有强大的代码生成引擎，支持C++，Java，Python，PHP，Ruby等编程语言。
	Thrift允许定义一个简单的定义文件（以.thirft结尾），文件中包含数据类型和服务接口。
	用以作为输入文件，编译器生成代码用来方便的生成RPC客户端和服务端通信的编程语言。
	具体Thrift安装过程请参考《Mac OS X 下搭建thrift环境》

	下面为大家解释一下上面的原理图，
	首先，我们编译完thrift定义文件后（这里我使用的是Java语言），会生成对应的Java类文件，
	该类的Iface接口定义了我们所规范的接口函数。
	在服务端，实现Iface接口，编写对应函数下的业务逻辑，启动服务。
	客户端同样需要生成的Java类文件，以供Client端调用相应的接口函数，监听服务端的IP和PORT来获取连接对象。

3.2代码示例
	StatsServer.java/StatsClient.java
StatQueryService类：
　这个类的代码量太大，暂不贴出。需要的同学请到以下地址下载。
　　下载地址：git@gitlab.com:dengjie/Resource.git
4.Hadoop V2的RPC简述
	Hadoop V2中的RPC采用的是自己独立开发的协议，其核心内容包含服务端，客户端，交互协议。
	源码内容都在hadoop-common-project项目的org.apache.hadoop.ipc包下面。
	See:org.apache.hadoop.ipc.VersionedProtocol
	该类中的两个方法一个是作为版本，另一个作为签名用。
	RPC下的Server类：
	 /** An RPC Server. */
	  public abstract static class Server extends org.apache.hadoop.ipc.Server {
	   boolean verbose;
	   static String classNameBase(String className) {
		  String[] names = className.split("\\.", -1);
		  if (names == null || names.length == 0) {
			return className;
		  }
		  return names[names.length-1];
		}
　　对外提供服务，处理Client端的请求，并返回处理结果。

　　至于Client端，监听Server端的IP和PORT，封装请求数据，并接受Response。
5.总结
	这篇博客赘述了RPC的相关内容，让大家先熟悉一下RPC的相关机制和流程，
	并简述了Hadoop V2的RPC机制，关于Hadoop V2的RPC详细内容会在下一篇博客中给大家分享。
	这里只是让大家先对Hadoop V2的RPC机制有个初步的认识。

6.Hadoop V2 RPC框架使用实例
本实例主要演示通过Hadoop V2的RPC框架实现一个计算两个整数的Add和Sub，
服务接口为 CaculateService ，继承于 VersionedProtocol ，具体代码如下所示：
CaculateService.java
注意，本工程使用的是Hadoop-2.6.0版本，这里CaculateService接口需要加入注解，来声明版本号。
CaculateServiceImpl类实现CaculateService接口。代码如下所示：
CaculateServiceImpl.java
CaculateServer服务类，对外提供服务，具体代码如下所示：
CaculateServer
注意，在Hadoop V2版本中，获取RPC下的Server对象不能在使用RPC.getServer()方法了，该方法已被移除，取而代之的是使用Builder方法来构建新的Server对象。
RPCClient客户端类，用于访问Server端，具体代码实现如下所示：
RPCClient
Hadoop V2 RPC服务端截图预览，如下所示：

7.总结
　　Hadoop V2 RPC框架对Socket通信进行了封装，定义了自己的基类接口VersionProtocol。该框架需要通过网络以序列化的方式传输对象，关于Hadoop V2的序列化可以参考《Hadoop2源码分析－序列化篇》，传统序列化对象较大。框架内部实现了基于Hadoop自己的服务端对象和客户端对象。服务端对象通过new RPC.Builder().builder()的方式来获取，客户端对象通过RPC.getProxy()的方式来获取。并且都需要接受Configuration对象，该对象实现了Hadoop相关文件的配置。
8.结束语







