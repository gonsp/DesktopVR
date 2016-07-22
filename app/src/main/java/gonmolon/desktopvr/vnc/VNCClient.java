package gonmolon.desktopvr.vnc;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.realvnc.vncsdk.DirectTcpConnector;
import com.realvnc.vncsdk.Library;
import com.realvnc.vncsdk.PixelFormat;
import com.realvnc.vncsdk.Viewer;

public class VNCClient implements Viewer.FramebufferCallback, SdkThread.Callback {

    private Viewer viewer;
    private DirectTcpConnector connector;
    private Bitmap frame;

    public VNCClient(Context context) {
        SdkThread.getInstance().init(context.getFilesDir().getAbsolutePath() + "dataStore", this);
        try {
            viewer = new Viewer();
        } catch (Library.VncException e) {
            e.printStackTrace();
        }
        connect("192.168.43.19");
    }

    private void connect(final String ipAddress) {
        final int tcpPort = 5900;

        if(!SdkThread.getInstance().initComplete()) {
            return;
        }

        SdkThread.getInstance().post(new Runnable() {
            @Override
            public void run() {

                SdkThread.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Library.enableAddOn("gc2DACnMx2xpaEiQXrU.gc2PXKcdUZ37RPsNEHL.devEX1Sg2Txs1CgVuW4.gc2AKkgvZPsNiozy1Hu");
                        } catch (Library.VncException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                });

                try {
                    viewer.setFramebufferCallback(VNCClient.this);

                    VNCClient.this.serverFbSizeChanged(viewer, 1024, 768);

                    connector = new DirectTcpConnector();
                    connector.connect(ipAddress, tcpPort, viewer.getConnectionHandler());

                } catch (Library.VncException e) {
                    Log.e("VNC", "Connection error");
                    e.printStackTrace();
                }
            }
        });
    }

    public Bitmap getFrame() {
        return frame;
    }

    @Override
    public void serverFbSizeChanged(Viewer viewer, int w, int h) {
        Log.e("VNC", "serverFbSizeChanged");
        frame = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        frame.setHasAlpha(false);
        /*try {
            viewer.setViewerFb(null, PixelFormat.bgr888(), w, h, 0);
            Log.e("VNC", "Biene");
        } catch (Library.VncException e) {
            Log.e("VNC", "MAL");
            e.printStackTrace();
        }*/
        Log.e("VNC", "serverFbSizeChanged finished");
    }

    @Override
    public void viewerFbUpdated(Viewer viewer, int x, int y, int w, int h) {
        Log.e("VNC", "viewerFbUpdated");
        if(frame != null) {
            try {
                viewer.getViewerFbData(x, y, w, h, frame, x, y);
            } catch (Library.VncException e) {
                e.printStackTrace();
            }
        }
        Log.e("VNC", "viewerFbUpdated finished");
        while(true);
    }

    @Override
    public void displayMessage(int msgId, String error) {
        Log.e("VNC", error);
    }
}
