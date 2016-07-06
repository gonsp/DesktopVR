package gonmolon.desktopvr.vr;

public abstract class VRListenerAdapter implements VRListener {

    @Override
    public boolean onLooking(double x, double y) {
        return true;
    }

    @Override
    public void onStartLooking() {}

    @Override
    public void onStopLooking() {}

    @Override
    public void onLongLooking() {}
}
