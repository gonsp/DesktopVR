package gonmolon.desktopvr.vr;


import android.util.Log;

import gonmolon.desktopvr.R;

public class WindowContent extends Element {

    public WindowContent(Window parent, float width, float height) {
        super(parent, width, height);
        setFocusable(true);
        setImage(R.drawable.desktop);
    }


    @Override
    public void onClick(double x, double y) {
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
