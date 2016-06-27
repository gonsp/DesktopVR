package gonmolon.desktopvr.vr;

import android.graphics.Color;

public class Window extends ParentLayout implements VRListener {


    private Menu menu;
    private WindowContent content; //TODO own class IMPORTANT

    public Window(DesktopRenderer renderer, float width, float height) {
        super(renderer, width, height, LayoutParams.VERTICAL);

        menu = new Menu(this);

        Layout test = new Layout(this, width, height-menu.getHeight(), LayoutParams.VERTICAL);
        test.setBackground(Color.WHITE);
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
