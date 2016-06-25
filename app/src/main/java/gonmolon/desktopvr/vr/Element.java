package gonmolon.desktopvr.vr;

import org.rajawali3d.Object3D;

public abstract class Element extends Object3D {

    protected float width;
    protected float height;

    public Element(float width, float height) {
        super();
        this.width = width;
        this.height = height;
    }

    public Element(Layout parent, float width, float height) {
        this(width, height);
        parent.addChild(this);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
