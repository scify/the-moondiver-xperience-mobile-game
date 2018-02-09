package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.scify.moonwalker.app.helpers.GameInfo;

public class CameraController {

    private GameInfo gameInfo;
    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;
    private Camera mainCamera;

    public CameraController() {
        gameInfo = GameInfo.getInstance();
    }

    public void initCamera(Stage stage) {
        int width = gameInfo.getScreenWidth();
        int height = gameInfo.getScreenHeight();

        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, width,
                height);
        box2DCamera.position.set(width / 2f, height / 2f, 0);
        mainCamera = stage.getCamera();
        debugRenderer = new Box2DDebugRenderer();
    }

    public void disposeResources() {
        debugRenderer.dispose();
    }

    public void update() {
        mainCamera.update();
    }

    public void render(World world) {
        debugRenderer.render(world, box2DCamera.combined);
    }

    public void setProjectionMatrix(Batch batch) {
        batch.setProjectionMatrix(mainCamera.combined);
    }
}