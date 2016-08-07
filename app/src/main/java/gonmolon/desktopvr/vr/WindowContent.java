package gonmolon.desktopvr.vr;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Surface;

import com.realvnc.vncsdk.Viewer;

import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.StreamingTexture;

import gonmolon.desktopvr.vnc.VNCClient;

public final class WindowContent extends Element implements StreamingTexture.ISurfaceListener {

    public static final float HEIGHT = 5f;

    private VNCClient vncClient;
    private StreamingTexture streamingTexture;
    private Surface surface;
    private int pixelsWidth;
    private int pixelsHeight;
    private volatile boolean shouldUpdate = false;

    public WindowContent(Window parent, int pixelsWidth, int pixelsHeight, VNCClient vncClient) {
        super(parent, (WindowContent.HEIGHT/pixelsHeight)*pixelsWidth, WindowContent.HEIGHT);

        this.pixelsWidth = pixelsWidth;
        this.pixelsHeight = pixelsHeight;
        this.vncClient = vncClient;

        streamingTexture = new StreamingTexture("StreamingTexture", this);
        try {
            getMaterial().addTexture(streamingTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
    }

    public void updateFrame(Bitmap frame) {
        if(surface != null && frame != null) {
            final Canvas canvas = surface.lockCanvas(null);
            canvas.drawBitmap(frame, 0, 0, null);
            surface.unlockCanvasAndPost(canvas);
            shouldUpdate = true;
        }
    }

    @Override
    public void setSurface(Surface surface) {
        this.surface = surface;
        streamingTexture.getSurfaceTexture().setDefaultBufferSize(pixelsWidth, pixelsHeight);
    }

    @Override
    public void onLooking(double x, double y) {
        sendPointerEvent(x, y, null);

        if(shouldUpdate) {
            streamingTexture.update();
            shouldUpdate = false;
        }
    }

    @Override
    public void onClick(double x, double y) {
        if(((Window) parent).isFocused()) {
            sendPointerEvent(x, y, Viewer.MouseButton.MOUSE_BUTTON_LEFT);
        }
        ((Window) parent).focus();
    }

    @Override
    public GazePointer.PointerStatus getPointerAction() {
        return GazePointer.PointerStatus.INVISIBLE;
    }

    @Override
    public void onStartLooking() {
        Log.d("WindowContent", "Start looking!");
    }

    @Override
    public void onStopLooking() {
        Log.d("WindowContent", "Stop looking!");
    }

    @Override
    public void onLongLooking() {
        Log.d("WindowContent", "Long looking");
    }

    private void sendPointerEvent(double x, double y, Viewer.MouseButton button) {
        x += getWidth()/2;
        y = -y;
        y += getHeight()/2;
        x = (pixelsWidth/getWidth())*x;
        y = (pixelsHeight/getHeight())*y;
        vncClient.sendPointerEvent((int)x, (int)y, button);
    }
}
