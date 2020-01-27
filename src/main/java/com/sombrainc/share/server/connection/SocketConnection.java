package com.sombrainc.share.server.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface SocketConnection {

    void consume(Socket socket, final InputStream in, final OutputStream out);

}
