package com.sombrainc.share.util;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class SocketUtils {

    private SocketUtils() {
    }

    public static void sendMessage(final OutputStream out, final String message) {
        try (PrintWriter writer = new PrintWriter(new BufferedOutputStream(out))) {
            writer.println();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
