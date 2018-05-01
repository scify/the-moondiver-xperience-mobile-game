package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.scify.engine.GameEvent;
import org.scify.engine.RenderingEngine;
import org.scify.engine.UserInputHandler;
import org.scify.engine.audio.AudioEngine;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.MoonwalkerUIPainter;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.actors.IContainerActor;
import org.scify.moonwalker.app.ui.actors.Updateable;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;
import org.scify.moonwalker.app.ui.sound.GdxAudioEngine;

import java.util.*;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;

public class MoonWalkerRenderingEngine implements RenderingEngine<MoonWalkerGameState> {
    /**
     * The rendering engine processes the game events, one at a time.
     * The currently processed {@link GameEvent} may block any UI input.
     */
    private GameEvent currentGameEvent;

    protected long lLastUpdate = -1L;
    protected Texture worldImgTexture;
    protected MoonWalkerGameState currentGameState;
    protected AudioEngine audioEngine;
    protected CameraController cameraController;
    protected AppInfo appInfo;
    protected World world;
    protected LGDXRenderableBookKeeper bookKeeper;
    protected ThemeController themeController;
    protected UserInputHandlerImpl userInputHandler;
    protected Batch batch;
    protected Stage stage;
    protected Viewport gameViewport;
    protected ResourceLocator resourceLocator;
    protected boolean bDisposalOngoing;
    protected boolean audioEnabled;
    protected MoonwalkerUIPainter painter;

    public MoonWalkerRenderingEngine(UserInputHandler userInputHandler, SpriteBatch batch, Stage stage) {
        this.resourceLocator = new ResourceLocator();
        cameraController = new CameraController();
        cameraController.initCamera(stage);

        this.userInputHandler = (UserInputHandlerImpl) userInputHandler;
        audioEngine = new GdxAudioEngine();
        themeController = new ThemeController();

        appInfo = AppInfo.getInstance();
        this.batch = batch;
        this.stage = stage;

        painter = new MoonwalkerUIPainter(stage, cameraController);
        painter.setOverallBackground(createBackgroundDefaultImg());
        painter.setFont(themeController.getFont());
        painter.setSkin(themeController.getSkin());

        gameViewport = stage.getViewport();
        try {
            bookKeeper = LGDXRenderableBookKeeper.initBookKeeper(themeController, userInputHandler);
        } catch (org.scify.moonwalker.app.ui.LGDXRenderableBookKeeper.AlreadyInitializedBookKeeperException e) {
            System.err.println("WARNING: Keeper already initialized. Full error:");
            e.printStackTrace(System.err);
        }

        bookKeeper.setBatch(batch);
        bookKeeper.setStage(stage);

        audioEngine.pauseCurrentlyPlayingAudios();
        audioEngine.loadSound("audio/episode_mainMenu/bg.mp3");
        audioEngine.loadSound("audio/button1.mp3");
        audioEngine.loadSound("audio/message.mp3");
        //TMP
        audioEngine.loadSound("audio/episode_forest/bg.mp3");

        audioEnabled = true;
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


    protected Image createBackgroundDefaultImg() {
        // Init empty pixmap
        Pixmap pTmp = new Pixmap(appInfo.getScreenWidth(), appInfo.getScreenHeight(), Pixmap.Format.RGB565);
        worldImgTexture = new Texture(pTmp);
        Image worldImg = new Image(worldImgTexture);
        worldImg.setWidth(appInfo.getScreenWidth());
        worldImg.setHeight(appInfo.getScreenHeight());

        return worldImg;
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
            }
        }
    }

    /**
     * This function
     * @param renderable
     */
    protected void drawRenderable(Renderable renderable) {
        if (!bDisposalOngoing) {
            // Get the UI representation of the renderable
            Object uiRepresentationOfRenderable = bookKeeper.getUIRepresentationOfRenderable(renderable);

            // Then draw the renderable itself, if needed (sprites always need to be updated)
            if (renderable.needsUpdate() || !(uiRepresentationOfRenderable instanceof Actor)) {

                // if the uiRepresentationOfRenderable implements the Updatable interface, pass the renderable as argument
                // for it to be updated
                if(uiRepresentationOfRenderable instanceof Updateable) {
                    ((Updateable) uiRepresentationOfRenderable).update(renderable);
                }

                painter.drawUIRenderable(uiRepresentationOfRenderable, renderable);
                // and share that it is now updated
                renderable.wasUpdated();
            }

            // If actor is a container
            if (uiRepresentationOfRenderable instanceof IContainerActor) {
                // For every contained actor
                for (Map.Entry<Actor,Renderable> mearCur : ((IContainerActor<Renderable>)uiRepresentationOfRenderable).getChildrenActorsAndRenderables().entrySet()) {
                    // draw it
                    drawRenderable(mearCur.getValue());
                }
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
                painter.setOverallBackground(createBackgroundCustomImg(imgPath));
                listIterator.remove();
                break;
            case "SCREEN_FADE_OUT":
                listIterator.remove();
                Action fadeOut = fadeOut((Float) gameEvent.parameters);
                stage.getRoot().addAction(fadeOut);
                break;
            case "SCREEN_FADE_IN":
                stage.getRoot().getColor().a = 0;
                Action action = fadeIn((Float) gameEvent.parameters);
                stage.getRoot().addAction(action);
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
                    audioEngine.stopSound("audio/episode_mainMenu/bg.mp3");
                }else {
                    audioEnabled = true;
                    audioEngine.playSoundLoop("audio/episode_mainMenu/bg.mp3");
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


    private Image createBackgroundCustomImg(String imgPath) {
        // Free existing texture, if available
        if (worldImgTexture != null) {
            worldImgTexture.dispose();
        }
        // Update texture
        worldImgTexture = new Texture(resourceLocator.getFilePath(imgPath));
        return new Image(worldImgTexture);
    }

    private void updateLabelText(HashMap.SimpleEntry<Renderable, String> parameters) {
        Actor actor = bookKeeper.getOrCreateActorResourceFor(parameters.getKey());
        Label label = (Label) actor;
        label.setText(parameters.getValue());
    }

    protected synchronized void resetEngine() {
        bDisposalOngoing = true;
        bookKeeper.reset();
        bDisposalOngoing = false;
    }

    @Override
    public void cancelCurrentRendering() {

    }

    @Override
    public synchronized void disposeRenderables() {
        bDisposalOngoing = true;
        bookKeeper.dispose();
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
        worldImgTexture.dispose();

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
                batch = stage.getBatch();
                synchronized (batch) {
                    batch.begin();

                    painter.updateStageBG(delta, lNewTime, lLastUpdate);
                    drawGameState(currentGameState);

                    batch.end();

                    cameraController.update();
                    stage.act(delta);
                    stage.draw();
                }
                cameraController.render(world);
                cameraController.setProjectionMatrix(batch);
                lLastUpdate = lNewTime;
            }
        }
    }


}
