package gonmolon.desktopvr.vr;


import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;

import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.primitives.Sphere;

public class DesktopRenderer extends VRRenderer {

    private GazePointer pointer;
    private Sphere bola;

    public DesktopRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        getCurrentCamera().setFarPlane(1000);

        DirectionalLight light = new DirectionalLight(0f, -1f, 0f);
        light.setPower(2f);
        getCurrentScene().addLight(light);

        FloorGenerator.generate(this);
        pointer = new GazePointer(this);

        bola = new Sphere(1, 12, 12);
        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        bola.setMaterial(material);
        bola.setColor(Color.YELLOW);
        bola.setPosition(0, 0, -6);
        getCurrentScene().addChild(bola);
    }

    @Override
    public void onRender(long elapsedTime, double deltaTime) {
        if(pointer != null) {
            pointer.refresh(isLookingAtObject(bola));
        }
        super.onRender(elapsedTime, deltaTime);
    }


    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    public void onCardboardTrigger() {

    }
}
