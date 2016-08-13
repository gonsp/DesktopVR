package gonmolon.desktopvr.vnc;

public abstract class Endpoint {

    private String uri;

    public Endpoint(String uri) {
        if(uri.charAt(0) != '/') {
            uri = "/" + uri;
        }
        this.uri = uri;
    }

    public abstract void execute(String body);

    public String getUri() {
        return uri;
    }
}