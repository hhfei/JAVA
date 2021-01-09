package NIO;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 单线程的NIO,没有使用多路复用器selector
 */
public class NIOServer {

    static LinkedBlockingQueue<SocketChannel> clients = new LinkedBlockingQueue<SocketChannel>();

    public static void main(String[] args) throws Exception {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(9090));
        server.configureBlocking(false);// 设置非阻塞
        System.out.println("服务端启动成功...");


        while (true) {
            Thread.sleep(2000);
            SocketChannel client = server.accept(); // 非阻塞
            if (client == null) {
                System.out.println("等待客户端请求...");
            } else {
                System.out.println("收到一个客户端请求，端口= " + client.socket().getPort());
                client.configureBlocking(false);
                clients.add(client);
            }

            for (SocketChannel itemClient : clients) {
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8192);
                    int read = itemClient.read(byteBuffer); // >0 0  -1
                    if (read > 0) {
                        byteBuffer.flip();
                        byte[] reqBytes = new byte[byteBuffer.limit()];
                        byteBuffer.get(reqBytes);
                        System.out.println("client-" + itemClient.socket().getPort() + "发送：" + new String(reqBytes));
                    }

            }
        }

    }


    public void handleRequest() {

    }
}
