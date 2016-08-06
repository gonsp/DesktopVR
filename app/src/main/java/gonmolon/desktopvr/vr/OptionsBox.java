package gonmolon.desktopvr.vr;

import org.rajawali3d.math.vector.Vector3;

import gonmolon.desktopvr.R;

public class OptionsBox extends ParentLayout {

    private static final float HEIGHT = 0.4f;
    private static final float MARGIN = 0.1f;

    private Button closeButton;
    private Button settingsButton;

    public OptionsBox(DesktopRenderer desktopRenderer) {
        super(desktopRenderer, HEIGHT*2 + MARGIN, HEIGHT, LayoutParams.HORIZONTAL);

        closeButton = new Button(this, HEIGHT, HEIGHT);
        closeButton.setFocusZoom(0.5f);
        offset += MARGIN;
        settingsButton = new Button(this, HEIGHT, HEIGHT);
        settingsButton.setFocusZoom(0.5f);

        closeButton.setImage(R.drawable.close, true);
        settingsButton.setImage(R.drawable.settings, true);

        closeButton.setVRListener(new VRListenerAdapter() {
            @Override
            public void onClick(double x, double y) {
                renderer.close();
            }
        });

        setAngularPosition(90, -3, 3);
    }

    public void refresh() {
        Vector3 cameraDir = renderer.getCameraDir();
        if(cameraDir.y < -1) {
            setVisible(true);
        } else {
            setVisible(false);
        }
    }
}
