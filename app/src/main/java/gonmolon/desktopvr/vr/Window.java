package gonmolon.desktopvr.vr;

import android.graphics.Bitmap;

public final class Window extends ParentLayout {

    private WindowManager windowManager;
    private int PID;
    private Menu menu;
    private WindowContent content;

    public Window(final WindowManager windowManager, float width, float height, final int PID) {
        super(windowManager.getRenderer(), width, height, LayoutParams.VERTICAL);
        this.windowManager = windowManager;
        this.PID = PID;
        menu = new Menu(this);
        content = new WindowContent(this, width, height-Menu.HEIGHT);
    }

    public void updateFrame(Bitmap frame) {
        content.updateFrame(frame);
    }

    public void focus() {
        windowManager.setWindowFocused(PID);
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
}
