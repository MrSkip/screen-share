package com.sombrainc.share.capture.linux;

import com.sombrainc.share.capture.IScreenshotCapture;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LinuxScreenshotCapture implements IScreenshotCapture {

    @Override
    public BufferedImage getScreenshot() {
        Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        try {
            return new Robot().createScreenCapture(screen);
        } catch (AWTException e) {
            throw new RuntimeException("Wasn't able to capture screenshot", e);
        }
    }

}
