package com.sombrainc.share.server.connection;

import java.net.Socket;

public interface SocketConnection {

    void consume(final Socket socket);

}
