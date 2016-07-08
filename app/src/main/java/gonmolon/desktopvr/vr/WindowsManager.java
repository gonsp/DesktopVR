package gonmolon.desktopvr.vr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WindowsManager {

    private HashMap<Integer, Window> windows;
    private DesktopRenderer renderer;

    public WindowsManager(DesktopRenderer renderer) {
        this.renderer = renderer;
        windows = new HashMap<>();
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
        boolean focus = false;
        Iterator i = getIterator();
        while(i.hasNext()) {
            Window window = (Window) i.next();
            if(window.isLookingAt()) {
                focus = true;
            }
        }
        return focus;
    }

    public void setClickAt() {
        Iterator i = getIterator();
        while(i.hasNext()) {
            Window window = (Window) i.next();
            if(window.isLookingAt()) {
                window.setClickAt();
            }
        }
    }

    public DesktopRenderer getRenderer() {
        return renderer;
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
