package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 单线程的NIO,没有使用多路复用器selector
 */
public class NIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel client = SocketChannel.open();
        client.connect(new InetSocketAddress("localhost", 9090));
        client.configureBlocking(false); // 设置非阻塞

        System.out.println("客户端创建成功");

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4096);
        byteBuffer.put("i am a client".getBytes());
        byteBuffer.flip();
        client.write(byteBuffer);



        while (true){

        }


    }
}
