package gonmolon.desktopvr.vr;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.MotionEvent;

import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.NormalMapTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.terrain.SquareTerrain;
import org.rajawali3d.terrain.TerrainGenerator;

import gonmolon.desktopvr.R;

public class DesktopRenderer extends VRRenderer {

    private SquareTerrain terrain;
    private GazePointer reticle;
    private Sphere bola;

    public DesktopRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        getCurrentCamera().setFarPlane(1000);

        DirectionalLight light = new DirectionalLight(0.2f, -1f, 0f);
        light.setPower(.7f);
        getCurrentScene().addLight(light);

        createTerrain();

        reticle = new GazePointer(this);

        bola = new Sphere(1, 12, 12);
        Material material = new Material();
        material.setColor(Color.YELLOW);
        bola.setMaterial(material);
        bola.setColor(Color.YELLOW);
        bola.setPosition(0, 0, -6);
        getCurrentScene().addChild(bola);
    }

    public void createTerrain() {
        //
        // -- Load a bitmap that represents the terrain. Its color values will
        //    be used to generate heights.
        //

        Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.terrain);

        try {
            SquareTerrain.Parameters terrainParams = SquareTerrain.createParameters(bmp);
            // -- set terrain scale
            terrainParams.setScale(4f, 54f, 4f);
            // -- the number of plane subdivisions
            terrainParams.setDivisions(128);
            // -- the number of times the textures should be repeated
            terrainParams.setTextureMult(4);
            //
            // -- Terrain colors can be set by manually specifying base, middle and
            //    top colors.
            //
            terrainParams.setBasecolor(Color.argb(255, 0, 0, 0));
            terrainParams.setMiddleColor(Color.argb(255, 200, 200, 200));
            terrainParams.setUpColor(Color.argb(255, 0, 30, 0));
            //
            // -- However, for this example we'll use a bitmap
            //
            //terrainParams.setColorMapBitmap(bmp);
            //
            // -- create the terrain
            //
            terrain = TerrainGenerator.createSquareTerrainFromBitmap(terrainParams, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        // -- The bitmap won't be used anymore, so get rid of it.
        //
        bmp.recycle();

        //
        // -- A normal map material will give the terrain a bit more detail.
        //
        Material material = new Material();
        material.enableLighting(true);
        material.useVertexColors(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        try {
            Texture groundTexture = new Texture("ground", R.drawable.ground);
            groundTexture.setInfluence(.5f);
            material.addTexture(groundTexture);
            material.addTexture(new NormalMapTexture("groundNormalMap", R.drawable.groundnor));
            material.setColorInfluence(0);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        //
        // -- Blend the texture with the vertex colors
        //
        material.setColorInfluence(.5f);
        terrain.setY(-100);
        terrain.setMaterial(material);

        getCurrentScene().addChild(terrain);
    }

    @Override
    public void onRender(long elapsedTime, double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
        if(reticle != null) {
            reticle.refresh(false);
        }
    }


    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    public void onCardboardTrigger() {

    }
}
