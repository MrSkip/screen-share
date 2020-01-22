package com.sombrainc.share.server.connection.impl;

import com.sombrainc.share.server.connection.SocketConnection;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;

public class ScreenReaderConnection implements SocketConnection {
    private final ImageView imageView;

    public ScreenReaderConnection(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void consume(final Socket socket) {
        System.out.println("Received connection for screen");
        try {
            BufferedImage read = ImageIO.read(socket.getInputStream());
            imageView.setImage(SwingFXUtils.toFXImage(read, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
