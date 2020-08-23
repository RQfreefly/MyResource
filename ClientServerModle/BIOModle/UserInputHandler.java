/*
1. 功能实现：专门负责等待用户输入信息的线程，一旦有信息键入，就马上发送给服务器。
*/

import java.io.*;

public class UserInputHandler implements Runnable {
    private ChatClient client;

    public UserInputHandler(ChatClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            //接收用户输入的消息
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            //不停的获取reader中的System.in，实现了等待用户输入的效果
            while (true) {
                String input = reader.readLine();
                //向服务器发送消息
                client.sendToServer(input);
                if (input.equals("quit"))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}