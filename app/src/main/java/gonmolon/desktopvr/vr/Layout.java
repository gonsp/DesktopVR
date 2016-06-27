package gonmolon.desktopvr.vr;

import org.rajawali3d.primitives.Plane;

import java.util.ArrayList;


public class Layout extends Element {

    private LayoutParams orientation;
    private ArrayList<Element> children;
    private LayoutBackground background;
    private float offset = 0;

    public Layout(float width, float height, LayoutParams orientation) {
        super(width, height);
        this.orientation = orientation;
        isContainer(true);
        if(orientation == LayoutParams.HORIZONTAL) {
            offset = -width/2;
        } else if(orientation == LayoutParams.VERTICAL) {
            offset = height/2;
        }
        children = new ArrayList<>();
    }

    public Layout(Layout parent, float width, float height, LayoutParams orientation) {
        this(width, height, orientation);
        parent.addChild(this);
    }

    public boolean isLookingAt(double x, double y) {
        return true;
    }

    @Override
    public void setBackgroundColor(int color) {
        if(background == null) {
            background = new LayoutBackground(this, color);
            super.addChild(background);
            //background.setPosition(0, 0, 0);
        }
    }

    public void addChild(Element element) {
        super.addChild(element);
        if(orientation == LayoutParams.HORIZONTAL) {
            element.setPosition(offset + element.getWidth()/2, 0, 0);
            offset += element.getWidth();
        } else if(orientation == LayoutParams.VERTICAL) {
            element.setPosition(0, offset - element.getHeight()/2, 0);
            offset -= element.getHeight();
        }
        children.add(element);
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

    public enum LayoutParams {
        HORIZONTAL, VERTICAL
    }

    protected class LayoutBackground extends Element {

        protected LayoutBackground(Layout layout, int color) {
            super(layout.width, layout.height);
            setBackgroundColor(color);
        }

        @Override
        public void onClick() {}

        @Override
        public void onStartLooking() {}

        @Override
        public void onStopLooking() {}

        @Override
        public void onLongLooking() {}
    }
}
