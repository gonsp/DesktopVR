package gonmolon.desktopvr.vr;

public class Window extends ParentLayout {

    private WindowsManager windowsManager;
    private int PID;
    private Menu menu;
    private WindowContent content;

    public Window(WindowsManager windowsManager, float width, float height, int PID) {
        super(windowsManager.getRenderer(), width, height, LayoutParams.VERTICAL);
        this.windowsManager = windowsManager;
        this.PID = PID;
        menu = new Menu(this);
        content = new WindowContent(this, width, height-Menu.HEIGHT);
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
