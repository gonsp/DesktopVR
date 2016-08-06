package gonmolon.desktopvr.vr;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Surface;

import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.StreamingTexture;

public final class WindowContent extends Element implements StreamingTexture.ISurfaceListener {

    public static final float HEIGHT = 5f;

    private StreamingTexture streamingTexture;
    private Surface surface;
    private int pixelsWidth;
    private int pixelsHeight;
    private volatile boolean shouldUpdate = false;

    public WindowContent(Window parent, int pixelsWidth, int pixelsHeight) {
        super(parent, (WindowContent.HEIGHT/pixelsHeight)*pixelsWidth, WindowContent.HEIGHT);

        this.pixelsWidth = pixelsWidth;
        this.pixelsHeight = pixelsHeight;

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
    public boolean onLooking(double x, double y) {
        if(shouldUpdate) {
            streamingTexture.update();
            shouldUpdate = false;
        }
        return super.onLooking(x, y);
    }

    @Override
    public void onClick() {
        ((Window) parent).focus();
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
}
