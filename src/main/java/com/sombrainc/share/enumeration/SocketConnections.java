package com.sombrainc.share.enumeration;

public enum SocketConnections {

    SOCKET_SCREEN,
    SOCKET_MOUSE;;

    public static SocketConnections of(final String name) {
        for (SocketConnections value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

}
