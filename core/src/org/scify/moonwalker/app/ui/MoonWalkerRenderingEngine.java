package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.scify.engine.GameEvent;
import org.scify.engine.RenderingEngine;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.engine.Renderable;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.ui.components.ActionDialog;
import org.scify.moonwalker.app.ui.components.SelectDialog;

import java.util.*;

public class MoonWalkerRenderingEngine implements RenderingEngine<MoonWalkerGameState>, Screen {
    /**
     * The rendering engine processes the game events, one at a time.
     * The currently processed {@link GameEvent} may block any UI input.
     */
    private GameEvent currentGameEvent;
    private SpriteBatch batch;
    private long lLastUpdate = -1L;
    private Image worldImg;
    private MoonWalkerGameState currentGameState;
    private OrthographicCamera mainCamera;
    private Viewport gameViewport;
    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;
    private GameInfo gameInfo;
    private World world;
    private Map<Renderable, Sprite> renderableSpriteMap = new HashMap<>();
    private Stage stage;
    private Skin skin;
    BitmapFont customFont;
    private UserInputHandler userInputHandler;

    public MoonWalkerRenderingEngine(UserInputHandler userInputHandler) {
        this.userInputHandler = userInputHandler;
        gameInfo = GameInfo.getInstance();
        initCamera();
        batch = new SpriteBatch();
        stage = new Stage(gameViewport, batch);
        worldImg = new Image(new Texture("theworld.png"));
        worldImg.setWidth(gameInfo.getScreenWidth());
        worldImg.setHeight(gameInfo.getScreenHeight());
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        customFont = new BitmapFont(Gdx.files.internal("data/custom.fnt"));
    }

    @Override
    public void initializeGameState(MoonWalkerGameState initialState) {
        this.world = initialState.world;
    }

    private void initCamera() {
        int width = gameInfo.getScreenWidth();
        int height = gameInfo.getScreenHeight();
        mainCamera = new OrthographicCamera(width * gameInfo.getAspectRatio(), height);
        mainCamera.position.set(width / 2f, height / 2, 0);

        gameViewport = new StretchViewport(width, height,
                mainCamera);

        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, width,
                height);
        box2DCamera.position.set(width / 2f, height / 2f, 0);

        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void setGameState(MoonWalkerGameState gameState) {
        this.currentGameState = gameState;
    }

    protected Sprite getResourceFor(Renderable toDraw) {
        Sprite resource;
        // If I have an existing sprite
        if (renderableSpriteMap.containsKey(toDraw)) {
            // reuse it
            resource = renderableSpriteMap.get(toDraw);
        } else {
            // else
            // getResourceForType
            Sprite newResourceForRenderable = getResourceForType(toDraw.getType());
            // and map it to the object
            renderableSpriteMap.put(toDraw, newResourceForRenderable);
            resource = newResourceForRenderable;
        }
        return resource;
    }

    protected Sprite getResourceForType(String sType) {
        Sprite sToReturn = null;
        // Get a sprite for this world object type
        switch (sType) {
            case "PLAYER":
                Texture playerImg = new Texture("knight.png");
                Sprite playerSprite = new Sprite(playerImg);
                playerSprite.setSize((float) (gameInfo.getScreenWidth() * 0.2), (float) (gameInfo.getScreenWidth() * 0.2));
                sToReturn = playerSprite;
                break;
        }
        if (sToReturn == null)
            throw new UnsupportedRenderableTypeException("Renderable: " + sType + " is not supported.");
        return sToReturn;
    }

    @Override
    public void drawGameState(MoonWalkerGameState currentState) {
        List<GameEvent> eventsList = currentState.getEventQueue();
        handleGameEvents(eventsList);
        drawRenderables(currentState);
    }

    protected void drawRenderables(MoonWalkerGameState currentState) {
        for (Renderable renderable : currentState.getRenderableList()) {
            Sprite sToDraw = getResourceFor(renderable);
            sToDraw.setPosition(renderable.getX(), renderable.getY());
            batch.draw(sToDraw, sToDraw.getX() - (sToDraw.getWidth() / 2f), sToDraw.getY() - (sToDraw.getHeight() / 2f), sToDraw.getWidth(), sToDraw.getHeight());
        }
    }

    private void handleGameEvents(List<GameEvent> eventsList) {

            // synchronized ensures that iterator.remove will be thread safe
            // on the passed list instance.
            synchronized (eventsList) {
                ListIterator<GameEvent> listIterator = eventsList.listIterator();
                while (listIterator.hasNext()) {
                    currentGameEvent = listIterator.next();
                    handleCurrentGameEvent(listIterator);
                }
            }

    }

    private void handleCurrentGameEvent(ListIterator<GameEvent> listIterator) {
        String eventType = currentGameEvent.type;
        switch (eventType) {
            case "ADD_DIALOG_UI":
                showDialog();
                listIterator.remove();
                break;
            default:
                listIterator.remove();
                break;
        }
    }

    @Override
    public void playGameOver() {

    }

    @Override
    public void cancelCurrentRendering() {

    }

    @Override
    public void disposeDrawables() {
        batch.dispose();
        for (Map.Entry<Renderable, Sprite> entry : renderableSpriteMap.entrySet()) {
            entry.getValue().getTexture().dispose();
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addActor(worldImg);
    }

    @Override
    public void render(float delta) {
        long lNewTime = new Date().getTime();
        if (lNewTime - lLastUpdate < 50L) {// If no less than 1/10 sec has passed
            Thread.yield();
            return; // Do nothing
        } else {
            lLastUpdate = lNewTime;
            Gdx.gl.glClearColor(1, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            stage.act(delta);
            stage.draw();

            batch.begin();

            drawGameState(currentGameState);
            batch.end();
            debugRenderer.render(world, box2DCamera.combined);
            batch.setProjectionMatrix(mainCamera.combined);
            mainCamera.update();
        }
    }

    void showDialog() {
        ActionDialog dialog1 = new SelectDialog(
                "Ερώτηση",
                "Πώς λεγόταν το πρόγραμμα της Σοβιετικής \nΈνωσης που έφτασε στο φεγγάρι το 1959;",
                skin
        );
        dialog1.setInputHandler(userInputHandler);
        dialog1.alignCenter();
        dialog1.addButton("Λούνα", 1);
        dialog1.addButton("Apollo", 2);
        dialog1.addButton("NASA", 3);
        dialog1.addButton("Eclipse", 4);
        stage.addActor(dialog1.getDialog());
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        customFont.dispose();
        stage.dispose();
        world.dispose();
        debugRenderer.dispose();
        disposeDrawables();
        // TODO call physics engine to dispose bodies?
    }

}
