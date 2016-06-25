package gonmolon.desktopvr.vr;


import android.content.Context;
import android.graphics.Color;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.math.Matrix4;
import org.rajawali3d.primitives.Sphere;

public class DesktopRenderer extends VRRenderer {

    private GazePointer pointer;
    private Window window;
    private Sphere bola;

    public DesktopRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        getCurrentCamera().setFarPlane(1000);

        DirectionalLight light = new DirectionalLight(0f, -1f, -0.5f);
        light.setPower(2f);
        getCurrentScene().addLight(light);

        FloorGenerator.generate(this);
        pointer = new GazePointer(this);
        window = new Window(this, 8, 5f);
        window.setPosition(0, 0, -10);
        window.setLookAt(getCurrentCamera().getPosition());

        bola = new Sphere(0.1f, 12, 12);
        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        bola.setMaterial(material);
        bola.setColor(Color.YELLOW);
        bola.setPosition(0, window.getY() + window.menu.getY(), -10);
        getCurrentScene().addChild(bola);

        Log.d("HELLOU", String.valueOf(window.menu.closeButton.getWorldPosition().x));
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
