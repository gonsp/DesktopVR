package gonmolon.desktopvr.vr;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.primitives.Plane;

public abstract class Element extends Plane implements VRListener, Pointeable {

    protected float width;
    protected float height;
    protected Layout parent;
    protected Material material;

    private boolean clickable;
    private boolean isLookingAt;
    private long startLooking;

    private double lastX;
    private double lastY;

    public Element(float width, float height) {
        super(width, height, 1, 1);

        this.width = width;
        this.height = height;
        lastX = 0;
        lastY = 0;
        parent = null;

        startLooking = -1;
        setClickable(true);

        material = new Material();
        material.enableLighting(false);
        material.setColorInfluence(0);
        setMaterial(material);
    }

    public Element(Layout parent, float width, float height) {
        this(width, height);
        this.parent = parent;
        this.parent.addChild(this);
    }

    public void setBackgroundColor(int color) {
        material.setColor(color);
        material.setColorInfluence(1);
        setTransparent(false);
    }

    public void setImage(int res, boolean transparent) {
        try {
            if(material.getTextureList().size() > 0) {
                for(ATexture aTexture : material.getTextureList()) {
                    material.removeTexture(aTexture);
                }
            }
            material.addTexture(new Texture("image", res));
            material.setColorInfluence(0);
            setTransparent(transparent);
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
    }

    public void setLookingAt(boolean isLookingAt, double x, double y) {
        lastX = x;
        lastY = y;
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
            if(startLooking > 0 && startLooking + 2000 <= now) {
                onLongLooking();
                startLooking = 0;
            }
            onLooking(x, y);
        }
    }

    public void setClickAt() {
        if(isClickable()) {
            onClick(lastX, lastY);
        }
    }
}