package org.scify.moonwalker.app.ui.components.calculator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;

import java.util.Arrays;

public class CalculatorComponent extends Table {

    protected final int BUTTONS_PER_ROW = 3;
    protected final float BUTTON_PADDING_PIXELS = 10;
    protected final float BUTTON_SIZE_PIXELS = 50;
    protected final float MAIN_LABEL_PADDING_PIXELS = 5;
    protected float MAIN_LABEL_HEIGHT;
    protected final String MAIN_LABEL_INITIAL_TEXT = "0";
    protected Label mainLabel;
    protected String[] operatorIds = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "dot"};
    protected String[] operationIds = {"add", "subtract", "multiply", "divide", "result"};
    protected Skin skin;
    protected ResourceLocator resourceLocator;
    protected GameInfo gameInfo;
    protected Image background;
    protected CalculatorController calculator;

    public CalculatorComponent(Skin skin) {
        super(skin);
        this.skin = skin;
        this.resourceLocator = new ResourceLocator();
        this.gameInfo = GameInfo.getInstance();
        createBackground();

        // Init controller
        calculator = new CalculatorController();
        calculator.resetCalculator();

        // add the opaque background
        addActor(background);
        // Create top label, that will have a 20% height
        MAIN_LABEL_HEIGHT = gameInfo.getScreenHeight() * 0.2f;
        this.add(createMainLabel()).colspan(2).expandX().pad(pixelsWithDensity(MAIN_LABEL_PADDING_PIXELS));
        this.row();

        // Create buttons row
        this.add(createTable(operatorIds)).colspan(1).top().fill().expandY();
        this.add(createTable(operationIds)).colspan(1).top().fill();

        this.setFillParent(true);
        this.debug();
    }


    protected void createBackground() {
        background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath("img/component_background.png")))));
        background.setSize(gameInfo.getScreenWidth(), gameInfo.getScreenHeight());
    }

    protected Label createMainLabel() {
        mainLabel = new Label(MAIN_LABEL_INITIAL_TEXT, skin);
        mainLabel.setHeight(MAIN_LABEL_HEIGHT);
        mainLabel.setFontScale(2);
        mainLabel.setAlignment(Align.center);
        Pixmap labelColor = new Pixmap((int)mainLabel.getWidth(), (int)mainLabel.getHeight(), Pixmap.Format.RGB888);
        labelColor.setColor(Color.BLACK);
        labelColor.fill();
        mainLabel.getStyle().background = new Image(new Texture(labelColor)).getDrawable();

        return mainLabel;
    }

    protected Table createTable(String[] idArray) {
        Table table = new Table(skin);
        addButtonsFromArrayToTable(idArray, table);
        return table;
    }

    protected void addButtonsFromArrayToTable(String[] idArray, Table table) {
        int btnIndex = 1;
        for(String operatorId: idArray) {
            // TODO change to id when images are ready
            Drawable btnImage = new SpriteDrawable(new Sprite(new Texture(resourceLocator.getFilePath("img/calculator/1.png"))));
            final ImageButton button = new ImageButton(btnImage);
            button.setName(operatorId);
            button.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    parseButtonClick(button.getName());
                }
            });
            table.add(button).size(pixelsWithDensity(BUTTON_SIZE_PIXELS)).left().fill().pad(pixelsWithDensity(BUTTON_PADDING_PIXELS));
            if(btnIndex % BUTTONS_PER_ROW == 0)
                table.row();
            btnIndex++;
        }
    }

    protected void parseButtonClick(String buttonValue) {
        // if operator clicked, parse the string to number and append to main Label
        System.out.println(buttonValue);
        if(buttonValue.equals("result")) {
            mainLabel.setText(calculator.parseResult());
            calculator.resetCalculator();
        }
        else {
            if (Arrays.asList(operatorIds).contains(buttonValue)) {
                calculator.addOperator(buttonValue);
            } else if (Arrays.asList(operationIds).contains(buttonValue)) {
                calculator.setOperation(buttonValue);
            }
            updateMainLabel();
        }
    }

    protected void updateMainLabel() {
        String value = calculator.getCurrentΡrepresentation();
        if(value.equals(""))
            value = MAIN_LABEL_INITIAL_TEXT;
        mainLabel.setText(value);
    }

    protected float pixelsWithDensity(float pixels) {
        return gameInfo.pixelsWithDensity(pixels);
    }

}
