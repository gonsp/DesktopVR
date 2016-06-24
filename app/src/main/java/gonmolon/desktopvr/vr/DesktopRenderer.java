package gonmolon.desktopvr.vr;


import android.content.Context;
import android.view.MotionEvent;

public class DesktopRenderer extends VRRenderer {

    public DesktopRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {

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
