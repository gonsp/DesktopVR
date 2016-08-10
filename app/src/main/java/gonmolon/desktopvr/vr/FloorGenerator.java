package gonmolon.desktopvr.vr;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.terrain.SquareTerrain;
import org.rajawali3d.terrain.TerrainGenerator;

import gonmolon.desktopvr.R;

public abstract class FloorGenerator {

    public static SquareTerrain generate(DesktopRenderer renderer, boolean sky) {
        renderer.getCurrentCamera().setFarPlane(1000);
        SquareTerrain floor;
        Bitmap bmp = BitmapFactory.decodeResource(renderer.getContext().getResources(), R.drawable.negy);
        try {
            SquareTerrain.Parameters terrainParams = SquareTerrain.createParameters(bmp);
            terrainParams.setScale(40f, 54f, 40f);
            terrainParams.setColorMapBitmap(bmp);
            floor = TerrainGenerator.createSquareTerrainFromBitmap(terrainParams, false);
            bmp.recycle();
            Material material = new Material();
            material.enableLighting(true);
            material.useVertexColors(true);
            material.setDiffuseMethod(new DiffuseMethod.Lambert());
            material.setColorInfluence(.5f);
            floor.setY(-100);
            floor.setMaterial(material);
            renderer.getCurrentScene().addChild(floor);

            if(sky) {
                renderer.getCurrentScene().setSkybox(R.drawable.posx, R.drawable.negx, R.drawable.posy, R.drawable.negy, R.drawable.posz, R.drawable.negz);
            }

            return floor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
