package gonmolon.desktopvr;

import android.os.Bundle;

import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

import gonmolon.desktopvr.vr.DesktopRenderer;

public class MainActivity extends GvrActivity {

    private GvrView vrView;
    private DesktopRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vrView = new GvrView(this);
        renderer = new DesktopRenderer(this);

        setContentView(vrView);
        setGvrView(vrView);
        vrView.setRenderer(renderer);
    }

    @Override
    public void onCardboardTrigger() {
        renderer.onCardboardTrigger();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        renderer.close();
        super.onDestroy();
    }
}