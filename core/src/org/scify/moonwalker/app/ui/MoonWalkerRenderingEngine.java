package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ZIndexedStage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.sentry.Sentry;
import io.sentry.event.EventBuilder;
import org.scify.engine.GameEvent;
import org.scify.engine.RenderingEngine;
import org.scify.engine.UserInputHandler;
import org.scify.engine.audio.AudioEngine;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.MoonwalkerUIPainter;
import org.scify.moonwalker.app.game.rules.episodes.BaseEpisodeRules;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.actors.Updateable;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;
import org.scify.moonwalker.app.ui.sound.GdxAudioEngine;

import java.util.*;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;

public class MoonWalkerRenderingEngine implements RenderingEngine<MoonWalkerGameState> {
    public static final String BACKGROUND_IMG_UI = "BACKGROUND_IMG_UI";
    public static final String SCREEN_FADE_OUT = "SCREEN_FADE_OUT";
    public static final String SCREEN_FADE_IN = "SCREEN_FADE_IN";
    public static final String BORDER_UI = "BORDER_UI";
    public static final String EPISODE_SUCCESS_UI = "EPISODE_SUCCESS_UI";
    public static final String AUDIO_TOGGLE_UI = "GAME_EVENT_AUDIO_TOGGLE_UI";
    public static final String AUDIO_DISPOSE_UI = "GAME_EVENT_AUDIO_DISPOSE_UI";
    public static final String AUDIO_START_UI = "GAME_EVENT_AUDIO_START_UI";
    public static final String AUDIO_START_LOOP_UI = "GAME_EVENT_AUDIO_START_LOOP_UI";
    public static final String AUDIO_STOP_UI = "GAME_EVENT_AUDIO_STOP_UI";
    public static final String UPDATE_LABEL_TEXT_UI = "UPDATE_LABEL_TEXT_UI";
    public static final String SOUND_BUMP_PATH = "audio/bump.wav";
    public static final String SOUND_SUCCESS_PATH = "audio/success.wav";
    public static final String SOUND_EPISODE_MAIN_MENU_BG_PATH = "audio/episode_main_menu/bg.mp3";
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
    protected ZIndexedStage stage;
    protected Viewport gameViewport;
    protected ResourceLocator resourceLocator;
    protected boolean bDisposalOngoing;
    protected boolean audioEnabled;
    protected MoonwalkerUIPainter painter;

    protected int lowerUpdatedZIndex = Integer.MAX_VALUE;

