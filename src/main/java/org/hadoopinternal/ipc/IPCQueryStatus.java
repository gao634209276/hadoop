package org.hadoopinternal.ipc;


/**
 * 由于客户端要调用服务器的方法，所以客户端需要知道服务器有哪些方法可以调用， 在IPC中使用的是定义公共接口的方法，如定义一个IPC接口，
 * 客户端和服务器端都知道这个接口，客户端通过IPC获取到一个服务器端这个实现了接口的引用，
 * 待要调用服务器的方法时，直接使用这个引用来调用方法，这样就可以调用服务器的方法了 。
 */
// 定义一个服务器端和客户端共有的接口IPCQueryStatus
public interface IPCQueryStatus {
	// 根据文件名得到一个IPCFileStatus对象
	public static final long versionID = 1L;
	IPCFileStatus getFileStatus(String filename);
}
