package gonmolon.desktopvr;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;

import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

import gonmolon.desktopvr.vr.DesktopRenderer;

public class MainActivity extends GvrActivity {

    private int brightness;
    private GvrView vrView;
    private DesktopRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentResolver contentResolver = getContentResolver();
        try {
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 255);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + this.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

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
        ContentResolver contentResolver = getContentResolver();
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        renderer.close();
        super.onDestroy();
    }
}