package br.com.neo.smsmanager.socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import br.com.mylocation.bean.message.Message;

public class ServerSocket
    implements Runnable {

    private static final int READ_BUFFER_SIZE = 1024;
    private static final int PORT_SERVER_SOCKET = 8000;
    private ControllerClient controllerClient;
    private ServerSocketChannel serverSocket;

    public ServerSocket(ControllerClient controllerClient) {
        this.controllerClient = controllerClient;
        new Thread(this).start();
    }

    private void loopSocket(ServerSocketChannel server, Selector selector) {
        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                continue;
            }
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = (SelectionKey) keyIterator.next();
                keyIterator.remove();

                if (key.isAcceptable()) {
                    accept(server, selector);
                    continue;
                }
                if (key.isReadable()) {
                    read(key);
                }
            }
        }
    }

    private void accept(ServerSocketChannel server, Selector selector) {
        SocketChannel socketClient = null;
        try {
            socketClient = server.accept();
            socketClient.configureBlocking(false);
            socketClient.register(selector, SelectionKey.OP_READ);
            controllerClient.newClient(socketClient);
        } catch (IOException e) {
            close(socketClient);
        }
    }

    private void read(SelectionKey key) {
        SocketChannel socketClient = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(READ_BUFFER_SIZE);

        try {
            if ((socketClient.read(buffer) <= 0)) {
                close(socketClient);
            } else {
                ByteArrayInputStream byteInput = new ByteArrayInputStream(buffer.array());
                ObjectInputStream input = new ObjectInputStream(byteInput);
                Object object = input.readObject();
                if (object instanceof Message) {
                    Message message = (Message) object;
                    controllerClient.receiveMessage(socketClient, message);
                    input.close();
                } else {
                    input.close();
                    close(socketClient);
                }
            }
        } catch (IOException e1) {
            close(socketClient);
        } catch (ClassNotFoundException e) {
            close(socketClient);
        }
    }

    public void write(SocketChannel socketClient, Message message) {
        try {
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(byteOutput);
            output.writeObject(message);
            output.flush();
            ByteBuffer buffer = ByteBuffer.wrap(byteOutput.toByteArray());
            socketClient.write(buffer);
        } catch (IOException e1) {
            close(socketClient);
        }
    }

    private void close(SocketChannel socketClient) {
        controllerClient.killClient(socketClient);
    }

    @Override
    public void run() {
        serverSocket = null;
        InetSocketAddress address = null;
        Selector selector = null;

        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.configureBlocking(false);
            address = new InetSocketAddress(PORT_SERVER_SOCKET);
            serverSocket.socket().bind(address);
            selector = Selector.open();
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Socket na porta " + PORT_SERVER_SOCKET + " aberto.");
        } catch (IOException e) {
        }

        loopSocket(serverSocket, selector);
    }

    public void destroy() {
        try {
            serverSocket.close();
            System.out.println("Socket na porta " + PORT_SERVER_SOCKET + " fechado.");
        } catch (IOException e) {
        }
    }
}
