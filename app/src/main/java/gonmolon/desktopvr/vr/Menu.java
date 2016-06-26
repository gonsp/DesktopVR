package gonmolon.desktopvr.vr;

import android.graphics.Color;

public class Menu extends Layout {

    public static final float HEIGHT = 0.5f;

    private Button closeButton;
    private Button minimizeButton;

    public Menu(final Window window) {
        super(window.getWidth(), HEIGHT, LayoutParams.HORIZONTAL);

        setBackground(Color.BLUE);

        closeButton = new Button(HEIGHT, HEIGHT, Color.GREEN);
        minimizeButton = new Button(HEIGHT, HEIGHT, Color.BLACK);

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

        addChild(closeButton);
        addChild(minimizeButton);

        window.addChild(this);
    }
}
