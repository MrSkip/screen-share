package com.sombrainc.share.client;

import com.sombrainc.share.capture.CaptureFactory;
import com.sombrainc.share.enumeration.SocketConnections;
import com.sombrainc.share.util.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Client {
    private final String ip;
    private final int port;

    public Client() {
        this.ip = "127.0.0.1";
        this.port = 8082;
    }

    public static void main(String[] args) throws IOException {
        final Client client = new Client();
        client.mouseConsumer();
//        CompletableFuture.runAsync(client::screenWriter);
//        CompletableFuture.runAsync(client::mouseConsumer);
    }

    private void mouseConsumer() {
        try {
            while (true) {
                TimeUnit.MILLISECONDS.sleep(33);

                Socket socket = new Socket(ip, port);
                OutputStream output = socket.getOutputStream();

                PrintWriter writer = new PrintWriter(new BufferedOutputStream(output), true);
                writer.println(SocketConnections.SOCKET_MOUSE.name());
                writer.println("Test");

                final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                if ("OK".equalsIgnoreCase(reader.readLine())) {
                    System.out.println("Good request for mouse");
                    final String m = reader.readLine();
                    final String[] split = m.split(";");
                    Robot robot = new Robot();
                    robot.mouseMove(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                } else {
                    System.out.println("Bad request for mouse");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void screenWriter() {
        try {
            while (true) {
                TimeUnit.MILLISECONDS.sleep(32);

                Socket socket = new Socket(ip, port);
                OutputStream output = socket.getOutputStream();

                PrintWriter writer = new PrintWriter(new BufferedOutputStream(output), true);
                writer.println(SocketConnections.SOCKET_SCREEN.name());

                BufferedImage bufferedImage = CaptureFactory.getScreenshotCapture().getScreenshot();
                ImageUtils.write(bufferedImage, "PNG", output);

                String result = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                        .lines().collect(Collectors.joining("\n"));
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
