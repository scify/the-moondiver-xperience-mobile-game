package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.RotateEffect;
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
        drawTopLeftPad(screenWidth, screenHeight);
        //Mid empty cell
        add().width(0.55f * screenWidth).height(0.57f * screenHeight);
        //Top Right Pad DaysToGo
        drawTopRightPad(screenWidth, screenHeight);

        //row();

        //Central
        //drawCentral(screenWidth, screenHeight);
        //row();
        //drawBottom(screenWidth, screenHeight);
        debugAll();
    }

    protected void drawTopLeftPad(float screenWidth, float screenHeight) {
        Image image = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getLeftTablet());
        image.setAlign(Align.center);
        renderable.getLeftTablet().addEffect(new RotateEffect(0.0, 4.0, 0));


        currentLocationLabel = (StackWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getLocationLabel());
        Label.LabelStyle ls = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(20, "controls");
        ls.font = themeController.getFont();
        ls.fontColor = Color.valueOf("2f312c");
        Label label = currentLocationLabel.getBasicComponent();
        label.setStyle(ls);
        label.setAlignment(Align.center);
        renderable.getLocationLabel().addEffect(new RotateEffect(0.0, 4.0, 0));

        add(image).top().left().maxWidth(convertWidth(image.getWidth())).maxHeight(convertHeight(image.getHeight()));
    }

    protected void drawTopRightPad(float screenWidth, float screenHeight) {
        Stack stack = new Stack();
        stack.setTransform(true);
        Image image = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getRightTablet());
        stack.add(image);
        daysLeftLabel = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getDaysLeftLabel());
        Label.LabelStyle ls = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(25, "controls");
        ls.font = themeController.getFont();
        ls.fontColor = Color.valueOf("912d25");
        daysLeftLabel.setStyle(ls);
        Container container = new Container(daysLeftLabel);
        container.center();
        container.padRight(0.02f * screenWidth);
        container.padTop(0.1f * screenHeight);

        stack.add(container);
        add(stack).top().right().width(convertWidth(image.getWidth())).height(convertHeight(image.getHeight()));
    }

    protected void drawCentral(float screenWidth, float screenHeight) {
        Stack travelButtons = new Stack();
        travelButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getTravelButton());
        travelButtons.add(travelButton);
        travelLightedButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getTravelLightedButton());
        travelButtons.add(travelLightedButton);
        add(travelButtons).width(convertWidth(travelButton.getWidth())).height(convertHeight(travelButton.getHeight())).align(Align.bottom);

        Table centralTable = new Table();
        centralTable.defaults();
        centralTable.align(Align.top);

        Stack contactButtons = new Stack();
        contactButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getContactButton());
        contactButtons.add(contactButton);
        contactLightedButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getContactLightedButton());
        contactButtons.add(contactLightedButton);
        centralTable.add(contactButtons).width(convertWidth(contactButton.getWidth())).height(convertHeight(contactButton.getHeight())).padRight(0.02f * screenWidth);

        Stack inventoryButtons = new Stack();
        inventoryButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getSpaceshipInventoryButton());
        inventoryButtons.add(inventoryButton);
        inventoryLightedButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getSpaceshipInventoryLightedButton());
        inventoryButtons.add(inventoryLightedButton);
        centralTable.add(inventoryButtons).width(convertWidth(inventoryButton.getWidth())).height(convertHeight(inventoryButton.getHeight()));

        centralTable.row();
        float distanceBetweenRows = convertHeight(contactButton.getHeight()* 0.8f);
        centralTable.add().height(distanceBetweenRows);
        centralTable.row();

        Stack chargeButtons = new Stack();
        chargeButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getChargeButton());
        chargeButtons.add(chargeButton);
        chargeLightedButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getChargeLighedButton());
        chargeButtons.add(chargeLightedButton);
        chargeButtons.add(chargeButtons);
        centralTable.add(chargeButtons).width(convertWidth(chargeButton.getWidth())).height(convertHeight(chargeButton.getHeight())).padRight(0.02f * screenWidth);

        Stack mapButtons = new Stack();
        mapButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getMapButton());
        mapButtons.add(mapButton);
        mapLightedButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getMapLightedButton());
        mapButtons.add(mapLightedButton);
        centralTable.add(mapLightedButton).width(convertWidth(mapButton.getWidth())).height(convertHeight(mapButton.getHeight()));
        add(centralTable).left().height(0.24f * screenHeight).padLeft(0.03f * screenWidth);

        Stack launchButtons = new Stack();
        launchButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getLaunchButton());
        launchButtons.add(launchButton);
        launchLightedButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getLaunchLightedButton());
        launchButtons.add(launchLightedButton);
        add(launchButtons).width(convertWidth(launchButton.getWidth())).height(convertHeight(launchButton.getHeight())).align(Align.bottom).padRight(0.04f * screenWidth);
    }

    protected void drawBottom(float screenWidth, float screenHeight) {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(16, "controls");
        labelStyle.font = themeController.getFont();
        labelStyle.fontColor = Color.valueOf("436272");

        bottomTable = new Table();

        bottomTable.add().height(screenHeight * 0.167f).width(0.28f * screenWidth);

        motorEfficiencyLabel = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getMotorEfficiencyLabel());
        motorEfficiencyLabel.setStyle(labelStyle);
        motorEfficiencyLabel.setAlignment(Align.center);

        bottomTable.add(motorEfficiencyLabel).width(0.06f *screenWidth);

        bottomTable.add().width(0.265f * screenWidth);


        remainingEnergyLabel = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getEnergyLabel());
        remainingEnergyLabel.setStyle(labelStyle);
        remainingEnergyLabel.setAlignment(Align.center);
        bottomTable.add(remainingEnergyLabel).width(0.06f *screenWidth);

        bottomTable.add().width(0.27f * screenWidth);

        remainingDestinationLabel = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getDistanceLabel());
        remainingDestinationLabel.setStyle(labelStyle);
        remainingDestinationLabel.setAlignment(Align.center);
        bottomTable.add(remainingDestinationLabel).width(0.06f *screenWidth);
        add(bottomTable).colspan(3).left();
    }


    @Override
    public void update(Renderable renderable) {
//        System.out.println("setting renderable: " + renderable.getRenderableLastUpdated() + " over: " + this.renderable.getRenderableLastUpdated());

        this.renderable = (CockpitRenderable) renderable;
        this.timestamp = this.renderable.getRenderableLastUpdated();
        currentLocationLabel.getBasicComponent().setText(this.renderable.getLocationLabel().getLabel());
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
