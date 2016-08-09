package gonmolon.desktopvr.vr;

import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import gonmolon.desktopvr.vnc.Utils;
import gonmolon.desktopvr.vnc.VNCClient;

public class WindowManager implements Pointeable {

    private HashMap<Integer, Window> windows;
    private DesktopRenderer renderer;
    private VNCClient vncClient;
    private WindowListProvider windowListProvider;
    private volatile Window pointed;
    private volatile Window focused;

    public WindowManager(DesktopRenderer renderer, String ipAddress) {
        Utils.ipAddress = ipAddress;
        this.renderer = renderer;
        pointed = null;
        focused = null;
        windows = new HashMap<>();
        vncClient = new VNCClient(renderer.getContext(), ipAddress);
        windowListProvider = new WindowListProvider();
    }

    public Iterator getIterator() {
        return new WindowsIterator(this);
    }

    public Window addWindow(int PID, int width, int height) throws WindowManagerException {
        if(PID < 0) {
            throw new WindowManagerException(WindowManagerException.Error.ID_INVALID);
        } else if(windows.containsKey(PID)) {
            throw new WindowManagerException(WindowManagerException.Error.ID_USED);
        } else {
            Window window = new Window(this, width, height, PID);
            windows.put(PID, window);
            return window;
        }
    }

    public void removeWindow(int PID) throws WindowManagerException {
        if(PID < 0) {
            throw new WindowManagerException(WindowManagerException.Error.ID_INVALID);
        } else if (windows.containsKey(PID)) {
            Window window = windows.get(PID);
            if(vncClient.getFocused() == window) {
                vncClient.focusWindow(null);
            }
            if(window == focused) {
                focused = null;
            }
            if(window == pointed) {
                pointed = null;
            }
            renderer.getCurrentScene().removeChild(window);
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

    public boolean refresh() {
        boolean pointing = false;
        Iterator i = getIterator();
        while(i.hasNext()) {
            Window window = (Window) i.next();
            if(window.isLookingAt()) {
                if(!pointing) {
                    pointed = window;
                    pointing = true;
                    vncClient.focusWindow(window);
                }
            }
        }
        if(!pointing) {
            pointed = null;
        }
        vncClient.refresh();
        return pointing;
    }

    @Override
    public GazePointer.PointerStatus getPointerAction() {
        if(pointed != null) {
            return pointed.getPointerAction();
        } else {
            return GazePointer.PointerStatus.NORMAL;
        }
    }

    public void setClickAt() {
        if(pointed != null) {
            pointed.setClickAt();
        }
    }

    public Window getFocused() {
        return focused;
    }

    public DesktopRenderer getRenderer() {
        return renderer;
    }

    private void allocateWindow(Window window) {

    }

    public void reallocateWindows() {
        double rad = 0;
        final double incr = 360/windows.size();
        Iterator i = getIterator();
        while(i.hasNext()) {
            Window window = (Window) i.next();
            window.setAngularPosition(rad, 0, 10);
            rad += incr;
        }
    }

    public void focusWindow(int PID) {
        if(focused == null || focused.getPID() != PID) {
            Iterator i = getIterator();
            while(i.hasNext()) {
                Window window = (Window) i.next();
                if(window.getPID() != PID && window.getDistancePos() < 10) {
                    window.setAngularPosition(window.getAngle(), window.getHeightPos(), 10);
                }
            }
            try {
                Window window = getWindow(PID);
                if(window.getHeightPos() != 0 || window.getDistancePos() > 4) {
                    window.setAngularPosition(window.getAngle(), 0, 4);
                }
                focused = window;
            } catch (WindowManagerException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        windowListProvider.disconnect();
    }

    public VNCClient getVNCClient() {
        return vncClient;
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

        private volatile boolean connected;

        public WindowListProvider() {
            execute((Void[]) null);
        }

        @Override
        protected void onPreExecute() {
            try {
                Utils.GET("connect");
                connected = true;
            } catch (Exception e) {
                e.printStackTrace();
                connected = false;
            }
        }

        @Override
        protected Void doInBackground(Void[] unused) {
            while(connected) {
                try {
                    String output = Utils.GET("getWindowList");
                    HashSet<Integer> activeWindows = new HashSet<>();
                    if(output.length() > 0) {
                        for(String s : output.split("#")) {
                            String[] params = s.split(",");
                            int pid = Integer.valueOf(params[0]);
                            int width = Integer.valueOf(params[1]);
                            int height = Integer.valueOf(params[2]);
                            try {
                                WindowManager.this.addWindow(pid, width, height);
                            } catch (WindowManagerException e) {
                                Window window = WindowManager.this.getWindow(pid);
                                if(window.getPixelsWidth() != width || window.getPixelsHeight() != height) {
                                    window.close();
                                    WindowManager.this.addWindow(pid, width, height);
                                }
                            }
                            activeWindows.add(pid);
                        }
                        ArrayList<Window> deletedWindows = new ArrayList<>();
                        Iterator i = getIterator();
                        while(i.hasNext()) {
                            Window window = (Window) i.next();
                            if(!activeWindows.contains(window.getPID())) {
                                deletedWindows.add(window);
                            }
                        }
                        for(Window window : deletedWindows) {
                            window.close();
                        }
                        reallocateWindows();
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public void disconnect() {
            connected = false;
        }
    }
}
