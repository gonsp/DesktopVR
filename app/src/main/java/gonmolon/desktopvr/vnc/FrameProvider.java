package gonmolon.desktopvr.vnc;

import android.content.Context;
import android.graphics.Bitmap;


public class FrameProvider {

    private VNCClient vncClient;
    private String ipAddress;
    private final String tcpPort = "8080";
    private int focus;

    public FrameProvider(Context context, String ipAddress) {
        vncClient = new VNCClient(context, ipAddress);
        this.ipAddress = ipAddress;
        focus = -1;
    }

    public Bitmap getFrame(int PID) {
        if(focus != PID) {
            focus = PID;
            focusWindow(PID);
        }
        return vncClient.getFrame();
    }

    private void focusWindow(int PID) {
        try {
            Utils.GET(ipAddress, tcpPort, "focus/" + PID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
