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
	Hadoop V2 RPC框架对Socket通信进行了封装，定义了自己的基类接口VersionProtocol。
	该框架需要通过网络以序列化的方式传输对象，传统序列化对象较大。
	框架内部实现了基于Hadoop自己的服务端对象和客户端对象。
	服务端对象通过new RPC.Builder().builder()的方式来获取，客户端对象通过RPC.getProxy()的方式来获取。
	并且都需要接受Configuration对象，该对象实现了Hadoop相关文件的配置。

---------------------------------------------------------------
2.YARN的RPC介绍
	我们知道在Hadoop的RPC当中，其主要由RPC，Client及Server这三个大类组成，
	分别实现对外提供编程接口、客户端实现及服务端实现。如下图所示：
	图中是Hadoop的RPC的一个类的关系图，大家可以到《Hadoop2源码分析－RPC探索实战》一文中，通过代码示例去理解他们之间的关系，这里就不多做赘述了。接下来，我们去看Yarn的RPC。
	Yarn对外提供的是YarnRPC这个类，这是一个抽象类，通过阅读YarnRPC的源码可以知道，实际的实现由参数yarn.ipc.rpc.class设定，默认情况下，其值为：org.apache.hadoop.yarn.ipc.HadoopYarnProtoRPC，部分代码如下：
	See:YarnRPC.java

		public abstract class YarnRPC {
		   // ......

			public static YarnRPC create(Configuration conf) {
			LOG.debug("Creating YarnRPC for " +
				conf.get(YarnConfiguration.IPC_RPC_IMPL));
			String clazzName = conf.get(YarnConfiguration.IPC_RPC_IMPL);
			if (clazzName == null) {
			  clazzName = YarnConfiguration.DEFAULT_IPC_RPC_IMPL;
			}
			try {
			  return (YarnRPC) Class.forName(clazzName).newInstance();
			} catch (Exception e) {
			  throw new YarnRuntimeException(e);
			}
		  }

		}
	YarnConfiguration类：

		public class YarnConfiguration extends Configuration {

		  //Configurations
		  public static final String YARN_PREFIX = "yarn.";

		  ////////////////////////////////
		  // IPC Configs
		  ////////////////////////////////
		  public static final String IPC_PREFIX = YARN_PREFIX + "ipc.";
		  /** RPC class implementation*/
		  public static final String IPC_RPC_IMPL =
			IPC_PREFIX + "rpc.class";
		  public static final String DEFAULT_IPC_RPC_IMPL =
			"org.apache.hadoop.yarn.ipc.HadoopYarnProtoRPC";
		}

	而HadoopYarnProtoRPC 通过 RPC 的 RpcFactoryProvider 生成客户端工厂
	(由参数 yarn.ipc.client.factory.class 指定，默认值是 org.apache.hadoop.yarn.factories.impl.pb.RpcClientFactoryPBImpl)
	和服务器工厂 (由参数 yarn.ipc.server.factory.class 指定，
	默认值是 org.apache.hadoop.yarn.factories.impl.pb.RpcServerFactoryPBImpl)，
	以根据通信协议的 Protocol Buffers 定义生成客户端对象和服务器对象。相关类的部分代码如下：
	HadoopYarnProtoRPC

		public class HadoopYarnProtoRPC extends YarnRPC {

		  private static final Log LOG = LogFactory.getLog(HadoopYarnProtoRPC.class);

		  @Override
		  public Object getProxy(Class protocol, InetSocketAddress addr,
			  Configuration conf) {
			LOG.debug("Creating a HadoopYarnProtoRpc proxy for protocol " + protocol);
			return RpcFactoryProvider.getClientFactory(conf).getClient(protocol, 1,
				addr, conf);
		  }

		  @Override
		  public void stopProxy(Object proxy, Configuration conf) {
			RpcFactoryProvider.getClientFactory(conf).stopClient(proxy);
		  }

		  @Override
		  public Server getServer(Class protocol, Object instance,
			  InetSocketAddress addr, Configuration conf,
			  SecretManager<? extends TokenIdentifier> secretManager,
			  int numHandlers, String portRangeConfig) {
			LOG.debug("Creating a HadoopYarnProtoRpc server for protocol " + protocol +
				" with " + numHandlers + " handlers");

			return RpcFactoryProvider.getServerFactory(conf).getServer(protocol,
				instance, addr, conf, secretManager, numHandlers, portRangeConfig);

		  }

		}

	RpcFactoryProvider

		public class RpcFactoryProvider {

		  // ......

		  public static RpcClientFactory getClientFactory(Configuration conf) {
			String clientFactoryClassName = conf.get(
				YarnConfiguration.IPC_CLIENT_FACTORY_CLASS,
				YarnConfiguration.DEFAULT_IPC_CLIENT_FACTORY_CLASS);
			return (RpcClientFactory) getFactoryClassInstance(clientFactoryClassName);
		  }

		  //......

		}

		/** Factory to create client IPC classes.*/
		  public static final String IPC_CLIENT_FACTORY_CLASS =
			IPC_PREFIX + "client.factory.class";
		  public static final String DEFAULT_IPC_CLIENT_FACTORY_CLASS =
			  "org.apache.hadoop.yarn.factories.impl.pb.RpcClientFactoryPBImpl";

