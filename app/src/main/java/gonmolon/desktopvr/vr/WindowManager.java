package gonmolon.desktopvr.vr;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import gonmolon.desktopvr.vnc.Endpoint;
import gonmolon.desktopvr.vnc.HttpClient;
import gonmolon.desktopvr.vnc.HttpServer;
import gonmolon.desktopvr.vnc.VNCClient;

public class WindowManager implements Pointeable {

    private HashMap<Integer, Window> windows;
    private DesktopRenderer renderer;
    private HttpServer server;
    private VNCClient vncClient;
    private WindowListProvider windowListProvider;
    private volatile Window pointed;
    private volatile Window focused;

    public WindowManager(final DesktopRenderer renderer, String ipAddress) {
        HttpClient.ipAddress = ipAddress;
        this.renderer = renderer;
        pointed = null;
        focused = null;
        windows = new HashMap<>();
        server = new HttpServer(8080, renderer.getContext());
        setUpServer();
        vncClient = new VNCClient(renderer.getContext(), ipAddress);
        windowListProvider = new WindowListProvider(server);
        HttpClient.nonBlockingRequest("connect", null);
    }

    private void setUpServer() {
        server.addEndpoint(new Endpoint("tap") {
            @Override
            public void execute(String body) {
                setClickAt();
            }
        });
        server.addEndpoint(new Endpoint("swipe") {
            @Override
            public void execute(String body) {
                Log.d("LEAP MOTION", "swipe");
            }
        });
        server.addEndpoint(new Endpoint("move") {
            @Override
            public void execute(String body) {
                if(pointed != null) {
                    float movement = Float.valueOf(body);
                    movement = movement/10;
                    double newDistance = pointed.getDistancePos()+movement;
                    if(newDistance >= 2 && newDistance < 50) {
                        pointed.setAngularPosition(pointed.getAngle(), pointed.getHeightPos(), newDistance);
                    }
                }
            }
        });
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
        HttpClient.nonBlockingRequest("disconnect", new HttpClient.ResultCallback() {
            @Override
            public void onResult(String result) {
                final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
                executor.schedule(new Runnable() {
                    @Override
                    public void run() {
                        server.stop();
                    }
                }, 1, TimeUnit.SECONDS);
            }

            @Override
            public void onException(Exception e) {
                server.stop();
            }
        });
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

    public class WindowListProvider {

        private static final int PORT = 8080;

        public WindowListProvider(HttpServer server) {
            server.addEndpoint(new Endpoint("updateWindowList") {
                @Override
                public void execute(String body) {
                    if(body != null) {
                        updateWindowList(body);
                    }
                }
            });
        }

        private void updateWindowList(String windowList) {
            HashSet<Integer> activeWindows = new HashSet<>();
            if(windowList.length() > 0) {
                for(String s : windowList.split("#")) {
                    String[] windowParams = s.split(",");
                    int pid = Integer.valueOf(windowParams[0]);
                    int width = Integer.valueOf(windowParams[1]);
                    int height = Integer.valueOf(windowParams[2]);
                    try {
                        addWindow(pid, width, height);
                    } catch (WindowManagerException e) {
                        try {
                            Window window = getWindow(pid);
                            if(window.getPixelsWidth() != width || window.getPixelsHeight() != height) {
                                window.close();
                                addWindow(pid, width, height);
                            }
                        } catch (WindowManagerException exception) {
                            exception.printStackTrace();
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
        }
    }
}
