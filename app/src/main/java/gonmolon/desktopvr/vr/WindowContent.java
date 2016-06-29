package gonmolon.desktopvr.vr;


import android.graphics.Color;
import android.util.Log;

import gonmolon.desktopvr.R;

public class WindowContent extends Element {

    public WindowContent(float width, float height) {
        super(width, height);
        setFocusable(true);
        setBackgroundColor(Color.WHITE);
        setImage(R.drawable.desktop);
    }


    @Override
    public void onClick(double x, double y) {

    }

    @Override
    public boolean onLooking(double x, double y) {
        Log.d("HELLOU", "Estoy apuntado en la pos: (" + x + ", " + y + ")");
        return true;
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
