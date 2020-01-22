package com.sombrainc.share.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    public static BufferedImage mergeScreenshotAndCursor(final BufferedImage screenshot, final Image cursor) {
        int x = MouseInfo.getPointerInfo().getLocation().x;
        int y = MouseInfo.getPointerInfo().getLocation().y;

        Graphics2D graphics2D = Objects.requireNonNull(screenshot).createGraphics();
        // cursor.gif is 16x16 size.
        graphics2D.drawImage(Objects.requireNonNull(cursor), x, y, 16, 16, null);

        return screenshot;
    }

}
