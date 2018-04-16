package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.scify.engine.GameEvent;
import org.scify.engine.RenderingEngine;
import org.scify.engine.UserInputHandler;
import org.scify.engine.audio.AudioEngine;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;
import org.scify.moonwalker.app.ui.renderables.RenderableManager;
import org.scify.moonwalker.app.ui.sound.GdxAudioEngine;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class MoonWalkerRenderingEngine implements RenderingEngine<MoonWalkerGameState> {
    /**
     * The rendering engine processes the game events, one at a time.
     * The currently processed {@link GameEvent} may block any UI input.
     */
    private GameEvent currentGameEvent;

    protected long lLastUpdate = -1L;
    protected Image worldImg;
    protected MoonWalkerGameState currentGameState;
    protected AudioEngine audioEngine;
    protected CameraController cameraController;
    protected AppInfo appInfo;
    protected World world;
    protected RenderableManager renderableManager;
    protected ThemeController themeController;
    protected UserInputHandlerImpl userInputHandler;
    protected Label fpsLabel;
    protected SpriteBatch batch;
    protected Stage stage;
    protected Viewport gameViewport;
    protected ResourceLocator resourceLocator;
    protected boolean bDisposalOngoing;
    protected boolean audioEnabled;

    public MoonWalkerRenderingEngine(UserInputHandler userInputHandler, SpriteBatch batch, Stage stage) {
        this.resourceLocator = new ResourceLocator();
        cameraController = new CameraController();
        this.userInputHandler = (UserInputHandlerImpl) userInputHandler;
        audioEngine = new GdxAudioEngine();
        appInfo = AppInfo.getInstance();
        this.batch = batch;
        this.stage = stage;
        gameViewport = stage.getViewport();
        themeController = new ThemeController();
        renderableManager = new RenderableManager(themeController, userInputHandler);
        renderableManager.setBatch(batch);
        renderableManager.setStage(stage);
        cameraController.initCamera(stage);
        createBackgroundDefaultImg();
        initFPSLabel();
        audioEngine.pauseCurrentlyPlayingAudios();
        audioEngine.loadSound("audio/mainMenu/menu.mp3");
        audioEngine.loadSound("audio/button1.mp3");
        audioEnabled = true;
        // TODO music should be added from episode rules
        //audioEngine.playSoundLoop("audio/episode_1/music.wav");
        printDebugInfo();
        resetEngine();
    }

    private void printDebugInfo() {
        System.out.println("\n\n");
        System.out.println("Screen height: " + appInfo.getScreenHeight());
        System.out.println("Screen width: " + appInfo.getScreenWidth());
        System.out.println("Density: " + Gdx.graphics.getDensity());
        System.out.println("GL version: " + Gdx.graphics.getGLVersion().getDebugVersionString());
        System.out.println("\n\n");
    }

    private void initFPSLabel() {
        fpsLabel = new Label("", themeController.getSkin());
        fpsLabel.setStyle(new Label.LabelStyle(themeController.getFont(), Color.RED));
        fpsLabel.setPosition(20, appInfo.getScreenHeight() - 20);
        // fps label has twice the normal font size
        fpsLabel.setFontScale(2);
    }

    protected void createBackgroundDefaultImg() {
        // Init empty pixmap
        Pixmap pTmp = new Pixmap(appInfo.getScreenWidth(), appInfo.getScreenHeight(), Pixmap.Format.RGB565);
        worldImg = new Image(new Texture(pTmp));
        worldImg.setWidth(appInfo.getScreenWidth());
        worldImg.setHeight(appInfo.getScreenHeight());
    }

    @Override
    public void setGameState(MoonWalkerGameState gameState) {
        this.currentGameState = gameState;
        if (this.world == null)
            this.world = gameState.world;
    }

    @Override
    public void drawGameState(MoonWalkerGameState currentState) {
        if (!bDisposalOngoing) {
            List<GameEvent> eventsList = currentState.getEventQueue();
            handleGameEvents(eventsList);
            drawRenderables(currentState);
        }
    }

    protected void drawRenderables(MoonWalkerGameState currentState) {
        if (!bDisposalOngoing) {
            synchronized (currentState.getRenderableList()) {
                for (Renderable renderable : currentState.getRenderableList()) {
                    drawRenderable(renderable);
                }
                //renderableManager.printActors();
            }
        }
    }

    protected void drawRenderable(org.scify.engine.renderables.Renderable renderable) {
        if (!bDisposalOngoing) {
            if (renderableManager.renderableExists(renderable)) {
                renderableManager.drawRenderable(renderable);
            } else {
                renderableManager.createAndAddRenderable(renderable);
            }
        }
    }

    private void handleGameEvents(List<GameEvent> eventsList) {
        if (!bDisposalOngoing) {
            // synchronized ensures that iterator.remove will be thread safe
            // on the passed list instance.
            synchronized (eventsList) {
                ListIterator<GameEvent> listIterator = eventsList.listIterator();
                while (listIterator.hasNext()) {
                    currentGameEvent = listIterator.next();
                    if (new Date().getTime() > currentGameEvent.delay)
                        handleCurrentGameEvent(currentGameEvent, listIterator);
                }
            }
        }
    }

    private void handleCurrentGameEvent(GameEvent gameEvent, ListIterator<GameEvent> listIterator) {
        String eventType = gameEvent.type;
        switch (eventType) {
            case "BACKGROUND_IMG_UI":
                String imgPath = (String) gameEvent.parameters;
                worldImg.setDrawable(new SpriteDrawable(new Sprite(new Texture(resourceLocator.getFilePath(imgPath)))));
                listIterator.remove();
                break;
            case "BORDER_UI":
                audioEngine.playSound("audio/bump.wav");
                listIterator.remove();
                break;
            case "EPISODE_SUCCESS_UI":
                audioEngine.playSound("audio/success.wav");
                listIterator.remove();
                break;
            case "AUDIO_TOGGLE_UI":
                if (audioEnabled) {
                    audioEnabled = false;
                    audioEngine.stopSound("audio/mainMenu/menu.mp3");
                }else {
                    audioEnabled = true;
                    audioEngine.playSoundLoop("audio/mainMenu/menu.mp3");
                }
                listIterator.remove();
                break;
            case "AUDIO_LOAD_UI":
                audioEngine.loadSound((String) gameEvent.parameters);
                listIterator.remove();
                break;
            case "AUDIO_DISPOSE_UI":
                audioEngine.disposeSound((String) gameEvent.parameters);
                listIterator.remove();
                break;
            case "AUDIO_START_UI":
                if (new Date().getTime() > gameEvent.delay) {
                    if (audioEnabled)
                        audioEngine.playSound((String) gameEvent.parameters);
                    listIterator.remove();
                }
                break;
            case "AUDIO_START_LOOP_UI":
                if (new Date().getTime() > gameEvent.delay) {
                    if (audioEnabled)
                        audioEngine.playSoundLoop((String) gameEvent.parameters);
                    listIterator.remove();
                }
                break;
            case "AUDIO_STOP_UI":
                if (new Date().getTime() > gameEvent.delay) {
                    if (audioEnabled)
                        audioEngine.stopSound((String) gameEvent.parameters);
                    listIterator.remove();
                }
                break;
            case "UPDATE_LABEL_TEXT_UI":
                updateLabelText((HashMap.SimpleEntry<org.scify.engine.renderables.Renderable, String>) currentGameEvent.parameters);
                listIterator.remove();
                break;
        }
    }

    private void updateLabelText(HashMap.SimpleEntry<Renderable, String> parameters) {
        Actor actor = renderableManager.getOrCreateActorResourceFor(parameters.getKey());
        Label label = (Label) actor;
        label.setText(parameters.getValue());
    }

    protected synchronized void resetEngine() {
        bDisposalOngoing = true;
        renderableManager.reset();
        bDisposalOngoing = false;
    }

    @Override
    public void cancelCurrentRendering() {

    }

    @Override
    public synchronized void disposeRenderables() {
        bDisposalOngoing = true;
        renderableManager.dispose();
        resetEngine();
        bDisposalOngoing = false;
    }

    @Override
    public void initialize() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
        appInfo.setScreenWidth(width);
        appInfo.setScreenHeight(height);
    }

    @Override
    public synchronized void disposeResources() {
        bDisposalOngoing = true;
        System.out.println("disposing rendering engine resources...");
        themeController.dispose();
        cameraController.disposeResources();
        audioEngine.disposeResources();
        bDisposalOngoing = false;
    }

    public void render(Float delta) {
        if (!bDisposalOngoing && currentGameState != null) {
            this.world = currentGameState.world;
            long lNewTime = new Date().getTime();
            if (lNewTime - lLastUpdate < 10L) {// If no less than 1/5 sec has passed
                Thread.yield();
                return; // Do nothing
            } else {
                drawComponents(delta, lNewTime);
                cameraController.render(world);
                cameraController.setProjectionMatrix(batch);
                lLastUpdate = lNewTime;
            }
        }
    }

    protected void drawComponents(Float delta, long lNewTime) {
        if (!bDisposalOngoing) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            //stage.setDebugAll(true);
            synchronized (batch) {
                batch.begin();
                fpsLabel.setText(String.valueOf(1000 / (lNewTime - lLastUpdate)));
                fpsLabel.draw(batch, 1);
                worldImg.draw(batch, 1);
                drawGameState(currentGameState);
                batch.end();
                cameraController.update();
                stage.act(delta);
                stage.draw();
            }
        }
    }
}
