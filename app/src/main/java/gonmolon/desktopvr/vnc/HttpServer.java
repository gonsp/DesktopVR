package gonmolon.desktopvr.vnc;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class HttpServer extends NanoHTTPD {
    
    private HashMap<String, Endpoint> endpoints;

    public HttpServer(int port) {
        super(port);
        
        endpoints = new HashMap<>();

        addEndpoint(new Endpoint("test") {
            @Override
            public void execute(Map<String, String> params) {
                Log.d("HTTP", params.get("param"));
            }
        });
    }

    @Override
    public Response serve(IHTTPSession session) {
        Log.d("HTTP", "New request");
        String uri = session.getUri();
        Log.d("HTTP", "Endpoint name = " + uri);
        Endpoint endpoint = endpoints.get(uri);
        if(endpoint != null) {
            Map<String, String> files = new HashMap<String, String>();
            Method method = session.getMethod();
            if (Method.PUT.equals(method) || Method.POST.equals(method)) {
                try {
                    session.parseBody(files);
                    endpoint.execute(session.getParms());
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

    public abstract class Endpoint {
    
        private String uri;
        
        public Endpoint(String uri) {
            if(uri.charAt(0) != '/') {
                uri = "/" + uri;
            }
            this.uri = uri;
        }
        
        public abstract void execute(Map<String, String> params);

        public String getUri() {
            return uri;
        }
    }
}
