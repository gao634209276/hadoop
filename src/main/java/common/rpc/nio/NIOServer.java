package common.rpc.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import common.rpc.ConfigureAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Defined nio server
 */
public class NIOServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(NIOServer.class);

	// The channel manager
	private Selector selector;

	/**
	 * Get ServerSocket channel and initialize
	 * <p>
	 * 1.Get a ServerSocket channel
	 * <p>
	 * 2.Set channel for non blocking
	 * <p>
	 * 3.The channel corresponding to the ServerSocket binding to port port
	 * <p>
	 * 4.Get a channel manager
	 * <p>
	 * 5.The channel manager and the channel binding, and the channel registered
	 * SelectionKey.OP_ACCEPT event
	 *
	 * @param port
	 * @throws IOException
	 */
	public void init(int port) throws IOException {
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.socket().bind(new InetSocketAddress(port));
		this.selector = Selector.open();
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

	/**
	 * listen selector
	 *
	 * @throws IOException
	 */
	public void listen() throws IOException {
		LOGGER.info("Server has start success");
		while (true) {
			selector.select();
			Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
			while (ite.hasNext()) {
				SelectionKey key = (SelectionKey) ite.next();
				ite.remove();
				if (key.isAcceptable()) {
					ServerSocketChannel server = (ServerSocketChannel) key.channel();
					SocketChannel channel = server.accept();
					channel.configureBlocking(false);// 非阻塞
					channel.write(ByteBuffer.wrap(new String("Send test info to client").getBytes()));
					channel.register(this.selector, SelectionKey.OP_READ);// 设置读的权限
				} else if (key.isReadable()) {
					read(key);
				}
			}
		}
	}

	/**
	 * Deal client send event
	 */
	public void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		channel.read(buffer);
		byte[] data = buffer.array();
		String info = new String(data).trim();
		LOGGER.info("Server receive info : " + info);
		ByteBuffer outBuffer = ByteBuffer.wrap(info.getBytes());
		channel.write(outBuffer);// 将消息回送给客户端
	}

	public static void main(String[] args) {
		try {
			NIOServer server = new NIOServer();
			server.init(ConfigureAPI.ServerAddress.NIO_PORT);
			server.listen();
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error("NIOServer main run error,info is " + ex.getMessage());
		}
	}
}
