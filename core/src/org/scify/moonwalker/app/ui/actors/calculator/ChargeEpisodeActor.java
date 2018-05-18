package org.scify.moonwalker.app.ui.actors.calculator;

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
        //debugAll();
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
        Table table = new Table();
        table.add().width(columnWidth).expandY();
        table.row();
        Button chargeButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getChargeButton());
        table.add(chargeButton).width(convertWidth(chargeButton.getWidth())).height(convertHeight(chargeButton.getHeight()));
        table.row();
        table.add().width(columnWidth).height(0.07f *screenHeight);
        add(table).width(columnWidth).height(screenHeight);
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
