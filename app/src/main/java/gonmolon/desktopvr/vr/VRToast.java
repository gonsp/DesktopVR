package gonmolon.desktopvr.vr;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import gonmolon.desktopvr.R;

public class VRToast {

    private Context context;
    private String text;
    private int duration;

    public VRToast(Context context, String text, int duration) {
        this.context = context;
        this.text = text;
        this.duration = duration;
    }

    public void show() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.vr_toast, (ViewGroup) ((Activity) context).findViewById(R.id.toast));

        TextView leftToast = (TextView) layout.findViewById(R.id.left_toast);
        leftToast.setText(text);
        TextView rightToast = (TextView) layout.findViewById(R.id.right_toast);
        rightToast.setText(text);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
