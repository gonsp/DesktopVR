package gonmolon.desktopvr.vr;

import gonmolon.desktopvr.R;

public class Menu extends Layout {

    public static final float HEIGHT = 0.3f;

    private Button closeButton;
    private Button minimizeButton;

    public Menu(final Window window) {
        super(window, window.getWidth(), HEIGHT, LayoutParams.HORIZONTAL);

        closeButton = new Button(this, HEIGHT, HEIGHT);
        minimizeButton = new Button(this, HEIGHT, HEIGHT);

        closeButton.setImage(R.drawable.close);
        minimizeButton.setImage(R.drawable.minimize);

        closeButton.setVRListener(new VRListenerAdapter() {
            @Override
            public void onClick() {
                window.close();
            }
        });

        //setBackgroundColor(Color.BLUE);
    }
}