在 YARN 中并未使用Hadoop自带的Writable来做序列化，而是使用 Protocol Buffers 作为默认的序列化机制，这带来的好处主要有以下几点：
	继承Protocol Buffers的优点：Protocol Buffers已被实践证明其拥有高效性、可扩展性、紧凑性以及跨语言性等特点。
	支持在线升级回滚：在Hadoop 2.x版本后，添加的HA方案，该方案能够进行主备切换，在不停止NNA节点服务的前提下，能够在线升级版本。

3.YARN的RPC示例
	YARN 的工作流程是先定义通信协议接口ResourceTracker，它包含2个函数，具体代码如下所示：
	ResourceTracker：

		public interface ResourceTracker {

		  @Idempotent
		  public RegisterNodeManagerResponse registerNodeManager(
			  RegisterNodeManagerRequest request) throws YarnException,
			  IOException;

		  @AtMostOnce
		  public NodeHeartbeatResponse nodeHeartbeat(NodeHeartbeatRequest request)
			  throws YarnException, IOException;

		}

	这里ResourceTracker提供了Protocol Buffers定义和Java实现，
	其中设计的Protocol Buffers文件有：
	ResourceTracker.proto、yarn_server_common_service_protos.proto和yarn_server_common_protos.proto，
	文件路径在Hadoop的源码包的 hadoop-2.6.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-common/src/main/proto，
	这里就不贴出3个文件的具体代码类，大家可以到该目录去阅读这部分代码。
	这里需要注意的是，若是大家要编译这些文件需要安装 ProtoBuf 的编译环境，环境安装较为简单，这里给大家简要说明下。

	首先是下载ProtoBuf的安装包，然后解压，进入到解压目录，编译安装。命令如下：
		./configure --prefix=/home/work /protobuf/
		make && make install
	最后编译 .proto 文件的命令：
		protoc ./ResourceTracker.proto  --java_out=./

	下面，我们去收取Hadoop源码到本地工程，运行调试相关代码。
	TestYarnServerApiClasses：

		public class TestYarnServerApiClasses {

		  // ......

		  // 列举测试4个方法

		@Test
		  public void testRegisterNodeManagerResponsePBImpl() {
			RegisterNodeManagerResponsePBImpl original =
				new RegisterNodeManagerResponsePBImpl();
			original.setContainerTokenMasterKey(getMasterKey());
			original.setNMTokenMasterKey(getMasterKey());
			original.setNodeAction(NodeAction.NORMAL);
			original.setDiagnosticsMessage("testDiagnosticMessage");

			RegisterNodeManagerResponsePBImpl copy =
				new RegisterNodeManagerResponsePBImpl(
					original.getProto());
			assertEquals(1, copy.getContainerTokenMasterKey().getKeyId());
			assertEquals(1, copy.getNMTokenMasterKey().getKeyId());
			assertEquals(NodeAction.NORMAL, copy.getNodeAction());
			assertEquals("testDiagnosticMessage", copy.getDiagnosticsMessage());

		  }

		@Test
		  public void testNodeHeartbeatRequestPBImpl() {
			NodeHeartbeatRequestPBImpl original = new NodeHeartbeatRequestPBImpl();
			original.setLastKnownContainerTokenMasterKey(getMasterKey());
			original.setLastKnownNMTokenMasterKey(getMasterKey());
			original.setNodeStatus(getNodeStatus());
			NodeHeartbeatRequestPBImpl copy = new NodeHeartbeatRequestPBImpl(
				original.getProto());
			assertEquals(1, copy.getLastKnownContainerTokenMasterKey().getKeyId());
			assertEquals(1, copy.getLastKnownNMTokenMasterKey().getKeyId());
			assertEquals("localhost", copy.getNodeStatus().getNodeId().getHost());
		  }

		@Test
		  public void testNodeHeartbeatResponsePBImpl() {
			NodeHeartbeatResponsePBImpl original = new NodeHeartbeatResponsePBImpl();

			original.setDiagnosticsMessage("testDiagnosticMessage");
			original.setContainerTokenMasterKey(getMasterKey());
			original.setNMTokenMasterKey(getMasterKey());
			original.setNextHeartBeatInterval(1000);
			original.setNodeAction(NodeAction.NORMAL);
			original.setResponseId(100);

			NodeHeartbeatResponsePBImpl copy = new NodeHeartbeatResponsePBImpl(
				original.getProto());
			assertEquals(100, copy.getResponseId());
			assertEquals(NodeAction.NORMAL, copy.getNodeAction());
			assertEquals(1000, copy.getNextHeartBeatInterval());
			assertEquals(1, copy.getContainerTokenMasterKey().getKeyId());
			assertEquals(1, copy.getNMTokenMasterKey().getKeyId());
			assertEquals("testDiagnosticMessage", copy.getDiagnosticsMessage());
		  }

		@Test
		  public void testRegisterNodeManagerRequestPBImpl() {
			RegisterNodeManagerRequestPBImpl original = new RegisterNodeManagerRequestPBImpl();
			original.setHttpPort(8080);
			original.setNodeId(getNodeId());
			Resource resource = recordFactory.newRecordInstance(Resource.class);
			resource.setMemory(10000);
			resource.setVirtualCores(2);
			original.setResource(resource);
			RegisterNodeManagerRequestPBImpl copy = new RegisterNodeManagerRequestPBImpl(
				original.getProto());

			assertEquals(8080, copy.getHttpPort());
			assertEquals(9090, copy.getNodeId().getPort());
			assertEquals(10000, copy.getResource().getMemory());
			assertEquals(2, copy.getResource().getVirtualCores());

		  }

		}

	TestResourceTrackerPBClientImpl：

		public class TestResourceTrackerPBClientImpl {

			private static ResourceTracker client;
			private static Server server;
			private final static org.apache.hadoop.yarn.factories.RecordFactory recordFactory = RecordFactoryProvider
					.getRecordFactory(null);

			@BeforeClass
			public static void start() {

				System.out.println("Start client test");

				InetSocketAddress address = new InetSocketAddress(0);
				Configuration configuration = new Configuration();
				ResourceTracker instance = new ResourceTrackerTestImpl();
				server = RpcServerFactoryPBImpl.get().getServer(ResourceTracker.class, instance, address, configuration, null,
						1);
				server.start();

				client = (ResourceTracker) RpcClientFactoryPBImpl.get().getClient(ResourceTracker.class, 1,
						NetUtils.getConnectAddress(server), configuration);

			}

			@AfterClass
			public static void stop() {

				System.out.println("Stop client");

				if (server != null) {
					server.stop();
				}
			}

			/**
			 * Test the method registerNodeManager. Method should return a not null
			 * result.
			 *
			 */
			@Test
			public void testResourceTrackerPBClientImpl() throws Exception {
				RegisterNodeManagerRequest request = recordFactory.newRecordInstance(RegisterNodeManagerRequest.class);
				assertNotNull(client.registerNodeManager(request));

				ResourceTrackerTestImpl.exception = true;
				try {
					client.registerNodeManager(request);
					fail("there should be YarnException");
				} catch (YarnException e) {
					assertTrue(e.getMessage().startsWith("testMessage"));
				} finally {
					ResourceTrackerTestImpl.exception = false;
				}

			}

			/**
			 * Test the method nodeHeartbeat. Method should return a not null result.
			 *
			 */

			@Test
			public void testNodeHeartbeat() throws Exception {
				NodeHeartbeatRequest request = recordFactory.newRecordInstance(NodeHeartbeatRequest.class);
				assertNotNull(client.nodeHeartbeat(request));

				ResourceTrackerTestImpl.exception = true;
				try {
					client.nodeHeartbeat(request);
					fail("there  should be YarnException");
				} catch (YarnException e) {
					assertTrue(e.getMessage().startsWith("testMessage"));
				} finally {
					ResourceTrackerTestImpl.exception = false;
				}

			}

			public static class ResourceTrackerTestImpl implements ResourceTracker {

				public static boolean exception = false;

				public RegisterNodeManagerResponse registerNodeManager(RegisterNodeManagerRequest request)
						throws YarnException, IOException {
					if (exception) {
						throw new YarnException("testMessage");
					}
					return recordFactory.newRecordInstance(RegisterNodeManagerResponse.class);
				}

				public NodeHeartbeatResponse nodeHeartbeat(NodeHeartbeatRequest request) throws YarnException, IOException {
					if (exception) {
						throw new YarnException("testMessage");
					}
					return recordFactory.newRecordInstance(NodeHeartbeatResponse.class);
				}

			}
		}

4.截图预览
	接下来，我们使用JUnit去测试代码，截图预览如下所示：
	对testRegisterNodeManagerRequestPBImpl()方法的一个DEBUG调试
		http://images0.cnblogs.com/blog2015/666745/201507/211616290539418.png
	testResourceTrackerPBClientImpl()方法的DEBUG调试
		http://images0.cnblogs.com/blog2015/666745/201507/211619327407066.png
	这里由于设置exception的状态为true，在调用registerNodeManager()时，会打印一条测试异常信息。
		if (exception) {
		　　throw new YarnException("testMessage");
		}
5.总结
	在学习Hadoop YARN的RPC时，可以先了解Hadoop的RPC机制，
	这样在接触YARN的RPC的会比较好理解，YARN的RPC只是其中的一部分，后续会给大家分享更多关于YARN的内容。

















