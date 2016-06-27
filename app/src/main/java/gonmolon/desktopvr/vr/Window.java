package gonmolon.desktopvr.vr;

public class Window extends ParentLayout implements VRListener {


    private Menu menu;
    private WindowContent content;

    public Window(DesktopRenderer renderer, float width, float height) {
        super(renderer, width, height, LayoutParams.VERTICAL);

        menu = new Menu(this);
        content = new WindowContent(width, height-menu.getHeight());
        addChild(content);
    }

    @Override
    public void onClick() {
    }

    @Override
    public void onStartLooking() {

    }

    @Override
    public void onStopLooking() {

    }

    @Override
    public void onLongLooking() {

    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
