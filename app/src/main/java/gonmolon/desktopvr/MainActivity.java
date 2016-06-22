package gonmolon.desktopvr;

import android.os.Bundle;

import com.google.vr.sdk.base.GvrActivity;

public class MainActivity extends GvrActivity {

    private VRView vrView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vrView = (VRView) findViewById(R.id.VR_panel);
        setGvrView(vrView);
    }
}
