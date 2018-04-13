package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.ThemeController;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

public class CockpitActor extends TableActor implements Updateable {

    protected CockpitRenderable renderable;
    protected Table bottomTable;
    protected Button navigateButton;
    protected Button launchButton;
    protected Button spaceshipPartsButton;
    protected Button mapButton;
    protected Button contactButtonSimple;
    protected Button contactButtonLighted;
    protected Button chargeEpisodeButton;


    /**
     * When adding values to the table, store the created cells
     * into Cell instances, so that they can easily be updated later.
     */
    protected Label motorEfficiencyLabel;
    protected Label remainingEnergyLabel;
    protected Label remainingDestinationLabel;
    protected Label daysLeftLabel;
    protected Label currentLocationLabel;

    public CockpitActor(Skin skin, CockpitRenderable renderable) {
        super(skin);
        appInfo = AppInfo.getInstance();
        resourceLocator = new ResourceLocator();
        this.renderable = renderable;
        timestamp = this.renderable.getRenderableLastUpdated();
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getImgPath());
        //debug();
    }

    public void addBackground(String imgPath) {
        background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath(imgPath)))));
        background.setSize(getWidth(), getHeight());
        addActor(background);
    }

    public void init() {
        float screenHeight = getHeight();
        float screenWidth = getWidth();
        top();

        //Top Left Pad Location
        drawTopLeftPad(screenWidth, screenHeight);
        //Mid empty cell
        add().width(0.55f * screenWidth).height(0.57f * screenHeight);
        //Top Right Pad DaysToGo
        drawTopRightPad(screenWidth, screenHeight);

        row();

        //Central
        drawCentral(screenWidth, screenHeight);
        row();
        drawBottom(screenWidth, screenHeight);
    }

    protected void drawTopLeftPad(float screenWidth, float screenHeight) {
        Stack stack = new Stack();
        stack.setTransform(true);
        Texture texture = imgUrlToTexture(renderable.POSITION_LABEL_IMG_PATH);
        Image image = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        image.setAlign(Align.center);
        stack.addActor(image);
        currentLocationLabel = new Label(renderable.getPositionValue(), getSkin());
        Label.LabelStyle ls = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(20, "controls");
        ls.font = themeController.getFont();
        //ls.font.getData().setScale(2, 2);
        ls.fontColor = Color.valueOf("2f312c");
        currentLocationLabel.setStyle(ls);
        Container container = new Container(currentLocationLabel);
        container.center();
        container.padBottom(screenHeight * 0.1f);
        container.padLeft(screenWidth * 0.05f);
        stack.add(container);
        stack.rotateBy(4);
        add(stack).top().left().width(convertWidth(texture.getWidth())).height(convertHeight(texture.getHeight()));
    }

    protected void drawTopRightPad(float screenWidth, float screenHeight) {
        Stack stack = new Stack();
        stack.setTransform(true);
        Texture texture = imgUrlToTexture(renderable.DAYS_LEFT_IMG_PATH);
        Image image = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        stack.add(image);
        daysLeftLabel = new Label(renderable.getDaysLeftValue(), getSkin());
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
        add(stack).top().right().width(convertWidth(texture.getWidth())).height(convertHeight(texture.getHeight()));
    }

    protected void drawCentral(float screenWidth, float screenHeight) {
        add(navigateButton).width(convertWidth(navigateButton.getWidth())).height(convertHeight(navigateButton.getHeight())).align(Align.bottom);

        Table centralTable = new Table();
        centralTable.defaults();
        centralTable.align(Align.top);

        Stack contactButtons = new Stack();
        contactButtons.add(contactButtonSimple);
        contactButtons.add(contactButtonLighted);
        contactButtonLighted.setVisible(false);
        centralTable.add(contactButtons).width(convertWidth(contactButtonSimple.getWidth())).height(convertHeight(contactButtonSimple.getHeight())).padRight(0.02f * screenWidth);
        centralTable.add(spaceshipPartsButton).width(convertWidth(spaceshipPartsButton.getWidth())).height(convertHeight(spaceshipPartsButton.getHeight()));
        centralTable.row();
        float distanceBetweenRows = convertHeight(contactButtonSimple.getHeight()* 0.8f);
        centralTable.add().height(distanceBetweenRows);
        centralTable.row();
        centralTable.add(chargeEpisodeButton).width(convertWidth(chargeEpisodeButton.getWidth())).height(convertHeight(chargeEpisodeButton.getHeight())).padRight(0.02f * screenWidth);
        centralTable.add(mapButton).width(convertWidth(mapButton.getWidth())).height(convertHeight(mapButton.getHeight()));
        add(centralTable).left().height(0.24f * screenHeight).padLeft(0.03f * screenWidth);

        add(launchButton).width(convertWidth(launchButton.getWidth())).height(convertHeight(launchButton.getHeight())).align(Align.bottom).padRight(0.04f * screenWidth);
    }

    protected void drawBottom(float screenWidth, float screenHeight) {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(16, "controls");
        labelStyle.font = themeController.getFont();
        labelStyle.fontColor = Color.valueOf("436272");

        bottomTable = new Table();

        bottomTable.add().height(screenHeight * 0.167f).width(0.28f * screenWidth);



        motorEfficiencyLabel = new Label(renderable.getMotorEfficiencyValue(), getSkin());
        motorEfficiencyLabel.setStyle(labelStyle);
        motorEfficiencyLabel.setAlignment(Align.center);

        bottomTable.add(motorEfficiencyLabel).width(0.06f *screenWidth);

        bottomTable.add().width(0.265f * screenWidth);


        remainingEnergyLabel = new Label(renderable.getRemainingEnergyValue(), getSkin());
        remainingEnergyLabel.setStyle(labelStyle);
        remainingEnergyLabel.setAlignment(Align.center);
        bottomTable.add(remainingEnergyLabel).width(0.06f *screenWidth);

        bottomTable.add().width(0.27f * screenWidth);

        remainingDestinationLabel = new Label(renderable.getDestinationDistanceValue() + "", getSkin());
        remainingDestinationLabel.setStyle(labelStyle);
        remainingDestinationLabel.setAlignment(Align.center);
        bottomTable.add(remainingDestinationLabel).width(0.06f *screenWidth);
        add(bottomTable).colspan(3).left();
    }


    @Override
    public void update(Renderable renderable) {
        if (this.renderable.getRenderableLastUpdated() > timestamp) {
            System.out.println("setting renderable: " + renderable.getRenderableLastUpdated() + " over: " + this.renderable.getRenderableLastUpdated());
            this.renderable = (CockpitRenderable) renderable;
            this.timestamp = this.renderable.getRenderableLastUpdated();
            setMotorEfficiency(this.renderable.getMotorEfficiencyValue());
            setRemainingEnergy(this.renderable.getRemainingEnergyValue());
            setRemainingDestination(this.renderable.getDestinationDistanceValue() + "");
            setLocation(this.renderable.getPositionValue());
            setDaysLeft(this.renderable.getDaysLeftValue());
            if (this.renderable.isContactButtonLighted()) {
                contactButtonLighted.setVisible(true);
                contactButtonSimple.setVisible(false);
            }else {
                contactButtonLighted.setVisible(false);
                contactButtonSimple.setVisible(true);
            }
        }
    }

    protected void setMotorEfficiency(String newValue) {
        //updateInfoValueCell(motorEfficiencyValueCell, newValue);
        motorEfficiencyLabel.setText(newValue);
    }

    protected void setRemainingEnergy(String newValue) {
        //updateInfoValueCell(remainingEnergyValueCell, newValue);
        remainingEnergyLabel.setText(newValue);
    }

    protected void setRemainingDestination(String newValue) {
        //updateInfoValueCell(remainingDestinationValueCell, newValue);
        remainingDestinationLabel.setText(newValue);
    }

    protected void setLocation(String newValue) {
        //updateInfoValueCell(positionValueCell, newValue);
        currentLocationLabel.setText(newValue);
    }


    protected void setDaysLeft(String newValue) {
        //updateInfoValueCell(daysLeftCell, newValue);
        daysLeftLabel.setText(newValue);
    }

    /*protected void updateInfoValueCell(Cell cell, String newValue) {
        // we need to search for the Label type actor in the group we have stored in the cell
        Group group = (Group) cell.getActor();
        for (Actor actor : group.getChildren()) {
            if (actor instanceof Label) {
                Label label = (Label) actor;
                label.setText(newValue);
            }
        }
    }*/

    public void setNavigateButton(Button navigateButton) {
        this.navigateButton = navigateButton;
    }

    public void setLaunchButton(Button launchButton) {
        this.launchButton = launchButton;
    }

    public void setSpaceshipPartsButton(Button spaceshipPartsButton) {
        this.spaceshipPartsButton = spaceshipPartsButton;
    }

    public void setMapButton(Button mapButton) {
        this.mapButton = mapButton;
    }

    public void setContactButtons(Button contactButtonSimple, Button contactButtonLighted) {
        this.contactButtonSimple = contactButtonSimple;
        this.contactButtonLighted = contactButtonLighted;
    }

    public void setChargeEpisodeButton(Button chargeEpisodeButton) {
        this.chargeEpisodeButton = chargeEpisodeButton;
    }
}
