package gonmolon.desktopvr.vr;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import org.rajawali3d.math.vector.Vector3;

public class DesktopRenderer extends VRRenderer {

    private GazePointer pointer;
    private WindowsManager windowManager;
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
        windowManager = new WindowsManager(this);
        optionsBox = new OptionsBox(this);
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

    int testID = 1;

    public void onCardboardTrigger() {
        if(windowManager != null && windowManager.isLookingAt()) {
            windowManager.setClickAt();
        }
        if(optionsBox != null && optionsBox.isLookingAt()) {
            optionsBox.setClickAt();
        }
        if(windowManager != null) {
            Window test = new Window(this, 8, 5);
            try {
                windowManager.addWindow(test, testID++);
                Log.d("HELLOU", "new window added");
            } catch (WindowsManagerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {}

    @Override
    public void onTouchEvent(MotionEvent event) {}

    public void close() {
        ((Activity)getContext()).finish();
    }
}
