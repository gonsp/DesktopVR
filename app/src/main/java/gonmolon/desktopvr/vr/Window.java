package gonmolon.desktopvr.vr;

public class Window extends ParentLayout {


    private Menu menu;
    private WindowContent content;

    public Window(DesktopRenderer renderer, float width, float height) {
        super(renderer, width, height, LayoutParams.VERTICAL);

        menu = new Menu(this);
        content = new WindowContent(width, height-menu.getHeight());
        addChild(content);
    }
}
