package gonmolon.desktopvr.vr;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.primitives.Plane;

public abstract class Element extends Plane implements VRListener {

    protected float width;
    protected float height;
    private Material material;

    private boolean clickable;
    private boolean focusable;
    private boolean isLookingAt;
    private long startLooking;

    public Element(float width, float height) {
        super(width, height, 1, 1);

        this.width = width;
        this.height = height;
        clickable = false;
        isLookingAt = false;
        startLooking = -1;

        material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        setMaterial(material);
    }

    public Element(Layout parent, float width, float height) {
        this(width, height);
        parent.addChild(this);
    }

    public void setBackgroundColor(int color) {
        material.setColor(color);
        material.setColorInfluence(1);
        setTransparent(false);
    }

    public void setImage(int res) {
        try {
            material.addTexture(new Texture("image", res));
            material.setColorInfluence(0);
            setTransparent(true);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
        if(clickable) {
            setFocusable(true);
        }
    }

    public boolean isFocusable() {
        return focusable;
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    public boolean setLookingAt(boolean isLookingAt, double x, double y) {
        if(!this.isLookingAt && isLookingAt) {
            onStartLooking();
        } else if(this.isLookingAt && !isLookingAt) {
            onStopLooking();
            startLooking = -1;
        }
        this.isLookingAt = isLookingAt;
        if(isLookingAt) {
            long now = System.currentTimeMillis();
            if(startLooking == -1) {
                startLooking = now;
            }
            if(startLooking != -1 && startLooking + 1000 <= now) {
                onLongLooking();
                startLooking = -1;
            }
            return onLooking(x, y);
        }
        return false;
    }

    public void setClickAt(double x, double y) {
        if(isClickable()) {
            onClick(x, y);
        }
    }

    @Override
    public boolean onLooking(double x, double y) {
        return focusable;
    }
}