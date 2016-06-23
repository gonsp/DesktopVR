package gonmolon.desktopvr;

public interface VRListener {

    void onClick(); //Param relative position

    void onStartLooking();

    void onStopLooking();

    void onLongLooking();
}
