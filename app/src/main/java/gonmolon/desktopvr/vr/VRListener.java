package gonmolon.desktopvr.vr;

public interface VRListener {

    void onClick(); //Param relative position

    void onStartLooking();

    void onStopLooking();

    void onLongLooking();
}
