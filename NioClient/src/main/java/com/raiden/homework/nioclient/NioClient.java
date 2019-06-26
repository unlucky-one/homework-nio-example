package com.raiden.homework.nioclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2019/6/26
 */
public class NioClient {
    public static void main(String[] args) {
        ObjectOutputStream outputStream;
        ObjectInputStream inputStream;
        try {
            Socket socket = new Socket("localhost", 8811);
//            outputStream = new ObjectOutputStream(socket.getOutputStream());
//            outputStream.writeBytes("Nio 测试！");
//            outputStream.flush();
            OutputStream out = socket.getOutputStream();
            out.write("Nio 测试！".getBytes());
            out.flush();
            out.close();
            socket.close();
//            inputStream = new ObjectInputStream(socket.getInputStream());
//            System.out.println(inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
