package gonmolon.desktopvr;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public abstract class Element implements VRListener {

    private static final int COORDS_PER_VERTEX = 3;

    protected VRView vrView;

    protected int shaderID = -1;
    protected float[] model;
    private boolean clickable = true;

    protected FloatBuffer vertices;
    protected float[] COORDS;
    protected FloatBuffer colors;
    protected float[] COLORS;
    protected FloatBuffer normals;
    protected float[] NORMALS;

    protected int elementPositionParam;
    protected int elementNormalParam;
    protected int elementColorParam;
    protected int elementModelParam;
    protected int elementModelViewParam;
    protected int elementModelViewProjectionParam;
    protected int elementLightPosParam;

    protected Element(VRView vrView, String shaderName, int vertexShaderRes, int fragmentShaderRes, float[] COORDS, float[] NORMALS, float[] COLORS) {
        this.vrView = vrView;
        shaderID = vrView.shaderContainer.getShader(shaderName);
        if(shaderID == -1) {
            shaderID = vrView.shaderContainer.addShader(shaderName, vertexShaderRes, fragmentShaderRes);
        }

        model = new float[16];
        this.COORDS = COORDS;
        this.NORMALS = NORMALS;
        this.COLORS = COLORS;

        vertices = loadData(COORDS);
        normals = loadData(NORMALS);
        colors = loadData(COLORS);

        GLES20.glUseProgram(shaderID);

        elementModelParam = GLES20.glGetUniformLocation(shaderID, "u_Model");
        elementModelViewParam = GLES20.glGetUniformLocation(shaderID, "u_MVMatrix");
        elementModelViewProjectionParam = GLES20.glGetUniformLocation(shaderID, "u_MVP");
        elementLightPosParam = GLES20.glGetUniformLocation(shaderID, "u_LightPos");

        elementPositionParam = GLES20.glGetAttribLocation(shaderID, "a_Position");
        elementNormalParam = GLES20.glGetAttribLocation(shaderID, "a_Normal");
        elementColorParam = GLES20.glGetAttribLocation(shaderID, "a_Color");

        VRView.checkGLError(shaderName);
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
        GLES20.glUseProgram(shaderID);

        float[] modelView = new float[16];
        float[] modelViewProjection = new float[16];
        Matrix.multiplyMM(modelView, 0, vrView.view, 0, model, 0);
        Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);

        GLES20.glUniform3fv(elementLightPosParam, 1, vrView.lightPosInEyeSpace, 0);
        GLES20.glUniformMatrix4fv(elementModelParam, 1, false, model, 0);
        GLES20.glUniformMatrix4fv(elementModelViewParam, 1, false, modelView, 0);
        GLES20.glUniformMatrix4fv(elementModelViewProjectionParam, 1, false, modelViewProjection, 0);
        GLES20.glVertexAttribPointer(elementPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertices);
        GLES20.glVertexAttribPointer(elementNormalParam, 3, GLES20.GL_FLOAT, false, 0, normals);
        GLES20.glVertexAttribPointer(elementColorParam, 4, GLES20.GL_FLOAT, false, 0, colors);

        GLES20.glEnableVertexAttribArray(elementPositionParam);
        GLES20.glEnableVertexAttribArray(elementNormalParam);
        GLES20.glEnableVertexAttribArray(elementColorParam);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, COORDS.length);

        VRView.checkGLError("Drawing " + shaderID);
    }

    private static FloatBuffer loadData(float[] data) {
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

    protected void scale(float width, float height) {
        Matrix.setIdentityM(model, 0);
        Matrix.scaleM(model, 0, width, height, 0);
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}
