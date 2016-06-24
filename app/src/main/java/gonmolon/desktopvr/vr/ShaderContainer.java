package gonmolon.desktopvr.vr;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ShaderContainer {

    private static final String TAG = "ShaderContainer";
    private HashMap<String, Integer> shaders;
    private Context context;

    public ShaderContainer(Context context) {
        shaders = new HashMap<>();
        this.context = context;
    }

    public int getShader(String name) {
        if(shaders.containsKey(name)) {
            return shaders.get(name);
        } else {
            return -1;
        }
    }

    public int addShader(String name, int vertexShaderRes, int fragmentShaderRes) {
        int vertexShaderID = loadGLShader(GLES20.GL_VERTEX_SHADER, vertexShaderRes);
        int fragmentShaderID = loadGLShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderRes);
        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShaderID);
        GLES20.glAttachShader(program, fragmentShaderID);
        GLES20.glLinkProgram(program);
        shaders.put(name, program);
        return program;
    }

    private int loadGLShader(int type, int resId) {
        String code = readRawTextFile(resId);
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }

    private String readRawTextFile(int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
