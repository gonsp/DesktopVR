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

    public void addWindow(Window window, int ID) throws WindowsManagerException {
        if(ID < 0) {
            throw new WindowsManagerException(WindowsManagerException.Error.ID_INVALID);
        } else if(windows.containsKey(ID)) {
            throw new WindowsManagerException(WindowsManagerException.Error.ID_USED);
        } else {
            windows.put(ID, window);
            window.setAngularPosition(90, 0, 5);
        }
    }

    public void removeWindow(int ID) throws WindowsManagerException {
        if(ID < 0) {
            throw new WindowsManagerException(WindowsManagerException.Error.ID_INVALID);
        } else if (windows.containsKey(ID)) {
            windows.remove(ID);
        } else {
            throw new WindowsManagerException(WindowsManagerException.Error.ID_NONEXISTENT);
        }
    }

    public Window getWindow(int ID) throws WindowsManagerException {
        if (ID < 0) {
            throw new WindowsManagerException(WindowsManagerException.Error.ID_INVALID);
        } else if (windows.containsKey(ID)) {
            return windows.get(ID);
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
