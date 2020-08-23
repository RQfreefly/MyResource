/*
1. 功能描述：不断监听发送来的消息的端口和消息，并转发回Client
当消息等于quit的时候，Server最后退出
*/

import java.io.*;
import java.net.*;

public class CharStreamBIOServer {
    public static void main(String[] args) {
        final int port = 8888;
        //创建ServerSocket监听8888端口
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("ServerSocket Start,The Port is:" + port);
            while (true) {//不停地监听该端口
                //阻塞式的监听，如果没有客户端请求就一直停留在这里
                Socket socket = serverSocket.accept();
                System.out.println("Client[" + socket.getPort() + "]Online");
                //接收消息
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                //发送消息
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())
                );

                String msg = null;
                while ((msg = reader.readLine()) != null) {
                    System.out.println("Client[" + socket.getPort() + "]:" + msg);
                    //写入服务端要发送的消息
                    writer.write("Server:" + msg + "\n");
                    writer.flush();
                    //如果客户端的消息是quit代表他退出了，并跳出循环，不用再接收他的消息了。如果客户端再次连接就会重新上线
                    if (msg.equals("quit")) {
                        System.out.println("Client[" + socket.getPort() + "]:Offline");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}