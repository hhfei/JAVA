package BIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 阻塞式IO，客户端
 */
public class BIOClient {
    public static void main(String[] args) throws IOException {
        final Socket client = new Socket("localhost", 9090);
        System.out.println("客户端"+client.getPort()+"启动了");
        new Thread(new Runnable() {
            public void run() {
                BufferedReader bufferedReader = null;
                PrintStream printStream = null;
                try {
                    while (true){
                        printStream = new PrintStream(client.getOutputStream());
                        String input = new BufferedReader(new InputStreamReader(System.in)).readLine();
                        printStream.println("client:    " + input);

                        bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        String resp = bufferedReader.readLine();
                        System.out.println("server: "+resp);
                    }



                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        bufferedReader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    printStream.close();
                }


            }
        }).start();


    }
}
