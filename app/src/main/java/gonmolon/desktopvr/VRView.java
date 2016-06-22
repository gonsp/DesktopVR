package gonmolon.desktopvr;

import android.content.Context;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

public class VRView extends GvrView implements GvrView.Renderer {

    public VRView(Context context) {
        super(context);
        setRenderer(this);
    }

    public void addWindow() {

    }

    @Override
    public void onDrawFrame(HeadTransform headTransform, Eye eye, Eye eye1) {

    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {

    }

    @Override
    public void onRendererShutdown() {

    }
}
