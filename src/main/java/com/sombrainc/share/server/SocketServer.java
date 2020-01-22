package com.sombrainc.share.server;

import com.sombrainc.share.enumeration.SocketConnections;
import com.sombrainc.share.server.connection.SocketConnection;
import com.sombrainc.share.server.connection.impl.MousePointerProviderConnection;
import com.sombrainc.share.server.connection.impl.ScreenReaderConnection;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.sombrainc.share.enumeration.SocketConnections.SOCKET_MOUSE;
import static com.sombrainc.share.enumeration.SocketConnections.SOCKET_SCREEN;
import static com.sombrainc.share.util.SocketUtils.sendMessage;

public class SocketServer extends Application {
    private ImageView imageView;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        final SocketServer socketServer = new SocketServer();
        showStage(primaryStage);
        socketServer.start();
    }

    private void showStage(Stage primaryStage) {
        imageView = new ImageView();

        StackPane root = new StackPane();
        root.getChildren().add(imageView);

        Scene scene = new Scene(root, 800, 800);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String argLine = reader.readLine();
            System.out.printf("Received line: %s\n", argLine);
            final SocketConnection socketConnection = lookForConnection(argLine);
            if (socketConnection == null) {
                sendMessage(socket.getOutputStream(), "BAD");
            } else {
                sendMessage(socket.getOutputStream(), String.format("OK\n%s", argLine));
                CompletableFuture.runAsync(() -> socketConnection.consume(socket));
            }
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

}