    public MoonWalkerRenderingEngine(UserInputHandler userInputHandler, SpriteBatch batch, ZIndexedStage stage) {
        this.resourceLocator = ResourceLocator.getInstance();
        cameraController = new CameraController();
        cameraController.initCamera(stage);

        this.userInputHandler = (UserInputHandlerImpl) userInputHandler;
        audioEngine = new GdxAudioEngine();
        themeController = ThemeController.getInstance();

        appInfo = AppInfo.getInstance();
        this.batch = batch;
        this.stage = stage;

        painter = new MoonwalkerUIPainter(stage, cameraController);
        painter.setOverallBackground(createBackgroundDefaultImg());
        painter.setFont(themeController.getFont());
        painter.setSkin(themeController.getDefaultSkin());

        gameViewport = stage.getViewport();
        try {
            bookKeeper = LGDXRenderableBookKeeper.initBookKeeper(themeController, userInputHandler);

            bookKeeper.setBatch(batch);
            bookKeeper.setStage(stage);
        } catch (org.scify.moonwalker.app.ui.LGDXRenderableBookKeeper.AlreadyInitializedBookKeeperException e) {
            System.err.println("WARNING: Keeper already initialized. Full error:");
            e.printStackTrace(System.err);
            // Log anyway
            Sentry.capture(e);
        }

        audioEngine.pauseCurrentlyPlayingAudios();
        audioEngine.loadSound("audio/button1.mp3");
        audioEngine.loadSound("audio/message.mp3");
        audioEngine.loadSound("audio/wrong.mp3");
        audioEngine.loadSound("audio/episode_charge/power_up.mp3");
        audioEngine.loadSound("audio/episode_location/tada.mp3");
        audioEngine.loadSound("audio/episode_location/correct.mp3");
        audioEngine.loadSound("audio/episode_location/wrong.mp3");
        audioEngine.loadSound("audio/episode_location/day_passed.mp3");
        audioEngine.loadSound("audio/episode_cockpit/moon_take_off.mp3");
        audioEngine.loadSound("audio/episode_cockpit/owl.mp3");
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
        System.out.println("I am " + toString());
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
                // Get renderable list snapshot (to avoid concurrent modification)
                List<Renderable> lRenderables = new ArrayList<>(currentState.getRenderableList());
                // Sort renderables
                Collections.sort(lRenderables, new Comparator<Renderable>() {
                    @Override
                    public int compare(Renderable o1, Renderable o2) {
                        if (o1.equals(o2)) {
                            return 0;
                        }

                        // Start with z-index
                        int iRes = o1.getZIndex() - o2.getZIndex();
                        if (iRes == 0) {
                            // Continue with area (smaller gets higher zorder)
                            iRes = -(int)((o1.getWidth() * o1.getHeight()) -
                                    (o2.getWidth() * o2.getHeight()));
                        }

                        // If still the same
                        if (iRes == 0) {
                            // use hash value of id to order
                            iRes = (int)(o1.getId().hashCode() - o2.getId().hashCode());
                        }

                        return iRes; // Order from lower to upper
                    }
                });


                lowerUpdatedZIndex = Integer.MAX_VALUE; // Nothing updated yet
                for (Renderable renderable : lRenderables) {
                    drawRenderable(renderable);
                }
            }
        }
    }

    /**
     * This function
     *
     * @param renderable
     */
    protected void drawRenderable(Renderable renderable) {
        if (!bDisposalOngoing) {
            // Get the UI representation of the renderable
            Object uiRepresentationOfRenderable = bookKeeper.getUIRepresentationOfRenderable(renderable);

            // If needs update (then it also needs repaint)
            if (renderable.needsUpdate()) {
                // call update of the actor, if applicable
                if (uiRepresentationOfRenderable instanceof Updateable) {
                    ((Updateable) uiRepresentationOfRenderable).update(renderable);
                }
                // repaint
                ///
                painter.drawUIRenderable(uiRepresentationOfRenderable, renderable);
                // NOTE: We do NOT and should NOT call wasUpdated. It is the actor's responsibility to do so.

                // Update lowest updated layer
                lowerUpdatedZIndex = Math.min(lowerUpdatedZIndex, renderable.getZIndex());
            }
            // else
            else {
                // If needs repaint in itself, or something below it was painted
                if (renderable.needsRepaint() || (renderable.getZIndex() >= lowerUpdatedZIndex)) {
                    // repaint
                    painter.drawUIRenderable(uiRepresentationOfRenderable, renderable);

                    // Update lowest updated layer
                    lowerUpdatedZIndex = Math.min(lowerUpdatedZIndex, renderable.getZIndex());
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
            case BACKGROUND_IMG_UI:
                String imgPath = (String) gameEvent.parameters;
                painter.setOverallBackground(createBackgroundCustomImg(imgPath));
                listIterator.remove();
                break;
            case SCREEN_FADE_OUT:
                listIterator.remove();
                Action fadeOut = fadeOut((Float) gameEvent.parameters);
                stage.getRoot().addAction(fadeOut);
                break;
            case SCREEN_FADE_IN:
                stage.getRoot().getColor().a = 0;
                Action action = fadeIn((Float) gameEvent.parameters);
                stage.getRoot().addAction(action);
                listIterator.remove();
                break;
            case BORDER_UI:
                audioEngine.playAudio(SOUND_BUMP_PATH);
                listIterator.remove();
                break;
            case EPISODE_SUCCESS_UI:
                audioEngine.playAudio(SOUND_SUCCESS_PATH);
                listIterator.remove();
                break;
            case AUDIO_TOGGLE_UI:
                if (audioEnabled) {
                    audioEnabled = false;
                    audioEngine.stopMusic(SOUND_EPISODE_MAIN_MENU_BG_PATH);
                } else {
                    audioEnabled = true;
                    audioEngine.playMusicLoop(SOUND_EPISODE_MAIN_MENU_BG_PATH);
                }
                listIterator.remove();
                break;
            case AUDIO_DISPOSE_UI:
                audioEngine.disposeResources();
                listIterator.remove();
                break;
            case AUDIO_START_UI:
                if (new Date().getTime() > gameEvent.delay) {
                    if (audioEnabled)
                        audioEngine.playAudio((String) gameEvent.parameters);
                    listIterator.remove();
                }
                break;
            case AUDIO_START_LOOP_UI:
                if (new Date().getTime() > gameEvent.delay) {
                    if (audioEnabled)
                        audioEngine.playMusicLoop((String) gameEvent.parameters);
                    listIterator.remove();
                }
                break;
            case AUDIO_STOP_UI:
                if (new Date().getTime() > gameEvent.delay) {
                    if (audioEnabled) {
                        if (gameEvent.parameters == null) {
                            audioEngine.stopAllMusic();
                        }else {
                            audioEngine.stopMusic((String) gameEvent.parameters);
                        }
                        listIterator.remove();
                    }
                }
                break;
            case UPDATE_LABEL_TEXT_UI:
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
        worldImgTexture = bookKeeper.getTexture(imgPath);
        Image iRes = new Image(worldImgTexture);
        // Apply app size to image (stretch it to fit the app stage)
        iRes.setSize(appInfo.getScreenWidth(), appInfo.getScreenHeight());
        return iRes;
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
        bookKeeper.disposeActorsAndTextures();
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
        cameraController.disposeResources();
        worldImgTexture.dispose();
        bDisposalOngoing = false;
    }

    public void render(Float delta) {
        if (!bDisposalOngoing && currentGameState != null) {
            if(this.world == null)
                this.world = currentGameState.world;
            long lNewTime = new Date().getTime();
            if (lNewTime - lLastUpdate < 10L) {// If a trivial time has not passed
                Thread.yield();
                return; // Do nothing
            } else {
                batch = stage.getBatch();
                synchronized (batch) {
                    // TODO: Employ z-ordering even when combining components (added to stage) and sprites (stand-alone)

                    batch.begin();

                    painter.updateStageBG(delta, lNewTime, lLastUpdate);

                    batch.end();

                    cameraController.update();
                    stage.act(delta);
                    stage.draw();

                    batch.begin();
                    drawGameState(currentGameState);
                    batch.end();

                    cameraController.render(world);
                    cameraController.setProjectionMatrix(batch);
                    lLastUpdate = lNewTime;
                }

            }
        }
    }

    @Override
    public void sendHideEventToEpisode() {
        currentGameState.addGameEvent(new GameEvent(BaseEpisodeRules.GAME_EVENT_APP_HIDE, null, 0, true));

    }
}
