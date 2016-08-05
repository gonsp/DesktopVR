package gonmolon.desktopvr.vr;

import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import gonmolon.desktopvr.vnc.Utils;
import gonmolon.desktopvr.vnc.VNCClient;

public class WindowManager {

    private HashMap<Integer, Window> windows;
    private DesktopRenderer renderer;
    private VNCClient vncClient;
    private String ipAddress;
    private final String tcpPort = "8080";
    private WindowListProvider windowListProvider;

    public WindowManager(DesktopRenderer renderer, String ipAddress) {
        this.renderer = renderer;
        this.ipAddress = ipAddress;
        windows = new HashMap<>();
        vncClient = new VNCClient(renderer.getContext(), ipAddress);
        windowListProvider = new WindowListProvider();
    }

    public Iterator getIterator() {
        return new WindowsIterator(this);
    }

    public void addWindow(int PID) throws WindowManagerException {
        if(PID < 0) {
            throw new WindowManagerException(WindowManagerException.Error.ID_INVALID);
        } else if(windows.containsKey(PID)) {
            throw new WindowManagerException(WindowManagerException.Error.ID_USED);
        } else {
            Window window = new Window(this, 8, 5, PID);
            window.setAngularPosition(90, 0, 5);
            windows.put(PID, window);
        }
    }

    public void removeWindow(int PID) throws WindowManagerException {
        if(PID < 0) {
            throw new WindowManagerException(WindowManagerException.Error.ID_INVALID);
        } else if (windows.containsKey(PID)) {
            renderer.getCurrentScene().removeChild(windows.get(PID));
            windows.remove(PID);
        } else {
            throw new WindowManagerException(WindowManagerException.Error.ID_NONEXISTENT);
        }
    }

    public Window getWindow(int PID) throws WindowManagerException {
        if (PID < 0) {
            throw new WindowManagerException(WindowManagerException.Error.ID_INVALID);
        } else if (windows.containsKey(PID)) {
            return windows.get(PID);
        } else {
            throw new WindowManagerException(WindowManagerException.Error.ID_NONEXISTENT);
        }
    }

    public boolean isLookingAt() {
        Iterator i = getIterator();
        while(i.hasNext()) {
            Window window = (Window) i.next();
            if(window.isLookingAt()) {
                vncClient.focusWindow(window);
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
        } catch (WindowManagerException e) {
            e.printStackTrace();
        }
    }

    public class WindowsIterator implements Iterator {

        private Iterator<Map.Entry<Integer, Window>> iterator;

        private WindowsIterator(WindowManager manager) {
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

    public class WindowListProvider extends AsyncTask<Void, Void, Void> {

        private boolean connected;

        public WindowListProvider() {
            execute((Void[]) null);
        }

        @Override
        protected void onPreExecute() {
            try {
                Utils.GET(ipAddress, tcpPort, "connect");
                Log.d("PID", "connected");
                connected = true;
            } catch (Exception e) {
                e.printStackTrace();
                connected = false;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            while(connected) {
                try {
                    String output = Utils.GET(ipAddress, tcpPort, "getWindowList");
                    if(output.length() > 0) {
                        for(String s : output.split("#")) {
                            String PID = s.split(",")[0];
                            Log.d("PID", PID);
                            int pid = Integer.valueOf(PID);
                            try {
                                WindowManager.this.addWindow(pid);
                            } catch (WindowManagerException e) {}
                        }
                        refresh();
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
