package gonmolon.desktopvr.vr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import gonmolon.desktopvr.vnc.VNCClient;

public class WindowsManager {

    private HashMap<Integer, Window> windows;
    private DesktopRenderer renderer;
    private int focus;
    VNCClient vncClient;

    public WindowsManager(DesktopRenderer renderer) {
        this.renderer = renderer;
        windows = new HashMap<>();
        focus = -1;
        vncClient = new VNCClient(renderer.getContext());
    }

    public Iterator getIterator() {
        return new WindowsIterator(this);
    }

    public void addWindow(int PID) throws WindowsManagerException {
        if(PID < 0) {
            throw new WindowsManagerException(WindowsManagerException.Error.ID_INVALID);
        } else if(windows.containsKey(PID)) {
            throw new WindowsManagerException(WindowsManagerException.Error.ID_USED);
        } else {
            Window window = new Window(this, 8, 5, PID);
            window.setAngularPosition(90, 0, 5);
            windows.put(PID, window);
        }
    }

    public void removeWindow(int PID) throws WindowsManagerException {
        if(PID < 0) {
            throw new WindowsManagerException(WindowsManagerException.Error.ID_INVALID);
        } else if (windows.containsKey(PID)) {
            renderer.getCurrentScene().removeChild(windows.get(PID));
            windows.remove(PID);
        } else {
            throw new WindowsManagerException(WindowsManagerException.Error.ID_NONEXISTENT);
        }
    }

    public Window getWindow(int PID) throws WindowsManagerException {
        if (PID < 0) {
            throw new WindowsManagerException(WindowsManagerException.Error.ID_INVALID);
        } else if (windows.containsKey(PID)) {
            return windows.get(PID);
        } else {
            throw new WindowsManagerException(WindowsManagerException.Error.ID_NONEXISTENT);
        }
    }

    public boolean isLookingAt() {
        focus = -1;
        Iterator i = getIterator();
        while(i.hasNext()) {
            Window window = (Window) i.next();
            if(window.isLookingAt()) {
                focus = window.getPID();
                window.updateContent(vncClient.getFrame());
                return true;
            }
        }
        return false;
    }

    public void setClickAt() {
        Iterator i = getIterator();
        while(i.hasNext()) {
            Window window = (Window) i.next();
            if(window.isLookingAt()) {
                window.setClickAt();
                return;
            }
        }
    }

    public DesktopRenderer getRenderer() {
        return renderer;
    }

    private void allocateWindow(Window window) {

    }

    public void refresh() {
        double rad = 0;
        final double incr = 360/windows.size();
        Iterator i = getIterator();
        while(i.hasNext()) {
            Window window = (Window) i.next();
            window.setAngularPosition(rad, 0, 10);
            rad += incr;
        }
    }

    public void setWindowFocused(int PID) {
        Iterator i = getIterator();
        while(i.hasNext()) {
            Window window = (Window) i.next();
            if(window.getPID() != PID && window.getDistancePos() < 10) {
                window.setAngularPosition(window.getAngle(), window.getHeightPos(), 10);
            }
        }
        try {
            Window window = getWindow(PID);
            if(window.getHeightPos() != 0 || window.getDistancePos() > 5) {
                window.setAngularPosition(window.getAngle(), 0, 5);
            }
        } catch (WindowsManagerException e) {
            e.printStackTrace();
        }
    }

    public class WindowsIterator implements Iterator {

        private Iterator<Map.Entry<Integer, Window>> iterator;

        private WindowsIterator(WindowsManager manager) {
            iterator = manager.windows.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Window next() {
            return iterator.next().getValue();
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }
}
