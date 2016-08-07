package gonmolon.desktopvr.vr;

import android.graphics.Bitmap;

public final class Window extends ParentLayout {

    private WindowManager windowManager;
    private int PID;
    private int pixelsWidth;
    private int pixelsHeight;
    private Menu menu;
    private WindowContent content;

    public Window(final WindowManager windowManager, int width, int height, final int PID) {
        super(windowManager.getRenderer(), (WindowContent.HEIGHT/height)*width, WindowContent.HEIGHT+Menu.HEIGHT, LayoutParams.VERTICAL);
        this.windowManager = windowManager;
        this.PID = PID;
        pixelsWidth = width;
        pixelsHeight = height;
        menu = new Menu(this);
        content = new WindowContent(this, width, height, windowManager.getVNCClient());
    }

    public void updateFrame(Bitmap frame) {
        content.updateFrame(frame);
    }

    public void refresh() {
        content.refresh();
    }

    public void focus() {
        windowManager.focusWindow(PID);
    }

    public boolean isFocused() {
        return windowManager.getFocused() == this;
    }

    public void close() {
        try {
            windowManager.removeWindow(PID);
        } catch (WindowManagerException e) {
            e.printStackTrace();
        }
    }

    public int getPID() {
        return PID;
    }

    public int getPixelsWidth() {
        return pixelsWidth;
    }

    public int getPixelsHeight() {
        return pixelsHeight;
    }
}
