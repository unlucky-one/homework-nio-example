package com.raiden.homework.nioserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2019/6/26
 */
public class NioServer {

    Selector selector;
    ByteBuffer buffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) {
        new NioServer(8811).listen();
    }

    public NioServer(int port) {
        try {
            ServerSocketChannel socketChannel = ServerSocketChannel.open();
            socketChannel.socket().bind(new InetSocketAddress(port));
            socketChannel.configureBlocking(false);
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        while (true) {
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    process(key);
                }

            } catch (Exception e) {

            }

        }
    }

    private void process(SelectionKey key) {
        try {
            if (key.isAcceptable()) {
                ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
                SocketChannel channel = socketChannel.accept();
//                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ,buffer);

            } else if (key.isReadable()) {
                SocketChannel channel = (SocketChannel) key.channel();
                ByteBuffer byteBuffer = (ByteBuffer)key.attachment();
                byteBuffer.clear();

                int len = channel.read(byteBuffer);
                if (len > 0) {
                    byteBuffer.flip();
                    String str = new String(byteBuffer.array(), 0, len);
                    System.out.println(str);
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_WRITE);
                    key.attach(str + " " + System.currentTimeMillis());
                }
            } else if (key.isWritable()) {
                SocketChannel channel = (SocketChannel) key.channel();
                String str = (String) key.attachment();
                channel.write(ByteBuffer.wrap(str.getBytes()));
                channel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
