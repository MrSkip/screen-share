package com.sombrainc.share.capture;

import com.sombrainc.share.capture.linux.LinuxCursorCapture;
import com.sombrainc.share.capture.linux.LinuxScreenshotCapture;

// TODO: 1/21/2020 add based on OS
public class CaptureFactory {

    public static ICursorCapture getCursorCapture() {
        return new LinuxCursorCapture();
    }

    public static IScreenshotCapture getScreenshotCapture() {
        return new LinuxScreenshotCapture();
    }

}
