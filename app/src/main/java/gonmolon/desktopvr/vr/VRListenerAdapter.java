package gonmolon.desktopvr.vr;

public abstract class VRListenerAdapter implements VRListener {

    @Override
    public void onLooking(double x, double y) {}

    @Override
    public void onStartLooking() {}

    @Override
    public void onStopLooking() {}

    @Override
    public void onLongLooking() {}
}
