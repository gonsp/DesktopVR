package gonmolon.desktopvr;

public abstract class Model implements VRListener {

    private boolean clickable = true;

    protected Model() {

    }

    public boolean isLooking() { //Param view direction
        boolean looking = true; //TODO implement this
        if(looking) {
            onLooking();
        }
        return looking;
    }

    public void setClick() {
        if(clickable) {
            onClick();
        }
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}
