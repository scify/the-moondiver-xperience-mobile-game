package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.scify.engine.*;
import org.scify.engine.audio.AudioEngine;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.conversation.MultipleConversationLines;
import org.scify.engine.conversation.SingleConversationLine;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.game.quiz.Question;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.components.*;
import org.scify.moonwalker.app.ui.input.UserActionCode;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;
import org.scify.moonwalker.app.ui.sound.GdxAudioEngine;

import java.util.*;
import java.util.List;

public class MoonWalkerRenderingEngine implements RenderingEngine<MoonWalkerGameState> {
    /**
     * The rendering engine processes the game events, one at a time.
     * The currently processed {@link GameEvent} may block any UI input.
     */
    private GameEvent currentGameEvent;

    private long lLastUpdate = -1L;
    private Image worldImg;
    private MoonWalkerGameState currentGameState;
    private AudioEngine audioEngine;
    private CameraController cameraController;
    private GameInfo gameInfo;
    private World world;
    private Map<Renderable, Sprite> renderableSpriteMap = new HashMap<>();
    private Map<Renderable, Actor> renderableActorMap = new HashMap<>();
    private ThemeController themeController;
    private UserInputHandlerImpl userInputHandler;
    private Label fpsLabel;
    private SpriteBatch batch;
    private Stage stage;
    private Viewport gameViewport;
    private ResourceLocator resourceLocator;
    private static final String TAG = MoonWalkerRenderingEngine.class.getName();
    private List<Actor> conversationActors;
    private ComponentFactory<Actor> actorFactory;
    private ComponentFactory<Sprite> spriteFactory;

    public MoonWalkerRenderingEngine(UserInputHandler userInputHandler, SpriteBatch batch, Stage stage) {
        this.resourceLocator = new ResourceLocator();
        cameraController = new CameraController();
        this.userInputHandler = (UserInputHandlerImpl) userInputHandler;
        conversationActors = new ArrayList<>();
        audioEngine = new GdxAudioEngine();
        gameInfo = GameInfo.getInstance();
        gameInfo.printInfo();
        this.batch = batch;
        this.stage = stage;
        gameViewport = stage.getViewport();
        themeController = new ThemeController();
        this.actorFactory = new ActorFactory(themeController.getSkin());
        this.spriteFactory = new SpriteFactory(themeController.getSkin());
        cameraController.initCamera(stage);
        createBackgroundDefaultImg();
        initFPSLabel();
        audioEngine.pauseCurrentlyPlayingAudios();
        // TODO music should be added from episode rules
        //audioEngine.playSoundLoop("audio/episode_1/music.wav");
    }

    private void initFPSLabel() {
        fpsLabel = new Label("", themeController.getSkin());
        fpsLabel.setStyle(new Label.LabelStyle(themeController.getFont(), Color.RED));
        fpsLabel.setPosition(20, gameInfo.getScreenHeight() - 20);
        // fps label has thrice the normal font size
        fpsLabel.setFontScale(3);
    }

    protected void createBackgroundDefaultImg() {
        worldImg = new Image(new Texture(resourceLocator.getFilePath("img/theworld.png")));
        worldImg.setWidth(gameInfo.getScreenWidth());
        worldImg.setHeight(gameInfo.getScreenHeight());
    }

    @Override
    public void initializeGameState(MoonWalkerGameState initialState) {
        this.world = initialState.world;
    }

    @Override
    public void setGameState(MoonWalkerGameState gameState) {
        this.currentGameState = gameState;
    }

    protected Sprite getSpriteResourceFor(Renderable toDraw) {
        Sprite resource = null;
        // If I have an existing sprite
        if (renderableSpriteMap.containsKey(toDraw)) {
            // reuse it
            resource = renderableSpriteMap.get(toDraw);
        } else {
            // else
            // createSpriteResourceForType
            Sprite newResourceForRenderable = spriteFactory.createResourceForType(toDraw);
            if (newResourceForRenderable != null) {
                // and map it to the object
                renderableSpriteMap.put(toDraw, newResourceForRenderable);
                resource = newResourceForRenderable;
            }
        }
        return resource;
    }

    protected Actor getActorResourceFor(Renderable toDraw) {
        Actor resource = null;
        if (renderableActorMap.containsKey(toDraw))
            resource = renderableActorMap.get(toDraw);
        else {
            Actor newActorForRenderable = actorFactory.createResourceForType(toDraw);
            if (newActorForRenderable != null) {
                renderableActorMap.put(toDraw, newActorForRenderable);
                resource = newActorForRenderable;
            }
        }
        return resource;
    }

    @Override
    public void drawGameState(MoonWalkerGameState currentState) {
        List<GameEvent> eventsList = currentState.getEventQueue();
        handleGameEvents(eventsList);
        drawRenderables(currentState);
    }

    protected void drawRenderables(MoonWalkerGameState currentState) {
        synchronized (currentState.getRenderableList()) {
            for (Renderable renderable : currentState.getRenderableList()) {
                drawRenderable(renderable);
            }
        }
    }

