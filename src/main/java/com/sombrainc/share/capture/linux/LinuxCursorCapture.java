package com.sombrainc.share.capture.linux;

import com.sombrainc.share.capture.ICursorCapture;
import com.sun.jna.*;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.ptr.NativeLongByReference;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LinuxCursorCapture implements ICursorCapture {

    @Override
    public Image loadCursorImage() {
        return getCursorImage();
    }

    private static BufferedImage getCursorImage() {
        X11 x11 = X11.INSTANCE;
        Xfixes xfixes = Xfixes.INSTANCE;

        X11.Display display = x11.XOpenDisplay(null);

        Xfixes.XFixesCursorImage cursorImage = xfixes.XFixesGetCursorImage(display);

        ByteBuffer buf = cursorImage.pixels.getPointer().getByteBuffer(0,
                cursorImage.width * cursorImage.height * NativeLong.SIZE);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        BufferedImage bim = new BufferedImage(cursorImage.width, cursorImage.height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = bim.getRaster();
        for (int y = 0; y < cursorImage.height; y++) {
            for (int x = 0; x < cursorImage.width; x++) {
                long z = NativeLong.SIZE == 8 ? buf.getLong() : buf.getInt();
                int b = (int) ((z >> 24) & 0xFF);
                int a = (int) ((z >> 16) & 0xFF);
                int g = (int) ((z >> 8) & 0xFF);
                int r = (int) (z & 0xFF);
                raster.setPixel(x, y, new int[] { a, r, g, b });
            }
        }

        x11.XCloseDisplay(display);
        return bim;
    }

    public interface Xfixes extends Library {

        Xfixes INSTANCE = Native.load("Xfixes", Xfixes.class);

        @Structure.FieldOrder({ "x", "y", "width", "height", "xhot", "yhot", "cursor_serial", "pixels", "atom", "name"})
        class XFixesCursorImage extends Structure {
            public short x;
            public short y;
            public short width;
            public short height;
            public short xhot;
            public short yhot;
            public NativeLong cursor_serial;

            public NativeLongByReference pixels;

            public NativeLong atom;
            public Pointer name;

            public XFixesCursorImage() {
                super();
            }
        }

        XFixesCursorImage XFixesGetCursorImage(X11.Display dpy);
    }


}
