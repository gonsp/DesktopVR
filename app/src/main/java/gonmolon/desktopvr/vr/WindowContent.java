package gonmolon.desktopvr.vr;


import android.graphics.Color;
import android.util.Log;

import gonmolon.desktopvr.R;

public class WindowContent extends Element {

    public WindowContent(Window parent, float width, float height) {
        super(parent, width, height);
        setImage(R.drawable.desktop, false);
    }

    @Override
    public void onClick() {
        Log.d("WindowContent", "Click registred!");
    }

    @Override
    public boolean onLooking(double x, double y) {
        return super.onLooking(x, y);
    }

    @Override
    public void onStartLooking() {
        Log.d("WindowContent", "Start looking!");
    }

    @Override
    public void onStopLooking() {
        Log.d("WindowContent", "Stop looking!");
    }

    @Override
    public void onLongLooking() {
        Log.d("WindowContent", "Long looking");
    }
}
