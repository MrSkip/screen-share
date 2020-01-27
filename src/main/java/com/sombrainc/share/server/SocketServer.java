package com.sombrainc.share.server;

import com.sombrainc.share.enumeration.SocketConnections;
import com.sombrainc.share.server.connection.SocketConnection;
import com.sombrainc.share.server.connection.impl.MousePointerProviderConnection;
import com.sombrainc.share.server.connection.impl.ScreenReaderConnection;
import javafx.scene.image.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static com.sombrainc.share.enumeration.SocketConnections.SOCKET_MOUSE;
import static com.sombrainc.share.enumeration.SocketConnections.SOCKET_SCREEN;
import static com.sombrainc.share.util.SocketUtils.sendMessage;

public class SocketServer {
    private final ImageView imageView;

    public SocketServer(ImageView imageView) {
        this.imageView = imageView;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(8082)) {
            while (true) {
                Socket socket = serverSocket.accept();
                handleConnection(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleConnection(Socket socket) throws IOException {
        final InputStream inputStream = socket.getInputStream();
        String argLine = readLine(inputStream);
        System.out.printf("Received line: %s\n", argLine);
        final SocketConnection socketConnection = lookForConnection(argLine);
        final OutputStream outputStream = socket.getOutputStream();
        if (socketConnection == null) {
            sendMessage(outputStream, "BAD");
        } else {
            sendMessage(outputStream, String.format("OK\n%s", argLine));
            CompletableFuture.runAsync(() -> socketConnection.consume(socket, inputStream, outputStream));
        }
    }

    private SocketConnection lookForConnection(final String argLine) {
        SocketConnections connection = SocketConnections.of(argLine);
        if (SOCKET_SCREEN == connection) {
            return new ScreenReaderConnection(imageView);
        } else if (SOCKET_MOUSE == connection) {
            return new MousePointerProviderConnection();
        }
        return null;
    }

    public String readLine(InputStream inputStream) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int r;

        for (r = inputStream.read(); r != '\n' && r != -1; r = inputStream.read()) {
            baos.write(r);
        }

        if (r == -1 && baos.size() == 0) {
            return null;
        }

        return baos.toString(StandardCharsets.UTF_8);
    }

}
