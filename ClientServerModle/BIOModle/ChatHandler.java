/*
1. 功能实现：这个类就是工作线程的类。在这个项目中，它的工作很简单：
把接收到的消息转发给其他客户端，当然还有一些小功能，比如添加\移除在线用户。
*/

import java.io.*;
import java.net.*;

public class ChatHandler implements Runnable {
    private ChatServer server;
    private Socket socket;

    //构造函数，ChatServer通过这个分配Handler线程
    public ChatHandler(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //往map里添加这个客户端
            server.addClient(socket);
            //读取这个客户端发送的消息
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            String msg = null;
            while ((msg = reader.readLine()) != null) {
                //这样拼接是为了让其他客户端也能看清是谁发送的消息
                String sendmsg = "Client[" + socket.getPort() + "]:" + msg;
                //服务器打印这个消息
                System.out.println(sendmsg);
                //将收到的消息转发给其他在线客户端
                server.sendMessage(socket, sendmsg + "\n");
                if (msg.equals("quit")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //如果用户退出或者发生异常，就在map中移除该客户端
            try {
                server.removeClient(socket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}