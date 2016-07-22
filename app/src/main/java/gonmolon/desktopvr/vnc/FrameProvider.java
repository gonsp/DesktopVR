package gonmolon.desktopvr.vnc;

import android.content.Context;
import android.graphics.Bitmap;

public class FrameProvider {

    private VNCClient vncClient;
    private int focus;

    public FrameProvider(Context context) {
        vncClient = new VNCClient(context);
        focus = -1;
    }

    public Bitmap getFrame(int PID) {
        if(focus != PID) {
            focus = PID;

        }
        return vncClient.getFrame();
    }
}
