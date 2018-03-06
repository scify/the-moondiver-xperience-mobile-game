package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import org.scify.engine.*;
import org.scify.engine.audio.AudioEngine;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.conversation.MultipleConversationLines;
import org.scify.engine.conversation.SingleConversationLine;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.actors.AvatarWithMessageComponent;
import org.scify.moonwalker.app.ui.actors.MultipleSelectionComponent;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;
import org.scify.moonwalker.app.ui.sound.GdxAudioEngine;

import java.util.*;

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
    protected List<Actor> additionalActors;
    protected boolean bDisposalOngoing;

    public MoonWalkerRenderingEngine(UserInputHandler userInputHandler, SpriteBatch batch, Stage stage) {
        this.resourceLocator = new ResourceLocator();
        cameraController = new CameraController();
        this.userInputHandler = (UserInputHandlerImpl) userInputHandler;

        additionalActors = new ArrayList<>();
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
        worldImg = new Image(new Texture(resourceLocator.getFilePath("img/theworld.png")));
        worldImg.setWidth(appInfo.getScreenWidth());
        worldImg.setHeight(appInfo.getScreenHeight());
    }

    @Override
    public void setGameState(MoonWalkerGameState gameState) {
        this.currentGameState = gameState;
        if(this.world == null)
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

    protected void drawRenderable(Renderable renderable) {
        if (!bDisposalOngoing) {
            if(renderableManager.renderableExists(renderable)) {
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
        String eventType = currentGameEvent.type;
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
            case "UPDATE_LABEL_TEXT_UI":
                updateLabelText((HashMap.SimpleEntry<Renderable, String>) currentGameEvent.parameters);
                listIterator.remove();
                break;
            case "CONVERSATION_LINE":
                List parameters = (List) currentGameEvent.parameters;
                SingleConversationLine conversationLine = (SingleConversationLine) parameters.get(0);
                UserAction action = (UserAction) parameters.get(1);
                renderConversationLine(conversationLine, action);
                listIterator.remove();
                break;
            case "CONVERSATION_LINES":
                MultipleConversationLines multipleConversationLines = (MultipleConversationLines) currentGameEvent.parameters;
                createMultipleSelectionForConversationLines(multipleConversationLines);
                listIterator.remove();
                break;
            case "REMOVE_CONVERSATIONS":
                for(Actor actor : additionalActors)
                    actor.remove();
                listIterator.remove();
                break;
        }
    }

    private void createMultipleSelectionForConversationLines(MultipleConversationLines multipleConversationLinesComponent) {
        MultipleSelectionComponent component = new MultipleSelectionComponent(multipleConversationLinesComponent.getTitle(),
                multipleConversationLinesComponent.getAvatarImgPath());
        component.initActor(themeController.getSkin());
        int btnIndex = 1;
        for(final ConversationLine conversationLine : multipleConversationLinesComponent.getConversationLines()) {
            component.addButton(conversationLine.getText(), new UserInputHandlerImpl() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //System.out.println("answer was " + conversationLine.getText() + " with order id " + conversationLine.getId());
                    userInputHandler.addUserAction(new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, conversationLine.getId()));
                }
            }, btnIndex);
            btnIndex++;
        }
        additionalActors.add(component);
        stage.addActor(component);
    }

    private void updateLabelText(HashMap.SimpleEntry<Renderable, String> parameters) {
        Actor actor = renderableManager.getOrCreateActorResourceFor(parameters.getKey());
        Label label = (Label) actor;
        label.setText(parameters.getValue());
    }

    private void renderConversationLine(final SingleConversationLine conversationLine, final UserAction toThrow) {
        AvatarWithMessageComponent component = new AvatarWithMessageComponent(conversationLine.getConversationLine().getText(),
                conversationLine.getRenderableReferenced(), conversationLine.getRelativeAvatarPath(), true);
        component.initActor(themeController.getSkin(), new UserInputHandlerImpl() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                userInputHandler.addUserAction(toThrow);
            }
        });
        component.setZIndex(2);
        additionalActors.add(component);
        stage.addActor(component);
    }

    protected synchronized void resetEngine() {
        bDisposalOngoing = true;
        renderableManager.reset();
        additionalActors = new ArrayList<>();
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
