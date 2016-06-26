package gonmolon.desktopvr.vr;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.primitives.Plane;

public class Button extends Element implements VRListener {

    private VRListener listener;
    private Plane test; //TODO delete this

    public Button(float width, float height, int color) {
        super(width, width);
        test = new Plane(width, height, 1, 1);
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
