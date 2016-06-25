package gonmolon.desktopvr.vr;

import android.graphics.Color;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.primitives.RectangularPrism;

public class Button extends Element implements VRListener {

    private VRListener listener;
    private RectangularPrism test; //TODO delete this

    public Button(int color) {
        super(0.5f, 0.5f);
        test = new RectangularPrism(0.5f, 0.5f, 0);
        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColor(color);
        test.setMaterial(material);
        addChild(test); //TODO delete this
        isContainer(true); //TODO delete this
        test.setPosition(0, 0, 0.01);
    }

    public void setVRListener(VRListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStartLooking() {
        if(listener != null) {
            listener.onStartLooking();
        }
    }

    @Override
    public void onStopLooking() {

    }

    @Override
    public void onLongLooking() {
        if(listener != null) {
            listener.onLongLooking();
        }
    }

    @Override
    public void onClick() {
        if(listener != null) {
            listener.onClick();
        }
    }
}
