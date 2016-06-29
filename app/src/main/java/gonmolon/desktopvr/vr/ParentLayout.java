package gonmolon.desktopvr.vr;

import org.rajawali3d.math.vector.Vector3;

public abstract class ParentLayout extends Layout {

    private DesktopRenderer renderer;
    double[][] transformationMatrix;

    public ParentLayout(DesktopRenderer renderer, float width, float height, LayoutParams orientation) {
        super(width, height, orientation);

        this.renderer = renderer;
        transformationMatrix = new double[3][3];

        renderer.getCurrentScene().addChild(this);
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
        if(t >= 0 && relativePosition.x >= -width/2 && relativePosition.x <= width/2 && relativePosition.y >= -height/2 && relativePosition.y <= height/2) {
            return setLookingAt(true, relativePosition.x, relativePosition.y);
        }
        return false;
    }

    private Vector3 getDir() {
        return new Vector3(getLookAt()).subtract(getPosition());
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

    public void setAngularPosition(double angle, double height, double distance) {
        angle = (angle/360)*2*Math.PI;
        Vector3 pos = new Vector3(distance, 0, 0);
        pos.rotateY(angle);
        pos.y = height;
        setPosition(pos.x, pos.y, pos.z);
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
        setLookAt(renderer.position);
        Vector3 v1 = getDir();
        Vector3 v2;
        Vector3 v3 = getDir();
        v1.cross(0, 1, 0);
        v2 = new Vector3(v1);
        v2.cross(v3);
        v1.normalize();
        v2.normalize();
        v3.normalize();

        transformationMatrix[0][0] = v1.x;
        transformationMatrix[0][1] = v1.y;
        transformationMatrix[0][2] = v1.z;
        transformationMatrix[1][0] = v2.x;
        transformationMatrix[1][1] = v2.y;
        transformationMatrix[1][2] = v2.z;
        transformationMatrix[2][0] = v3.x;
        transformationMatrix[2][1] = v3.y;
        transformationMatrix[2][2] = v3.z;
    }

    public void close() {
        renderer.getCurrentScene().removeChild(this);
    }
}
