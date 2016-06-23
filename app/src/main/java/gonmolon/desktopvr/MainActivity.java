package gonmolon.desktopvr;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.vr.sdk.base.GvrActivity;

public class MainActivity extends GvrActivity {

    private VRView vrView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout = (LinearLayout) findViewById(R.id.VR_layout);
        vrView = new VRView(this);
        vrView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
        layout.addView(vrView);
        setGvrView(vrView);
    }

    @Override
    public void onCardboardTrigger() {
        vrView.onCardboardTrigger();
    }
}
