package gonmolon.desktopvr.vr;

import org.rajawali3d.math.vector.Vector3;

public class Layout extends Element {

    private LayoutParams orientation;
    private LayoutBackground background;
    protected float offset;
    private Element childFocused;

    public Layout(float width, float height, LayoutParams orientation) {
        super(width, height);
        this.orientation = orientation;
        isContainer(true);
        if(orientation == LayoutParams.HORIZONTAL) {
            offset = -width/2;
        } else {
            offset = height/2;
        }
    }

    public Layout(Layout parent, float width, float height, LayoutParams orientation) {
        this(width, height, orientation);
        parent.addChild(this);
    }

    @Override
    public void setBackgroundColor(int color) {
        if(background == null) {
            background = new LayoutBackground(this);
            //super.addChild(background);
        }
        background.setColor(color);
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
    }

    private Element getElementIn(double x, double y) {
        for(int i = 0; i < getNumChildren(); ++i) {
            Element element = (Element) getChildAt(i);
            Vector3 pos = element.getPosition();
            if(x >= pos.x - element.getWidth()/2 && x <= pos.x + element.getWidth()/2 && y >= pos.y - element.getHeight()/2 && y <= pos.y + element.getHeight()/2) {
                return element;
            }
        }
        return null;
    }

    @Override
    public void onClick(double x, double y) {
        if(childFocused != null) {
            childFocused.setClickAt();
        }
    }

    @Override
    public void onLooking(double x, double y) {
        Element element = getElementIn(x, y);
        if(element != null) {
            element.setLookingAt(true, x - element.getPosition().x, y - element.getPosition().y);
        }
        if(childFocused != null && (element == null || element != childFocused)) {
            childFocused.setLookingAt(false, 0, 0);
        }
        childFocused = element;
    }

    @Override
    public void onStopLooking() {
        if(childFocused != null) {
            childFocused.setLookingAt(false, 0, 0);
            childFocused = null;
        }
    }

    @Override
    public GazePointer.PointerStatus getPointerAction() {
        if(childFocused != null) {
            return childFocused.getPointerAction();
        } else {
            return GazePointer.PointerStatus.NORMAL;
        }
    }

    @Override
    public void onStartLooking() {}

    @Override
    public void onLongLooking() {}

    public enum LayoutParams {
        HORIZONTAL, VERTICAL
    }

    protected class LayoutBackground extends Element {

        protected LayoutBackground(Layout layout, int color) {
            this(layout);
            setBackgroundColor(color);
        }

        protected LayoutBackground(Layout layout) {
            super(layout.width, layout.height);
            Layout.super.addChild(this);
            background.setPosition(0, 0, -0.1f);
            setClickable(false);
        }

        @Override
        public void onClick(double x, double y) {}

        @Override
        public void onLooking(double x, double y) {}

        @Override
        public void onStartLooking() {}

        @Override
        public void onStopLooking() {}

        @Override
        public void onLongLooking() {}

        @Override
        public GazePointer.PointerStatus getPointerAction() {
            return GazePointer.PointerStatus.NORMAL;
        }
    }
}
