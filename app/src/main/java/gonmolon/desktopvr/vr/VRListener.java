package gonmolon.desktopvr.vr;

public interface VRListener {

    void onClick(double x, double y);

    void onLooking(double x, double y);

    void onStartLooking();

    void onStopLooking();

    void onLongLooking();
}