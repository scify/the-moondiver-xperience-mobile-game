package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import org.scify.moonwalker.app.ui.ThemeController;
import org.scify.moonwalker.app.ui.actors.FadingTableActor;
import org.scify.moonwalker.app.ui.actors.ImageWithEffect;
import org.scify.moonwalker.app.ui.renderables.ChargeEpisodeRenderable;

public class ChargeEpisodeActor extends FadingTableActor<ChargeEpisodeRenderable> {

    protected Label.LabelStyle ls;

    public ChargeEpisodeActor(Skin skin, ChargeEpisodeRenderable rRenderable) {
        super(skin, rRenderable);
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
        init();
    }

    protected void init() {
        ls = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(20, "dialog");
        ls.font = themeController.getFont();
        ls.fontColor = Color.valueOf("e8ffff");
        float screenHeight = getHeight();
        float screenWidth = getWidth();
        center();
        createLeftColumn(screenWidth, screenHeight, 0.28f * screenWidth);
        createCentralColumn(screenWidth, screenHeight, 0.44f * screenWidth);
        createRightColumn(screenWidth, screenHeight, 0.28f * screenWidth);
    }

    protected void createLeftColumn(float screenWidth, float screenHeight, float columnWidth) {
        Image image1 = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getMoonPhase1());
        float ratio1 = image1.getHeight() / image1.getWidth();
        float image1Width = 0.10f * screenWidth;
        float image1Height = ratio1 * image1Width;

