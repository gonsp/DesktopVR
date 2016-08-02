package gonmolon.desktopvr.vr;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Surface;

import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.StreamingTexture;

public class WindowContent extends Element implements StreamingTexture.ISurfaceListener {

    private StreamingTexture streamingTexture;
    private Surface surface;

    public WindowContent(Window parent, float width, float height) {
        super(parent, width, height);

        material.setColorInfluence(0);
        setTransparent(true);

        streamingTexture = new StreamingTexture("StreamingTexture", this);
        try {
            material.addTexture(streamingTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
    }

    public void updateFrame(Bitmap frame) {
        if(surface != null && frame != null) {
            final Canvas canvas = surface.lockCanvas(null);
            canvas.setBitmap(frame);
            surface.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    @Override
    public void onClick() {
        Log.d("WindowContent", "Click registered!");
    }

    @Override
    public boolean onLooking(double x, double y) {
        return super.onLooking(x, y);
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
