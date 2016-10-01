http://www.sjsjw.com/kf_cloud/article/92_10408_7543.asp

在Hadoop中，不同节点间使用RPC机制进行通信，
在Java中比较典型的RPC是RMI(Remote Method Invocation)的调用方式，
虽然Hadoop使用Java语言实现的，但是Hadoop并没有使用RMI实现RPC，
而是实现了一套自己独有的节点通信机制，称为Hadoop IPC
（Inter-Process Communication，进程间通信）。
这是一种简洁，低消耗的通信机制，可以精确控制进程间通信中如连接、
超时、缓存等细节。Hadoop IPC机制的实现使用了Java动态代理，Java NIO等技术。
关于Java动态代理技术可以参考博文Java动态代理，Java NIO技术可以参考博文Java NIO。 
根据Hadoop提供的IPC机制，下面就来着手开发一个
使用Hadoop IPC实现客户端调用服务器端方法的示例，
这个示例源于《Hadoop技术内幕：深入解析Hadoop Common和HDFS架构设计与实现原理》这本书，
功能是返回服务器端的一个文件信息

