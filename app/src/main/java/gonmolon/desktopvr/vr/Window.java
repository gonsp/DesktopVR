package gonmolon.desktopvr.vr;

import android.graphics.Color;

import org.rajawali3d.primitives.RectangularPrism;

public class Window extends Layout implements VRListener {

    DesktopRenderer renderer;

    public Menu menu;
    public RectangularPrism content; //TODO own class IMPORTANT

    public Window(DesktopRenderer renderer, float width, float height) {
        super(width, height, LayoutParams.VERTICAL);
        renderer.getCurrentScene().addChild(this);

        this.renderer = renderer;

        menu = new Menu(this);

        Layout test = new Layout(this, width, height-menu.getHeight(), LayoutParams.VERTICAL);
        test.setBackground(Color.WHITE);

        setLookAt(renderer.mCameraPosition);
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

    public void close() {
        renderer.getCurrentScene().removeChild(this);
    }
}
