package BIO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 阻塞式IO，服务端
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9090);
        System.out.println("服务端已启动...");

        while (true) {
            final Socket client = serverSocket.accept(); // 阻塞方法
            System.out.println("收到一个新的客户端，" + client.getPort());

            new Thread(new Runnable() {
                public void run() {
                    BufferedReader bufferedReader = null;
                    PrintStream printStream = null;
                    try {
                        while (!Thread.currentThread().isInterrupted() && !client.isClosed()) {
                            InputStream inputStream = client.getInputStream();
                            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                            String line = bufferedReader.readLine(); // 阻塞
                            System.out.println(line);

                            printStream = new PrintStream(client.getOutputStream());
                            printStream.println("你好，client");
                        }
//                        System.out.println("客户端断开连接");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        printStream.close();
                    }
                }
            }).start();
        }


    }
}
