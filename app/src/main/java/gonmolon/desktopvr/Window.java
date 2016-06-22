package gonmolon.desktopvr;

public class Window extends Model{

    private static final String SHADER_NAME = "Window";
    private String name;

    public Window(String name, VRView vrView) {
        super(vrView, SHADER_NAME, R.raw.light_vertex, R.raw.passthrough_fragment);
        this.name = name;
    }

    @Override
    public void onClick() {

    }

    @Override
    public void onLooking() {

    }

    @Override
    public void onLongLooking() {

    }

    @Override
    protected void draw(float[] modelView, float[] modelViewProjection) {

    }
}
