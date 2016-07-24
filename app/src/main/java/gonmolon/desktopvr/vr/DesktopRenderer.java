package gonmolon.desktopvr.vr;


import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import org.rajawali3d.math.vector.Vector3;

public class DesktopRenderer extends VRRenderer {

    private GazePointer pointer;
    private WindowManager windowManager;
    private OptionsBox optionsBox;

    public Vector3 position;
    public Vector3 leftEyePos;
    public Vector3 rightEyePos;

    public DesktopRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        position = new Vector3(0, 0, 0);
        leftEyePos = new Vector3(position);
        rightEyePos = new Vector3(position);

        getCurrentCamera().setFarPlane(1000);

        FloorGenerator.generate(this, true);
        pointer = new GazePointer(this);
        optionsBox = new OptionsBox(this);
        windowManager = new WindowManager(this, "192.168.10.101");
    }

    public Vector3 getCameraDir() {
        Vector3 cameraDir = new Vector3(pointer.getPosition());
        cameraDir.subtract(position);
        return cameraDir;
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        super.onNewFrame(headTransform);
        position = new Vector3(rightEyePos);
        position.add(leftEyePos);
        position.divide(2.0f);

        if(pointer != null) {
            pointer.refresh();
            pointer.setClickable(windowManager.isLookingAt() || optionsBox.isLookingAt());
        }
    }

    @Override
    public void onDrawEye(Eye eye) {
        super.onDrawEye(eye);
        if(eye.getType() == 1) {
            leftEyePos = getCurrentCamera().getPosition();
        } else {
            rightEyePos = getCurrentCamera().getPosition();
        }
    }

    @Override
    public void onRender(long elapsedTime, double deltaTime) {
        //getCurrentCamera().setPosition(position);
        super.onRender(elapsedTime, deltaTime);
    }

    public void onCardboardTrigger() {
        if(windowManager != null && windowManager.isLookingAt()) {
            windowManager.setClickAt();
        }
        if(optionsBox != null && optionsBox.isLookingAt()) {
            optionsBox.setClickAt();
        }
    }

    public void close() {
        ((Activity)getContext()).finish();
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {}

    @Override
    public void onTouchEvent(MotionEvent event) {}
}
