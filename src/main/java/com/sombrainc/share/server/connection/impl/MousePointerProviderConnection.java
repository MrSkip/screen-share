package com.sombrainc.share.server.connection.impl;

import com.sombrainc.share.server.connection.SocketConnection;
import com.sombrainc.share.util.SocketUtils;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class MousePointerProviderConnection implements SocketConnection {

    @Override
    public void consume(final Socket socket) {
        System.out.println("Received connection for mouse pointer");
        try {
            Point location = MouseInfo.getPointerInfo().getLocation();
            SocketUtils.sendMessage(socket.getOutputStream(), location.x + ";" + location.y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
