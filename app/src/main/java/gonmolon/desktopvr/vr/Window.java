package gonmolon.desktopvr.vr;

import android.util.Log;

public class Window extends ParentLayout {

    private WindowsManager windowsManager;
    private int PID;
    private Menu menu;
    private WindowContent content;

    public Window(final WindowsManager windowsManager, float width, float height, final int PID) {
        super(windowsManager.getRenderer(), width, height, LayoutParams.VERTICAL);
        this.windowsManager = windowsManager;
        this.PID = PID;
        menu = new Menu(this);
        content = new WindowContent(this, width, height-Menu.HEIGHT) {
            @Override
            public void onClick() {
                windowsManager.setWindowFocused(PID);
            }
        };
    }

    public void close() {
        try {
            windowsManager.removeWindow(PID);
        } catch (WindowsManagerException e) {
            e.printStackTrace();
        }
    }

    public int getPID() {
        return PID;
    }
}
