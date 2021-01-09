package MultiNIO;


import com.sun.security.ntlm.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 多路复用的NIO, 单线程版本
 */
public class MultiNIOServer {

    static LinkedBlockingQueue<SocketChannel> clients = new LinkedBlockingQueue<SocketChannel>();

    public static void main(String[] args) throws Exception {
        ServerSocketChannel server = ServerSocketChannel.open();
        Selector selector = Selector.open();
        server.bind(new InetSocketAddress(9090));
        server.configureBlocking(false);// 设置非阻塞
        server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务端启动成功...");


        while (true) {

            if (selector.select(0) > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    iterator.remove();
                    if (next.isAcceptable()) {
                        handleAccept(next);
                    } else if (next.isReadable()) {
                        handleRead(next);
                    }
                }
            }


        }
    }

    public static void handleAccept(SelectionKey key) throws Exception {
        ServerSocketChannel ss = (ServerSocketChannel) key.channel();
        SocketChannel client = ss.accept(); // 非阻塞

        System.out.println("收到一个客户端请求，端口= " + client.socket().getPort());
        client.configureBlocking(false);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8192);
        client.register(key.selector(), SelectionKey.OP_READ, byteBuffer); // 客户端的channel 和 buffer绑定一起

    }

    public static void handleRead(SelectionKey key) throws Exception {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();

        int read = 0;

        while (true) {
            read = client.read(buffer);
            if (read > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    client.write(buffer);
                }
                buffer.clear();
            } else if (read == 0) {
                break;
            } else {// -1
                client.close();
                break;
            }
        }


    }
}
