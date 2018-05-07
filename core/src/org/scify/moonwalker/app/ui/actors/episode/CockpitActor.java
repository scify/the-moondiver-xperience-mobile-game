package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import org.scify.engine.renderables.effects.MoveEffect;
import org.scify.engine.renderables.effects.RotateEffect;
import org.scify.engine.renderables.effects.SlideEffect;
import org.scify.moonwalker.app.ui.ThemeController;
import org.scify.moonwalker.app.ui.actors.FadingTableActor;
import org.scify.moonwalker.app.ui.actors.ImageWithEffect;
import org.scify.moonwalker.app.ui.actors.StackWithEffect;
import org.scify.moonwalker.app.ui.actors.Updateable;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

public class CockpitActor extends FadingTableActor<CockpitRenderable> implements Updateable {

    protected Table bottomTable;
    protected Button travelButton;
    protected Button travelLightedButton;
    protected Button launchButton;
    protected Button launchLightedButton;
    protected Button inventoryButton;
    protected Button inventoryLightedButton;
    protected Button mapButton;
    protected Button mapLightedButton;
    protected Button contactButton;
    protected Button contactLightedButton;
    protected Button chargeButton;
    protected Button chargeLightedButton;

    /**
     * When adding values to the table, store the created cells
     * into Cell instances, so that they can easily be updated later.
     */
    protected Label motorEfficiencyLabel;
    protected Label remainingEnergyLabel;
    protected Label remainingDestinationLabel;
    protected Label daysLeftLabel;
    protected StackWithEffect<Label> currentLocationLabel;

    public CockpitActor(Skin skin, CockpitRenderable renderable) {
        super(skin, renderable);
        timestamp = this.renderable.getRenderableLastUpdated();
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
        init();
    }

    protected void init() {
        float screenHeight = getHeight();
        float screenWidth = getWidth();
        top();


        //Top Left Pad Location
        drawTopLeftPad();
        //Mid empty cell
        add().height(0.57f * screenHeight).expandX();
        //Top Right Pad DaysToGo
        drawTopRightPad();

        row();

        //Central
        drawCentral(screenWidth, screenHeight);

        row();

        //bottom
        drawBottom(screenWidth, screenHeight);
        //debugAll();
    }

    protected void drawTopLeftPad() {
        Stack stack = new Stack();
        stack.setTransform(true);

        Image image = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getLeftTablet());
        float width = convertWidth(image.getWidth());
        float height = convertHeight(image.getHeight());
        stack.add(image);

        Table textTable = new Table();
        textTable.top();
        textTable.add().width(width).height(height * 0.2f).colspan(3);
        textTable.row();
        textTable.add().width(0.27f * width);
        currentLocationLabel = (StackWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getLocationLabel());
        Label.LabelStyle ls = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(20, "controls");
        ls.font = themeController.getFont();
        ls.fontColor = Color.valueOf("2f312c");
        Label label = currentLocationLabel.getBasicComponent();
        label.setStyle(ls);
        label.setAlignment(Align.center);
        textTable.add(currentLocationLabel).expandX();
        textTable.add().width(0.1f * width);

        stack.add(textTable);

