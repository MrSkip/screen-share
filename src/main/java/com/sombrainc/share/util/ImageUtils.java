package com.sombrainc.share.util;

import javax.imageio.IIOException;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import static javax.imageio.ImageIO.createImageOutputStream;
import static javax.imageio.ImageIO.getImageWriters;

public class ImageUtils {

    private ImageUtils() {
    }

    public static boolean write(RenderedImage im,
                                String formatName,
                                OutputStream output) throws IOException {
        if (output == null) {
            throw new IllegalArgumentException("output == null!");
        }
        ImageOutputStream imageOutputStream = createImageOutputStream(output);
        if (imageOutputStream == null) {
            throw new IIOException("Can't create an ImageOutputStream!");
        }
//        return doWrite(im, getWriter(im, formatName), imageOutputStream);
        try {
            return doWrite(im, getWriter(im, formatName), imageOutputStream);
        } finally {
            imageOutputStream.close();
        }
    }

    private static ImageWriter getWriter(RenderedImage im,
                                         String formatName) {
        ImageTypeSpecifier type =
                ImageTypeSpecifier.createFromRenderedImage(im);
        Iterator<ImageWriter> iter = getImageWriters(type, formatName);

        if (iter.hasNext()) {
            return iter.next();
        } else {
            return null;
        }
    }

    private static boolean doWrite(RenderedImage im, ImageWriter writer,
                                   ImageOutputStream outputStream) throws IOException {
        if (writer == null) {
            return false;
        }
        writer.setOutput(outputStream);
        try {
            writer.write(im);
        } finally {
            writer.dispose();
            outputStream.flush();
        }
        return true;
    }

}
