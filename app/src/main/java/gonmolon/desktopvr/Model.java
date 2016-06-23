package gonmolon.desktopvr;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public abstract class Model implements VRListener {

    protected VRView vrView;

    protected int shaderID = -1;
    protected float[] model;
    private boolean clickable = true;

    protected Model(VRView vrView, String shaderName, int vertexShaderRes, int fragmentShaderRes) {
        this.vrView = vrView;
        shaderID = vrView.shaderContainer.getShader(shaderName);
        if(shaderID == -1) {
            shaderID = vrView.shaderContainer.addShader(shaderName, vertexShaderRes, fragmentShaderRes);
        }
        model = new float[16];
    }

    public boolean isLooking() { //Param view direction
        boolean looking = true; //TODO implement this
        if(looking) {
            onLooking();
        }
        return looking;
    }

    public void setClick() {
        if(clickable) {
            onClick();
        }
    }

    public void draw(float[] perspective) {
        float[] modelView = new float[16];
        float[] modelViewProjection = new float[16];
        Matrix.multiplyMM(modelView, 0, vrView.view, 0, model, 0);
        Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);
        draw(modelView, modelViewProjection);
    }

    protected abstract void draw(float[] modelView, float[] modelViewProjection);

    protected static FloatBuffer loadData(float[] data) {
        FloatBuffer buffer;
        ByteBuffer aux = ByteBuffer.allocateDirect(data.length * 4);
        aux.order(ByteOrder.nativeOrder());
        buffer = aux.asFloatBuffer();
        buffer.put(data);
        buffer.position(0);
        return buffer;
    }

    public void move(float x, float y, float z) {
        Matrix.setIdentityM(model, 0);
        Matrix.translateM(model, 0, x, y, z);
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}
