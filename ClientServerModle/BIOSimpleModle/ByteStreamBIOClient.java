/*
1. 功能描述：Client使用字节流传输信息，发送给Server一条信息并接收Server一条信息，
并注释了告知对方已发送完命令的4种方式。
*/

import java.io.OutputStream;
import java.net.*;

public class ByteStreamBIOClient {
    public static void main(String args[]) throws Exception {
        // 要连接的服务端IP地址和端口
        String host = "127.0.0.1";
        int port = 9999;

        // 与服务端建立连接
        Socket socket = new Socket(host, port);

        // 建立连接后从socket获得输出流
        OutputStream outputStream = socket.getOutputStream();
        String message = "你好，Server";
        socket.getOutputStream().write(message.getBytes("UTF-8"));
        /*
        告知对方已发送完命令:
        1. 通过socket关闭，socket.close()。

        2. 通过socket关闭输出流，socket.shutdownOutput()。

        3. 这里也可以采用约定符号告知对方已发送完命令（这里用字节流我尚未实现）：
        Client：
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            String message = "你好，Server!"+ "\n" + "quit";
            printWriter.println(message);
            
        Server：
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = read.readLine()) != null && !"quit".equals(line)) {
                sb.append(line);
            }
        
            4. 根据长度界定。
        送一次消息变成了两个步骤：
            发送消息的长度
            发送消息

        最后的问题就是，“发送消息的长度”这一步骤所发送的字节量必须是固定的，否则我们仍然会陷入僵局。
        一般来说，我们可以使用固定的字节数来保存消息的长度，比如规定前2个字节就是消息的长度，
        不过这样我们能够传送的消息最大长度也就被固定死了，以2个字节为例，我们发送的消息最大长度不超过2^16个字节即64K。
        Client：
            // 首先要把message转换成bytes以便处理
            byte[] bytes = message.getBytes("UTF-8");
            // 接下来传输两个字节的长度，使用移位实现
            int length = bytes.length;
            outputStream.write(length >> 8); // write默认一次只传输一个字节
            outputStream.write(length);
            // 传输完长度后，再正式传送消息
            outputStream.write(bytes);

        Server：
             // 先读第一个字节
            int first = inputStream.read();
            if (first == -1) {
                // 如果是-1，说明输入流已经被关闭了，也就不需要继续监听了
                this.socket.close();
                break;
            }
            // 读取第二个字节
            int second = inputStream.read();
            int length = (first << 8) + second; // 用位运算将两个字节拼起来成为真正的长度
            bytes = new byte[length]; // 构建指定长度的字节大小来储存消息即可
            inputStream.read(bytes);

            System.out.println("receive message: " + new String(bytes,"UTF-8"));
        */
        socket.shutdownOutput();

        //从socket获得输入流
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = inputStream.read(bytes)) != -1) {
            //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
            sb.append(new String(bytes, 0, len,"UTF-8"));
        }
        System.out.println("get message from server: " + sb);

        inputStream.close();
        outputStream.close();
        socket.close();
        inputStream.close();
    }
}