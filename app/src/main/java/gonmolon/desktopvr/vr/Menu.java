package gonmolon.desktopvr.vr;

import android.graphics.Color;

import gonmolon.desktopvr.R;

public class Menu extends Layout {

    public static final float HEIGHT = 0.3f;

    private Button closeButton;
    private Button minimizeButton;

    public Menu(final Window window) {
        super(window, window.getWidth(), HEIGHT, LayoutParams.HORIZONTAL);

        closeButton = new Button(this, HEIGHT, HEIGHT);
        closeButton.setImage(R.mipmap.ic_launcher);
        minimizeButton = new Button(this, HEIGHT, HEIGHT);

        closeButton.setVRListener(new VRListener() {
            @Override
            public void onClick() {
                window.close();
            }

            @Override
            public void onStartLooking() {}

            @Override
            public void onStopLooking() {}

            @Override
            public void onLongLooking() {}
        });

        setBackgroundColor(Color.BLUE);
    }
}
