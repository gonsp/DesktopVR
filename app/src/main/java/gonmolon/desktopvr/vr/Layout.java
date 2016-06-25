package gonmolon.desktopvr.vr;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.primitives.RectangularPrism;

import java.util.ArrayList;


public class Layout extends Element {

    private LayoutParams orientation;
    private ArrayList<Element> children;
    private RectangularPrism background;
    private float offset = 0;

    public Layout(float width, float height, LayoutParams orientation) {
        super(width, height);
        this.orientation = orientation;
        isContainer(true);
        if(orientation == LayoutParams.HORIZONTAL) {
            offset = -width/2;
        } else if(orientation == LayoutParams.VERTICAL) {
            offset = height/2;
        }
        children = new ArrayList<>();
    }

    public Layout(Layout parent, float width, float height, LayoutParams orientation) {
        this(width, height, orientation);
        parent.addChild(this);
    }

    public void setBackground(int color) {
        if(background == null) {
            background = new RectangularPrism(width, getHeight(), 0);
            addChild(background);
            background.setPosition(0, 0, 0);
        }
        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColor(color);
        background.setMaterial(material);
    }

    public void addChild(Element element) {
        super.addChild(element);
        if(orientation == LayoutParams.HORIZONTAL) {
            element.setPosition(offset + element.getWidth()/2, 0, 0);
            offset += element.getWidth();
        } else if(orientation == LayoutParams.VERTICAL) {
            element.setPosition(0, offset - element.getHeight()/2, 0);
            offset -= element.getHeight();
        }
        children.add(element);
    }



    public enum LayoutParams {
        HORIZONTAL, VERTICAL
    }
}
