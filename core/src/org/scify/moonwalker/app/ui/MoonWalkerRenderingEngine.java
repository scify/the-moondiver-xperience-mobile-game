package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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
    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;
    private GameInfo gameInfo;
    private World world;
    private Map<Renderable, Sprite> renderableSpriteMap = new HashMap<>();
    private Map<Renderable, Actor> renderableActorMap = new HashMap<>();
    private Skin skin;
    private BitmapFont font;
    private UserInputHandlerImpl userInputHandler;
    private GameHUD gameHUD;
    private Label fpsLabel;
    private SpriteBatch batch;
    private Stage stage;
    private Viewport gameViewport;
    private Camera mainCamera;
    private ResourceLocator resourceLocator;
    private static final String TAG = MoonWalkerRenderingEngine.class.getName();
    private List<Actor> conversationActors;
    private ComponentFactory<Actor> actorFactory;
    private ComponentFactory<Sprite> spriteFactory;

    public MoonWalkerRenderingEngine(UserInputHandler userInputHandler, SpriteBatch batch, Stage stage) {
        this.resourceLocator = new ResourceLocator();
        this.userInputHandler = (UserInputHandlerImpl) userInputHandler;
        conversationActors = new ArrayList<>();
        audioEngine = new GdxAudioEngine();
        gameInfo = GameInfo.getInstance();
        gameInfo.printInfo();
        this.batch = batch;
        this.stage = stage;
        gameViewport = stage.getViewport();
        initCamera();
        createBackgroundDefaultImg();
        initFontAndSkin();
        this.actorFactory = new ActorFactory(skin);
        this.spriteFactory = new SpriteFactory(skin);
        gameHUD = new GameHUD(skin, font);
        fpsLabel = new Label("", skin);
        fpsLabel.setStyle(new Label.LabelStyle(font, Color.RED));
        fpsLabel.setPosition(20, gameInfo.getScreenHeight() - 20);
        // fps label has thrice the normal font size
        fpsLabel.setFontScale(3);
        audioEngine.pauseCurrentlyPlayingAudios();
        // TODO music should be added from episode rules
        //audioEngine.playSoundLoop("audio/episode_1/music.wav");
    }

    protected void createBackgroundDefaultImg() {
        worldImg = new Image(new Texture(resourceLocator.getFilePath("img/theworld.png")));
        worldImg.setWidth(gameInfo.getScreenWidth());
        worldImg.setHeight(gameInfo.getScreenHeight());
    }

    // TODO Move font methods into separate class
    protected void initFontAndSkin() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(resourceLocator.getFilePath("fonts/Starjedi.ttf")));
        font = createFont(generator, 12);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin = new Skin();
        skin.add("default-font", font, BitmapFont.class);
        skin.addRegions(new TextureAtlas(Gdx.files.internal(resourceLocator.getFilePath("fonts/uiskin.atlas"))));
        skin.load(Gdx.files.internal(resourceLocator.getFilePath("fonts/uiskin.json")));
        generator.dispose();
    }

    private BitmapFont createFont(FreeTypeFontGenerator generator, float fontSize) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float screenDPI = 160.0f * Gdx.graphics.getDensity();
        int pixelSize = (int)(fontSize * screenDPI / 96.0f); // Reference size based on 96 DPI screen
        parameter.size = pixelSize;
        //Gdx.app.log(TAG, "Font size: "+pixelSize+"px");
        return generator.generateFont(parameter);
    }

    @Override
    public void initializeGameState(MoonWalkerGameState initialState) {
        this.world = initialState.world;
    }

    private void initCamera() {
        int width = gameInfo.getScreenWidth();
        int height = gameInfo.getScreenHeight();

        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, width,
                height);
        box2DCamera.position.set(width / 2f, height / 2f, 0);
        mainCamera = stage.getCamera();
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void setGameState(MoonWalkerGameState gameState) {
        this.currentGameState = gameState;
        // TODO remove gamehud class and add table as an independent component
        // so that the rules can add
        // new GameEvent("TABLE") and a set of actors and
        // the table component creates the table and passes
        // it to the rendering engine
        this.gameHUD.setLives(gameState.getPlayer().getLives());
        this.gameHUD.setScore(gameState.getPlayer().getScore());
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
                handleQuestion(gameEvent);
                listIterator.remove();
                break;
            case "PLAYER_SCORE":
                int newScore = (int) gameEvent.parameters;
                gameHUD.setScore(newScore);
                listIterator.remove();
                break;
            case "PLAYER_LIVES":
                int newLives = (int) gameEvent.parameters;
                gameHUD.setLives(newLives);
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
        component.initActor(skin);
        int btnIndex = 1;
        for(final ConversationLine conversationLine : multipleConversationLinesComponent.getConversationLines()) {
            component.addButton(conversationLine.getText(), new UserInputHandlerImpl() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    System.out.println("answer was " + conversationLine.getText() + " with order id " + conversationLine.getId());
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
        component.initActor(skin, new UserInputHandlerImpl() {
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
        font.dispose();
        debugRenderer.dispose();
        audioEngine.disposeResources();
    }

    public void render(Float delta) {
        this.world = currentGameState.world;
        long lNewTime = new Date().getTime();
        if (lNewTime - lLastUpdate < 50L) {// If no less than 1/5 sec has passed
            Thread.yield();
            return; // Do nothing
        } else {
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

                mainCamera.update();
            }
            debugRenderer.render(world, box2DCamera.combined);
            batch.setProjectionMatrix(mainCamera.combined);
            lLastUpdate = lNewTime;
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
                skin);
        textInputDialog.createForQuestion(question, stage);
    }

    private void printActors() {
        for(Actor stageActor : stage.getActors()) {
            System.out.println("Actor " + stageActor.getClass() + " " + stageActor.getZIndex());
        }
    }

}
