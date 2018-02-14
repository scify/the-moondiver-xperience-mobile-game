package org.scify.moonwalker.app.ui.components.calculator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
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

public class CalculatorComponent extends Group {

    protected final int BUTTONS_PER_ROW = 3;
    protected final int BUTTON_PADDING = 5;
    protected final int MAIN_LABEL_HEIGHT = 400;
    protected final int TABLE_PADDING = 20;
    protected final String MAIN_LABEL_INITIAL_TEXT = "0";
    protected Label mainLabel;
    protected String[] operatorIds = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "dot"};
    protected String[] operationIds = {"add", "subtract", "multiply", "divide", "result"};
    protected Table containerTable;
    protected Skin skin;
    protected ResourceLocator resourceLocator;
    protected GameInfo gameInfo;
    protected Image background;
    protected CalculatorController calculator;

    public CalculatorComponent(Skin skin) {
        this.skin = skin;
        this.resourceLocator = new ResourceLocator();
        this.gameInfo = GameInfo.getInstance();
        calculator = new CalculatorController();
        calculator.resetCalculator();
        setHeight(gameInfo.getScreenHeight());
        setWidth(gameInfo.getScreenWidth());
        initContainerTable();
        createBackground();
        createAndAddMainLabel();
        containerTable.row();
        containerTable.add(createTable(operatorIds)).bottom().center().expand();
        containerTable.add(createTable(operationIds)).bottom().center().expand();
        // TODO Add escape button
        addActor(background);
        addActor(containerTable);
        //containerTable.debug();
    }

    protected void initContainerTable() {
        containerTable = new Table(skin);
        containerTable.setHeight(gameInfo.getScreenHeight());
        containerTable.setWidth(gameInfo.getScreenWidth());
        containerTable.defaults().pad(TABLE_PADDING);
        containerTable.setFillParent(true);
        containerTable.center().bottom();
    }

    protected void createBackground() {
        background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath("img/component_background.png")))));
        background.setSize(gameInfo.getScreenWidth(), gameInfo.getScreenHeight());
    }

    protected void createAndAddMainLabel() {
        mainLabel = new Label(MAIN_LABEL_INITIAL_TEXT, skin);
        mainLabel.setHeight(MAIN_LABEL_HEIGHT);
        mainLabel.setFontScale(2);
        mainLabel.setAlignment(Align.center);
        Pixmap labelColor = new Pixmap((int)mainLabel.getWidth(), (int)mainLabel.getHeight(), Pixmap.Format.RGB888);
        labelColor.setColor(Color.BLACK);
        labelColor.fill();
        mainLabel.getStyle().background = new Image(new Texture(labelColor)).getDrawable();
        containerTable.add(mainLabel).colspan(2).fillX();
    }

    protected Table createTable(String[] idArray) {
        Table table = new Table(skin);
        System.out.println("1: " + gameInfo.getScreenHeight());
        System.out.println("2: " + gameInfo.getScreenWidth());
        System.out.println("3: " + containerTable.getHeight());
        System.out.println("4: " + mainLabel.getHeight());
        table.setHeight(containerTable.getHeight() - mainLabel.getHeight() - (TABLE_PADDING * 2));
        addButtonsFromArrayToTable(idArray, table);
        return table;
    }

    protected void addButtonsFromArrayToTable(String[] idArray, Table table) {
        float btnSize = singleButtonHeight(table.getHeight(), idArray);
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
            table.add(button).size(btnSize, btnSize).pad(BUTTON_PADDING);
            if(btnIndex % BUTTONS_PER_ROW == 0)
                table.row();
            btnIndex++;
        }
    }

    protected float singleButtonHeight(float containerHeight, String[] btnContainerArray) {
        return containerHeight / ( (float) btnContainerArray.length / BUTTONS_PER_ROW);
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

}
