package gonmolon.desktopvr.vr;

import android.graphics.Color;
import android.opengl.Matrix;

import org.rajawali3d.materials.Material;
import org.rajawali3d.math.Matrix4;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;

public class GazePointer extends Sphere implements Renderable {

    private static final float SIZE = 0.01f;

    private DesktopRenderer renderer;
    private float[] absolutePos = new float[4];
    private final float[] relativePos = new float[]{0, 0, -3f, 1.0f};
    private float[] headViewMatrix = new float[16];
    private PointerStatus status;

    public GazePointer(DesktopRenderer renderer) {
        super(SIZE, 12, 12);
        this.renderer = renderer;
        status = PointerStatus.NORMAL;
        Material material = new Material();
        material.setColor(Color.RED);
        setMaterial(material);
        renderer.getCurrentScene().addChild(this);
    }

    public void refresh() {
        Matrix4 matrix = new Matrix4();
        matrix.setAll(renderer.mHeadViewMatrix);
        matrix.inverse().toFloatArray(headViewMatrix);
        Matrix.multiplyMV(absolutePos, 0, headViewMatrix, 0, relativePos, 0);
        Vector3 pos = new Vector3(absolutePos[0], absolutePos[1], absolutePos[2]);
        pos.add(renderer.position);
        setPosition(pos);
    }

    public void setStatus(PointerStatus... statuses) {
        status = getPredominantStatus(statuses);
        if(status == PointerStatus.INVISIBLE) {
            setVisible(false);
        } else {
            setVisible(true);
            if(status == PointerStatus.NORMAL) {
                setScale(1.0f);
            } else if(status == PointerStatus.CLICKABLE) {
                setScale(1.7f);
            }
        }
    }

    private PointerStatus getPredominantStatus(PointerStatus[] statuses) {
        for(PointerStatus status : statuses) {
            if(status == PointerStatus.INVISIBLE) {
                return PointerStatus.INVISIBLE;
            } else if(status == PointerStatus.CLICKABLE) {
                return PointerStatus.CLICKABLE;
            }
        }
        return PointerStatus.NORMAL;
    }

    public enum PointerStatus {
        NORMAL, CLICKABLE, INVISIBLE
    }
}
