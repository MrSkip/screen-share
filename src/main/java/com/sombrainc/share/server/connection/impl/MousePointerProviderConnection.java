package com.sombrainc.share.server.connection.impl;

import com.sombrainc.share.server.connection.SocketConnection;
import com.sombrainc.share.util.SocketUtils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MousePointerProviderConnection implements SocketConnection {

    @Override
    public void consume(Socket socket, final InputStream in, final OutputStream out) {
        System.out.println("Received connection for mouse pointer");
        Point location = MouseInfo.getPointerInfo().getLocation();
        SocketUtils.sendMessage(out, location.x + ";" + location.y);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
