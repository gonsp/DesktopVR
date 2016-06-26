package gonmolon.desktopvr.vr;

import android.graphics.Color;
import android.opengl.Matrix;
import android.util.Log;

import org.rajawali3d.math.Matrix4;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;

public class Window extends Layout implements VRListener {

    DesktopRenderer renderer;

    private Menu menu;
    private WindowContent content; //TODO own class IMPORTANT
    double[][] transformationMatrix;

    public Window(DesktopRenderer renderer, float width, float height) {
        super(width, height, LayoutParams.VERTICAL);
        renderer.getCurrentScene().addChild(this);

        this.renderer = renderer;
        transformationMatrix = new double[3][3];

        menu = new Menu(this);

        Layout test = new Layout(this, width, height-menu.getHeight(), LayoutParams.VERTICAL);
        test.setBackground(Color.WHITE);
    }

    private static void printVector(Vector3 vector) {
        Log.d("HELLOUUUUU", String.valueOf(vector.x) + ", " + vector.y + ", " + vector.z);
    }

    private Vector3 getDir() {
        return new Vector3(getLookAt()).subtract(getPosition());
    }

    public boolean isLookingAt() {
        Vector3 cameraPos = new Vector3(renderer.position);
        Vector3 cameraDir = new Vector3(renderer.getCameraDir());
        Vector3 windowPos = new Vector3(getPosition());
        Vector3 windowDir = getDir();
        double t = windowDir.dot(windowPos.subtract(cameraPos)) / windowDir.dot(cameraDir);
        Vector3 intersection = cameraPos.add(cameraDir.multiply(t));
        intersection.subtract(windowPos);
        Vector3 relativePosition = transformPosition(intersection);
        return t >= 0 && relativePosition.x >= -width/2 && relativePosition.x <= width/2 && relativePosition.y >= -height/2 && relativePosition.y <= height/2;
    }

    private Vector3 transformPosition(Vector3 pos) {
        double[] aux = pos.toArray();
        double[] res = new double[3];
        for(int i = 0; i < 3; ++i) {
            res[i] = 0;
            for(int j = 0; j < 3; ++j) {
                res[i] += transformationMatrix[i][j] * aux[j];
            }
        }
        return new Vector3(res);
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
        setLookAt(renderer.position);
        Vector3 v1 = getDir();
        Vector3 v2 = getDir();
        Vector3 v3 = getDir();
        v1.cross(0, 1, 0);
        v2.cross(v1);
        v1.normalize();
        v2.normalize();
        v3.normalize();

        Matrix4 matrix = new Matrix4(new double[]{v1.x, v1.y, v1.z, 0, v2.x, v2.y, v2.z, 0, v3.x, v3.y, v3.z, 0, 0, 0, 0, 0});
        matrix.inverse();
        double[] aux = matrix.getDoubleValues();

        /*

        transformationMatrix[0][0] = aux[matrix.M00];
        transformationMatrix[0][1] = aux[matrix.M10];
        transformationMatrix[0][2] = aux[matrix.M20];
        transformationMatrix[1][0] = aux[matrix.M01];
        transformationMatrix[1][1] = aux[matrix.M11];
        transformationMatrix[1][2] = aux[matrix.M21];
        transformationMatrix[2][0] = aux[matrix.M02];
        transformationMatrix[2][1] = aux[matrix.M12];
        transformationMatrix[2][2] = aux[matrix.M22];

        */

        transformationMatrix[0][0] = v1.x;
        transformationMatrix[0][1] = v1.y;
        transformationMatrix[0][2] = v1.z;
        transformationMatrix[1][0] = v2.x;
        transformationMatrix[1][1] = v2.y;
        transformationMatrix[1][2] = v2.z;
        transformationMatrix[2][0] = v3.x;
        transformationMatrix[2][1] = v3.y;
        transformationMatrix[2][2] = v3.z;

        printVector(v1);
        printVector(v2);
        printVector(v3);
    }

    @Override
    public void onClick() {
    }

    @Override
    public void onStartLooking() {

    }

    @Override
    public void onStopLooking() {

    }

    @Override
    public void onLongLooking() {

    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void close() {
        renderer.getCurrentScene().removeChild(this);
    }
}
