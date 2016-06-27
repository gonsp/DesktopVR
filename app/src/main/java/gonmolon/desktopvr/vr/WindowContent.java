package gonmolon.desktopvr.vr;


import android.graphics.Color;

import gonmolon.desktopvr.R;

public class WindowContent extends Element {

    public WindowContent(float width, float height) {
        super(width, height);
        setBackgroundColor(Color.WHITE);
        setImage(R.mipmap.ic_launcher);
    }

    @Override
    public void onClick() {

    }

    @Override
    public void onStartLooking() {

    }

    @Override
    public void onStopLooking() {

    }

    @Override
    public void onLongLooking() {

    }
}
