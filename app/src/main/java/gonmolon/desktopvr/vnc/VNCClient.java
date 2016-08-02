package gonmolon.desktopvr.vnc;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.realvnc.vncsdk.DirectTcpConnector;
import com.realvnc.vncsdk.ImmutableDataBuffer;
import com.realvnc.vncsdk.Library;
import com.realvnc.vncsdk.PixelFormat;
import com.realvnc.vncsdk.Viewer;

import java.util.EnumSet;
import java.util.Iterator;

public class VNCClient implements Viewer.FramebufferCallback, SdkThread.Callback, Viewer.AuthenticationCallback, Viewer.PeerVerificationCallback {

    private Viewer viewer;
    private DirectTcpConnector connector;
    private Bitmap frame;
    private volatile boolean updated;

    public VNCClient(Context context, String ipAddress) {
        updated = false;
        SdkThread.getInstance().init(context.getFilesDir().getAbsolutePath() + "dataStore", this);
        try {
            viewer = new Viewer();
        } catch (Library.VncException e) {
            e.printStackTrace();
        }
        connect(ipAddress);
    }

    private void connect(final String ipAddress) {
        final int tcpPort = 5900;

        if (!SdkThread.getInstance().initComplete()) {
            return;
        }

        SdkThread.getInstance().post(new Runnable() {
            @Override
            public void run() {
                try {
                    Library.enableAddOn("TC2MxFwH1OATXGPHECgBRtEB/eGe+KE+qNS9zgOqtukmBLX9+rQRndAc/qfOeAO8a8Od1NPnPE++voZtrcDO2dKa1hRrPcS+SkPLdi+OhUIuZCH6Zo5XhReh/mqoY6SiAwTPlpC+CMVEJ3Bc6wNUhhRTg6VYL9qpbsjDoNxXLXlNWRtoxnFOyLblEAPKs8E7lJhxOs5UyshjJCWaSEmC9xYmkoSICxx5biPdXXfytblILf+BS9Ml0sEktTu2CS47UCcSPoIHSe2yPIPOcoCiC5m6fHitOwjs0ZWXaXIs3CVkMhtmDbyDJwYxq+1RL8GNHZdPMFisImuYX18hpiijq9yOGN+isEbSuXKdDK5C31ujS3LdyimtVeMrY01i4Wzg3YPN4UAUS5a9ycpyJ680wiYAdR57YJrYds6vSiH+dUCegeUZaAlNKAcM2/frxVtIx353L9oxh3tVavWTlr326f9SUjsRfNeOrW+U8RPdHi/X2H771EplUbBm2Q7ixVi+JaYASmYOrRgT7Q9Ju8Vqe+L23vlL2glBpIXCTqd1WFCIZCLicnzGEZ9CcViIcnbiZQMp2pdt9aQ2al9g7rgm6ZfUTsdm6JhXeSW4Lw8QuI5kqg/eQYYaf5NztuVhqO9v9FfBTGE064t0uyuh6Ok11/NHa+IEFrs9rlvudg/g1GQ=");

                    viewer.setConnectionCallback(new Viewer.ConnectionCallback() {
                        @Override
                        public void connecting(Viewer viewer) {
                            Log.d("VNC", "CONNECTING");
                        }

                        @Override
                        public void connected(Viewer viewer) {
                            Log.d("VNC", "CONNECTED!!!!");
                        }

                        @Override
                        public void disconnected(Viewer viewer, String msg, EnumSet<Viewer.DisconnectFlags> flags) {
                            Log.d("VNC", "DISCONNECTED: " + msg);
                            Iterator iterator = flags.iterator();
                            while(iterator.hasNext()) {
                                Log.d("VNC", "Flag: " + ((Viewer.DisconnectFlags)iterator.next()).name());
                            }
                        }
                    });
                    viewer.setAuthenticationCallback(VNCClient.this);
                    viewer.setFramebufferCallback(VNCClient.this);
                    viewer.setPeerVerificationCallback(VNCClient.this);

                    VNCClient.this.serverFbSizeChanged(viewer, 1024, 768);

                    connector = new DirectTcpConnector();
                    connector.connect(ipAddress, tcpPort, viewer.getConnectionHandler());
                } catch (Library.VncException e) {
                    Log.e("VNC", "Exception in connection");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void serverFbSizeChanged(Viewer viewer, int w, int h) {
        frame = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        frame.setHasAlpha(false);
        try {
            viewer.setViewerFb(null, PixelFormat.bgr888(), w, h, 0);
        } catch (Library.VncException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void viewerFbUpdated(Viewer viewer, int x, int y, int w, int h) {
        if(frame != null) {
            try {
                viewer.getViewerFbData(x, y, w, h, frame, x, y);
            } catch (Library.VncException e) {
                e.printStackTrace();
            }
        }
        updated = true;
    }

    public Bitmap getFrame() {
        if(updated) {
            updated = false;
            return frame;
            //return frame.copy(frame.getConfig(), false);
        } else {
            return null;
        }
    }

    @Override
    public void displayMessage(int msgId, String error) {
        Log.e("VNC", error);
    }

    @Override
    public void requestUserCredentials(final Viewer viewer, boolean needUser, boolean needPassword) {
        SdkThread.getInstance().post(new Runnable() {
            @Override
            public void run() {
                try {
                    viewer.sendAuthenticationResponse(true, "", "");
                } catch (Library.VncException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void cancelUserCredentialsRequest(Viewer viewer) {}

    @Override
    public void verifyPeer(final Viewer viewer, final String hexFingerprint, final String catchphraseFingerprint, ImmutableDataBuffer serverRsaPublic) {
        SdkThread.getInstance().post(new Runnable() {
            @Override
            public void run() {
                try {
                    viewer.sendPeerVerificationResponse(true);
                } catch (Library.VncException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void cancelPeerVerification(Viewer viewer) {}
}
