package gonmolon.desktopvr.vr;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.google.vr.sdk.base.HeadTransform;

import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.Sphere;

public class DesktopRenderer extends VRRenderer {

    private GazePointer pointer;
    private Window window;
    private Sphere bola;

    public Vector3 position;
    public Vector3 leftEyePos;

    public DesktopRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        leftEyePos = new Vector3(0, 0, 0);
        position = new Vector3(0, 0, 0);

        getCurrentCamera().setFarPlane(1000);

        DirectionalLight light = new DirectionalLight(0f, -1f, -0.5f);
        light.setPower(2f);
        getCurrentScene().addLight(light);

        FloorGenerator.generate(this);
        pointer = new GazePointer(this);
        window = new Window(this, 8, 5f);
        window.setPosition(0, 0, -5);

        bola = new Sphere(1f, 12, 12);
        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        bola.setMaterial(material);
        bola.setColor(Color.YELLOW);
        //getCurrentScene().addChild(bola);
        bola.setPosition(0, 0, -6);

        Plane plane = new Plane(10, 10, 1, 1);
        material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        plane.setMaterial(material);
        plane.setColor(Color.GREEN);
        plane.setPosition(0, 0, -10);
        //getCurrentScene().addChild(plane);
    }

    public Vector3 getCameraDir() {
        /*
        Vector3 cameraDir = new Vector3();
        mHeadViewQuaternion.fromMatrix(mHeadViewMatrix);
        mHeadViewQuaternion.inverse();
        cameraDir.setAll(0, 0, 1);
        cameraDir.transform(mHeadViewQuaternion);
        cameraDir.inverse();
        return cameraDir;*/
        Vector3 cameraDir = new Vector3(pointer.getPosition());
        cameraDir.subtract(position);
        return cameraDir;
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        super.onNewFrame(headTransform);
        position = new Vector3(getCurrentCamera().getPosition());
        position.add(leftEyePos);
        position.divide(2.0f);
        Log.d("HELLOU", position.x + ", " + position.y + ", " + position.z);
        if(pointer != null) {
            pointer.refresh();
            pointer.setClickable(window.isLookingAt());
        }
    }

    @Override
    public void onRender(long elapsedTime, double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
        leftEyePos = new Vector3(getCurrentCamera().getPosition());
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
