package gonmolon.desktopvr.vnc;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class HttpServer extends NanoHTTPD {
    
    private HashMap<String, Endpoint> endpoints;

    public HttpServer(int port, final Context context) {
        super(port);
        endpoints = new HashMap<>();
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEndpoint(new Endpoint("log") {
            @Override
            public void execute(final String body) {
                Log.d("LEAP MOTION", body);
                //VRToast toast = new VRToast(context, body, Toast.LENGTH_SHORT);
                //toast.show();
            }
        });
    }

    @Override
    public Response serve(IHTTPSession session) {
        Log.d("HTTP", "New request");
        String uri = session.getUri();
        final Endpoint endpoint = endpoints.get(uri);
        if(endpoint != null) {
            final Map<String, String> map = new HashMap<String, String>();
            Method method = session.getMethod();
            if (Method.PUT.equals(method) || Method.POST.equals(method)) {
                try {
                    session.parseBody(map);
                    new Thread(new Runnable() {
                        @Override
                        public void run () {
                            try {
                                endpoint.execute(map.get("postData"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (IOException | ResponseException e) {
                    e.printStackTrace();
                }
            }
        }
        return newFixedLengthResponse("");
    }
    
    public void addEndpoint(Endpoint endpoint) {
        endpoints.put(endpoint.getUri(), endpoint);
    }
}