        stack.rotateBy(4);
        add(stack).top().left().maxWidth(width).maxHeight(height);
    }

    protected void drawTopRightPad() {
        Stack stack = new Stack();

        Image image = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getRightTablet());
        float width = convertWidth(image.getWidth());
        float height = convertHeight(image.getHeight());
        stack.add(image);

        Table textTable = new Table();
        textTable.top();
        textTable.add().width(width).height(height * 0.5f).colspan(3);
        textTable.row();
        textTable.add().width(0.1f * width);
        daysLeftLabel = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getDaysLeftLabel());
        daysLeftLabel.setAlignment(Align.center);
        Label.LabelStyle ls = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(25, "controls");
        ls.font = themeController.getFont();
        ls.fontColor = Color.valueOf("912d25");
        daysLeftLabel.setStyle(ls);
        textTable.add(daysLeftLabel).expandX();
        textTable.add().width(0.2f * width);
        stack.add(textTable);
        add(stack).top().right().width(width).height(height);
    }

    protected void drawCentral(float screenWidth, float screenHeight) {

        Table table = new Table();

        table.add().width(0.03f * screenWidth);

        Stack travelButtons = new Stack();
        travelButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getTravelButton());
        travelButtons.add(travelButton);
        travelLightedButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getTravelLightedButton());
        travelButtons.add(travelLightedButton);
        table.add(travelButtons).width(convertWidth(travelButton.getWidth())).height(convertHeight(travelButton.getHeight())).bottom();

        table.add().width(0.065f * screenWidth);

        Table innerTable = new Table();
        innerTable.defaults();
        innerTable.top();


        Stack contactButtons = new Stack();
        contactButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getContactButton());
        contactButtons.add(contactButton);
        contactLightedButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getContactLightedButton());
        contactButtons.add(contactLightedButton);
        innerTable.add(contactButtons).width(convertWidth(contactButton.getWidth())).height(convertHeight(contactButton.getHeight()));

        innerTable.add().width(0.01f * screenWidth);

        Stack inventoryButtons = new Stack();
        inventoryButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getSpaceshipInventoryButton());
        inventoryButtons.add(inventoryButton);
        inventoryLightedButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getSpaceshipInventoryLightedButton());
        inventoryButtons.add(inventoryLightedButton);
        innerTable.add(inventoryButtons).width(convertWidth(inventoryButton.getWidth())).height(convertHeight(inventoryButton.getHeight()));

        innerTable.row();
        float distanceBetweenRows = convertHeight(contactButton.getHeight() * 0.8f);
        innerTable.add().height(distanceBetweenRows);
        innerTable.row();

        Stack chargeButtons = new Stack();
        chargeButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getChargeButton());
        chargeButtons.add(chargeButton);
        chargeLightedButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getChargeLighedButton());
        chargeButtons.add(chargeLightedButton);
        innerTable.add(chargeButtons).width(convertWidth(chargeButton.getWidth())).height(convertHeight(chargeButton.getHeight()));

        innerTable.add().width(0.01f * screenWidth);

        Stack mapButtons = new Stack();
        mapButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getMapButton());
        mapButtons.add(mapButton);
        mapLightedButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getMapLightedButton());
        mapButtons.add(mapLightedButton);
        innerTable.add(mapButtons).width(convertWidth(mapButton.getWidth())).height(convertHeight(mapButton.getHeight()));

        table.add(innerTable).height(0.24f * screenHeight).expandX().left();

        table.add().width(0.1f * screenWidth);

        Stack launchButtons = new Stack();
        launchButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getLaunchButton());
        launchButtons.add(launchButton);
        launchLightedButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getLaunchLightedButton());
        launchButtons.add(launchLightedButton);
        table.add(launchButtons).width(convertWidth(launchButton.getWidth())).height(convertHeight(launchButton.getHeight())).bottom();

        table.add().width(0.05f * screenWidth);

        add(table).width(screenWidth).height(0.24f * screenHeight).colspan(3);

    }

    protected void drawBottom(float screenWidth, float screenHeight) {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(16, "controls");
        labelStyle.font = themeController.getFont();
        labelStyle.fontColor = Color.valueOf("436272");

        bottomTable = new Table();

        bottomTable.add().height(screenHeight * 0.167f).width(0.26f * screenWidth);

        motorEfficiencyLabel = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getMotorEfficiencyLabel());
        motorEfficiencyLabel.setStyle(labelStyle);
        motorEfficiencyLabel.setAlignment(Align.center);

        bottomTable.add(motorEfficiencyLabel).width(0.06f * screenWidth);

        bottomTable.add().width(0.265f * screenWidth);


        remainingEnergyLabel = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getEnergyLabel());
        remainingEnergyLabel.setStyle(labelStyle);
        remainingEnergyLabel.setAlignment(Align.center);
        bottomTable.add(remainingEnergyLabel).width(0.06f * screenWidth);

        bottomTable.add().width(0.27f * screenWidth);

        remainingDestinationLabel = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getDistanceLabel());
        remainingDestinationLabel.setStyle(labelStyle);
        remainingDestinationLabel.setAlignment(Align.center);
        bottomTable.add(remainingDestinationLabel).width(0.06f * screenWidth);
        bottomTable.add().expandX();
        add(bottomTable).colspan(3).left();
    }


    @Override
    public void update(Renderable renderable) {
        this.renderable = (CockpitRenderable) renderable;
        TextLabelRenderable location = this.renderable.getLocationLabel();
        if (location.needsUpdate()) {
            currentLocationLabel.getBasicComponent().setText(location.getLabel());
            location.wasUpdated();
        }
        renderable.wasUpdated();


        /*setMotorEfficiency(this.renderable.getMotorEfficiencyValue());
        setRemainingEnergy(this.renderable.getRemainingEnergyValue());
        setRemainingDestination(this.renderable.getDestinationDistanceValue() + "");
        setLocation(this.renderable.getPositionValue());
        setDaysLeft(this.renderable.getDaysLeftValue());*/

        /*if (this.renderable.isContactButtonLighted()) {
            contactLightedButton.setVisible(true);
            contactButton.setVisible(false);
        }else {
            contactLightedButton.setVisible(false);
            contactButton.setVisible(true);
        }*/
    }
}