        Image image2 = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getMoonPhase2());
        float ratio2 = image2.getHeight() / image2.getWidth();
        float image2Width = 0.08f * screenWidth;
        float image2Height = ratio2 * image2Width;

        Image image3 = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getMoonPhase3());
        float ratio3 = image3.getHeight() / image3.getWidth();
        float image3Width = 0.08f * screenWidth;
        float image3Height = ratio3 * image3Width;

        Table table = new Table();
        table.add().width(columnWidth).height(0.14f * screenHeight);
        table.row();

        Table currentPhaseTable = new Table();
        currentPhaseTable.add(image1).width(image1Width).height(image1Height);
        currentPhaseTable.row();
        Label label = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getUnitsLabel1());
        label.setAlignment(Align.center);
        label.setStyle(ls);
        currentPhaseTable.add(label);
        table.add(currentPhaseTable).width(columnWidth).height(0.25f * screenHeight);

        table.row();
        table.add().width(columnWidth).height(0.08f * screenHeight);
        table.row();

        Table nextPhaseTable = new Table();
        nextPhaseTable.add(image2).width(image2Width).height(image2Height);
        nextPhaseTable.row();
        Label label2 = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getUnitsLabel2());
        label2.setAlignment(Align.center);
        label2.setStyle(ls);
        nextPhaseTable.add(label2);
        table.add(nextPhaseTable).width(columnWidth).height(0.23f * screenHeight);

        table.row();
        table.add().width(columnWidth).height(0.01f * screenHeight);
        table.row();

        Table postNextPhaseTable = new Table();
        postNextPhaseTable.add(image3).width(image3Width).height(image3Height);
        postNextPhaseTable.row();
        Label label3 = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getUnitsLabel3());
        label3.setAlignment(Align.center);
        label3.setStyle(ls);
        postNextPhaseTable.add(label3);
        table.add(postNextPhaseTable).width(columnWidth).height(0.23f * screenHeight);

        table.row();

        table.add().width(columnWidth).expandY();

        add(table).width(columnWidth).height(screenHeight);
    }

    protected void createCentralColumn(float screenWidth, float screenHeight, float columnWidth) {
        Button chargeButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getChargeButton());
        float chargeButtonWidth = convertWidth(chargeButton.getWidth());
        float chargeButtonHeight = convertHeight(chargeButton.getHeight());
        float calculatorHeight = screenHeight - (0.14f * screenHeight) - chargeButtonHeight;

        Table table = new Table();
        table.add().width(columnWidth).height(0.07f *screenHeight);
        table.row();
        table.add(createCalculator(0.05f * screenWidth, calculatorHeight)).width(columnWidth).height(calculatorHeight);
        table.row();
        table.add(chargeButton).width(chargeButtonWidth).height(chargeButtonHeight);
        table.row();
        table.add().width(columnWidth).height(0.07f *screenHeight);
        add(table).width(columnWidth).height(screenHeight);
    }

    protected Table createCalculator(float widthOfLeftAndRight, float tableHeight) {
        Table ret = new Table();
        ret.add().width(widthOfLeftAndRight).height(tableHeight);

        Table calculator = new Table();
        Label calculatorResult = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorLabel());
        calculatorResult.setAlignment(Align.right);
        Label.LabelStyle calculatorStyle = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(25,"controls");
        calculatorStyle.font = themeController.getFont();
        calculatorStyle.fontColor = Color.valueOf("113b3e");
        calculatorResult.setStyle(calculatorStyle);
        calculator.add(calculatorResult).height(0.2f * tableHeight).right().colspan(4).padRight(widthOfLeftAndRight);
        calculator.row();
        //row 789/
        Button button7 = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButton7());
        float buttonWidth = convertWidth(button7.getWidth());
        float buttonHeight = convertHeight(button7.getHeight());
        calculator.add(button7).width(buttonWidth).height(buttonHeight);
        Button button8 = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButton8());
        calculator.add(button8).width(buttonWidth).height(buttonHeight);
        Button button9 = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButton9());
        calculator.add(button9).width(buttonWidth).height(buttonHeight);
        Button buttonDiv = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButtonDiv());
        calculator.add(buttonDiv).width(buttonWidth).height(buttonHeight);
        //row 456x
        calculator.row();
        Button button4 = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButton4());
        calculator.add(button4).width(buttonWidth).height(buttonHeight);
        Button button5 = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButton5());
        calculator.add(button5).width(buttonWidth).height(buttonHeight);
        Button button6 = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButton6());
        calculator.add(button6).width(buttonWidth).height(buttonHeight);
        Button buttonMulti = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButtonMulti());
        calculator.add(buttonMulti).width(buttonWidth).height(buttonHeight);
        //row 123-
        calculator.row();
        Button button1 = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButton1());
        calculator.add(button1).width(buttonWidth).height(buttonHeight);
        Button button2 = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButton2());
        calculator.add(button2).width(buttonWidth).height(buttonHeight);
        Button button3 = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButton3());
        calculator.add(button3).width(buttonWidth).height(buttonHeight);
        Button buttonMinus = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButtonMinus());
        calculator.add(buttonMinus).width(buttonWidth).height(buttonHeight);
        //row 0C-+
        calculator.row();
        Button button0 = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButton0());
        calculator.add(button0).width(buttonWidth).height(buttonHeight);
        Button buttonC = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButtonC());
        calculator.add(buttonC).width(buttonWidth).height(buttonHeight);
        Button buttonEquals = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButtonEquals());
        calculator.add(buttonEquals).width(buttonWidth).height(buttonHeight);
        Button buttonPlus = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getCalculatorButtonPlus());
        calculator.add(buttonPlus).width(buttonWidth).height(buttonHeight);


        ret.add(calculator).height(tableHeight).expandX();
        ret.add().width(widthOfLeftAndRight).height(tableHeight);
        return ret;
    }

    protected void createRightColumn(float screenWidth, float screenHeight, float columnWidth) {
        Table table = new Table();

        table.top();
        table.add().width(columnWidth).height(0.02f * screenHeight);
        table.row();

        Button exitButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getExitButton());
        table.add(exitButton).width(convertWidth(exitButton.getWidth())).height(convertHeight(exitButton.getHeight())).right().padRight(0.1f * columnWidth);

        table.row();
        table.add().width(columnWidth).height(0.1f * screenHeight);
        table.row();

        Label labelDistance = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getDistanceFromDestinationLabel());
        labelDistance.setAlignment(Align.center);
        labelDistance.setStyle(ls);
        table.add(labelDistance);

        table.row();
        table.add().width(columnWidth).height(0.24f * screenHeight);
        table.row();

        Label labelDistancePerUnit = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getDistancePerUnitLabel());
        labelDistancePerUnit.setAlignment(Align.center);
        labelDistancePerUnit.setStyle(ls);
        table.add(labelDistancePerUnit);

        table.row();
        table.add().width(columnWidth).height(0.21f * screenHeight);
        table.row();

        Label energyLabel = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getEnergyLabel());
        energyLabel.setAlignment(Align.center);
        energyLabel.setStyle(ls);
        table.add(energyLabel);

        table.row();
        table.add().width(columnWidth).expandY();

        add(table).width(columnWidth).height(screenHeight);
    }
}
