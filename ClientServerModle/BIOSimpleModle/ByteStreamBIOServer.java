/*
1. 功能描述：Server使用字节流传输信息，接收Client一条信息并发回Client一条信息。
*/

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ByteStreamBIOServer {
    public static void main(String args[]) throws Exception {
        int port = 9999;
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();

        // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = inputStream.read(bytes)) != -1) {
            //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
            sb.append(new String(bytes, 0, len, "UTF-8"));
        }
        System.out.println("get message from client: " + sb);

        //从socket获得输出流
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("Hello Client,I get the message.".getBytes("UTF-8"));

        outputStream.close();
        inputStream.close();
        socket.close();
        serverSocket.close();
    }
}