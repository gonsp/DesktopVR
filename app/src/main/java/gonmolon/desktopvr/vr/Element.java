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

    public Element(float width, float height) {
        super(width, height, 1, 1);

        this.width = width;
        this.height = height;
        clickable = false;

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
    }
}