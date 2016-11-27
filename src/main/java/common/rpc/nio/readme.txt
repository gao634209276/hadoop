对Hadoop V2的RPC机制做进一步探索，在研究Hadoop V2的RPC机制，
我们需要掌握相关的Java基础知识，如：Java NIO、动态代理与反射等。本篇博客介绍的内容目录如下所示：
	Java NIO简述
	Java NIO实例演示
	动态代理与反射简述
	动态代理与反射实例演示
	Hadoop V2 RPC框架使用实例
下面开始今天的博客介绍。
2.Java NIO简述
	Java NIO又称Java New IO，它替代了Java IO API，提供了与标准IO不同的IO工作方式。
	Java NIO由一下核心组件组成：
		Channels：连接通道，即能从通道读取数据，又能写数据到通道。可以异步读写，读写从Buffer开始。
		Buffers：消息缓冲区，用于和NIO通道进行交互。
		所谓缓冲区，它是一块可以读写的内存，该内存被封装成NIO的Buffer对象，并提供相应的方法，以便于访问。
		Selectors：通道管理器，它能检测到Java NIO中多个通道，单独的线程可以管理多个通道，间接的管理多个网络连接。
	下图为Java NIO的工作原理图，如下图所示：
	See.2.png
首先，我们来看NIOServer的代码块。代码内容如下所示：
NIOClient
然后，我们在来看NIOClient的代码块，代码具体内容如下所示：
ConfigureAPI
下面给出ConfigureAPI类的代码，内容如下所示：