    protected void drawRenderable(Renderable renderable) {
        Sprite sToDraw = getSpriteResourceFor(renderable);
        if (sToDraw != null) {
            drawSpriteFromRenderable(renderable, sToDraw);
        } else {
            Actor aToDraw = getActorResourceFor(renderable);
            if (aToDraw != null) {
                drawActorFromRenderable(renderable, aToDraw);
            }
        }
    }

    protected void drawSpriteFromRenderable(Renderable renderable, Sprite sToDraw) {
        sToDraw.setPosition(renderable.getxPos(), renderable.getyPos());
        batch.draw(sToDraw, sToDraw.getX() - (sToDraw.getWidth() / 2f), sToDraw.getY() - (sToDraw.getHeight() / 2f), sToDraw.getWidth(), sToDraw.getHeight());
    }

    protected void drawActorFromRenderable(Renderable renderable, Actor aToDraw) {
        aToDraw.setPosition(renderable.getxPos(), renderable.getyPos());
        // if actor does not have a stage, it means that
        // it is the first time that is added to the stage.
        if(aToDraw.getStage() == null) {
            if(renderable.getZIndex() != -1)
                stage.getRoot().addActorAt(renderable.getZIndex(), aToDraw);
            else
                stage.addActor(aToDraw);
            printActors();
        }
    }

    private void handleGameEvents(List<GameEvent> eventsList) {
        // synchronized ensures that iterator.remove will be thread safe
        // on the passed list instance.
        synchronized (eventsList) {
            ListIterator<GameEvent> listIterator = eventsList.listIterator();
            while (listIterator.hasNext()) {
                currentGameEvent = listIterator.next();
                if(new Date().getTime() > currentGameEvent.delay)
                    handleCurrentGameEvent(currentGameEvent, listIterator);
            }
        }
    }

    private void handleCurrentGameEvent(GameEvent gameEvent, ListIterator<GameEvent> listIterator) {
        String eventType = currentGameEvent.type;
        switch (eventType) {
            case "QUESTION_UI":
                try {
                    handleQuestion(gameEvent);
                } catch (UnsupportedOperationException e) {
                    e.printStackTrace();
                }
                listIterator.remove();
                break;
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
            case "DISPOSE_RESOURCES_UI":
                disposeDrawables();
                listIterator.remove();
                break;
            case "RESET_RENDERING_ENGINE":
                reset();
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
                for(Actor actor : conversationActors)
                    actor.remove();
                listIterator.remove();
            default:
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
        conversationActors.add(component);
        stage.addActor(component);
    }

    private void updateLabelText(HashMap.SimpleEntry<Renderable, String> parameters) {
        Actor actor = getActorResourceFor(parameters.getKey());
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
        conversationActors.add(component);
        stage.addActor(component);
    }

    protected void reset() {
        renderableSpriteMap = new HashMap<>();
    }

    @Override
    public void cancelCurrentRendering() {

    }

    @Override
    public void disposeDrawables() {
        for (Map.Entry<Renderable, Sprite> entry : renderableSpriteMap.entrySet()) {
            entry.getValue().getTexture().dispose();
        }
    }

    @Override
    public void initialize() {
        Gdx.input.setInputProcessor(stage);
        stage.addActor(worldImg);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void disposeResources() {
        System.out.println("disposing rendering engine resources...");
        themeController.dispose();
        cameraController.disposeResources();
        audioEngine.disposeResources();
    }

    public void render(Float delta) {
        this.world = currentGameState.world;
        long lNewTime = new Date().getTime();
        if (lNewTime - lLastUpdate < 50L) {// If no less than 1/5 sec has passed
            Thread.yield();
            return; // Do nothing
        } else {
            drawComponents(delta, lNewTime);
            cameraController.render(world);
            cameraController.setProjectionMatrix(batch);
            lLastUpdate = lNewTime;
        }
    }
    
    protected void drawComponents(Float delta, long lNewTime) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //stage.setDebugAll(true);
        stage.act(delta);
        stage.draw();
        synchronized (batch) {
            batch.begin();
            fpsLabel.setText(String.valueOf(1000 / (lNewTime - lLastUpdate)));
            fpsLabel.draw(batch, 1);
            drawGameState(currentGameState);
            batch.end();
            cameraController.update();
        }
    }

    // TODO refactor method into separate class?
    protected void handleQuestion(GameEvent gameEvent) {
        Question question = (Question) gameEvent.parameters;
        switch (question.type) {
            case FREE_TEXT:
                showTextInputDialog(question);
                break;
            default:
                throw new UnsupportedOperationException("Question type " + question.type + " not supported");
        }
    }

    void showTextInputDialog(Question question) {
        TextInputDialog textInputDialog = new TextInputDialog(gameInfo.getScreenWidth() / 2f,
                gameInfo.getScreenHeight() / 2f,
                gameInfo.getScreenWidth() * 0.8f,
                gameInfo.getScreenHeight() * 0.6f,
                question.getTitle(),
                question.getBody(),
                themeController.getSkin());
        textInputDialog.createForQuestion(question, stage);
    }

    private void printActors() {
        for(Actor stageActor : stage.getActors()) {
            System.out.println("Actor " + stageActor.getClass() + " " + stageActor.getZIndex());
        }
    }

}
