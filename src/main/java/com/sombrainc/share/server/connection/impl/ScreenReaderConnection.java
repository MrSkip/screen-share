package com.sombrainc.share.server.connection.impl;

import com.sombrainc.share.server.connection.SocketConnection;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ScreenReaderConnection implements SocketConnection {
    private final ImageView imageView;

    public ScreenReaderConnection(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void consume(Socket socket, final InputStream in, final OutputStream out) {
        System.out.println("Received connection for screen");
        try {
            BufferedImage read = ImageIO.read(in);
            imageView.setImage(SwingFXUtils.toFXImage(read, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
