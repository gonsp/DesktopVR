package gonmolon.desktopvr.vr;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Surface;

import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.StreamingTexture;

public final class WindowContent extends Element implements StreamingTexture.ISurfaceListener {

    private StreamingTexture streamingTexture;
    private Surface surface;
    private volatile boolean shouldUpdate = false;

    public WindowContent(Window parent, float width, float height) {
        super(parent, width, height);

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
        streamingTexture.getSurfaceTexture().setDefaultBufferSize(1920, 1080);
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
