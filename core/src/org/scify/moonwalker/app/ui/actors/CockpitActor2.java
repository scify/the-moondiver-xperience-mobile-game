package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.Renderable;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.ThemeController;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

import javax.xml.soap.Text;

public class CockpitActor2 extends TableActor implements Updateable {

    protected CockpitRenderable renderable;
    protected Table bottomTable;
    protected Button navigateButton;
    protected Button launchButton;
    protected Button spaceshipPartsButton;
    protected Button mapButton;
    protected Button contactButton;
    protected Button chargeEpisodeButton;


    /**
     * When adding values to the table, store the created cells
     * into Cell instances, so that they can easily be updated later.
     */
    protected Cell motorEfficiencyValueCell;
    protected Cell remainingEnergyValueCell;
    protected Cell remainingDestinationValueCell;
    protected Cell positionValueCell;
    protected Cell daysLeftCell;
    protected Cell currentLocationCell;

    public CockpitActor2(Skin skin, CockpitRenderable renderable) {
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
        currentLocationCell = drawTopLeftPad(screenWidth, screenHeight);
        //Mid empty cell
        add().width(0.55f * screenWidth).height(0.57f * screenHeight);
        //Top Right Pad DaysToGo
        remainingDestinationValueCell = drawTopRightPad(screenWidth, screenHeight);

        row();

        //Central
        drawCentral(screenWidth, screenHeight);
        row();
        drawBottom(screenWidth, screenHeight);
    }

    protected Cell drawTopLeftPad(float screenWidth, float screenHeight) {
        Stack stack = new Stack();
        stack.setTransform(true);
        Texture texture = imgUrlToTexture(renderable.POSITION_LABEL_IMG_PATH);
        Image image = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        image.setAlign(Align.center);
        stack.addActor(image);
        //Label label = new Label(renderable.getPositionValue(), getSkin());
        //Label label = new Label("ΛΟΝΔΙΝΟ", getSkin());
        Label label = new Label(renderable.getPositionValue(), getSkin());
        Label.LabelStyle ls = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(20, "controls");
        ls.font = themeController.getFont();
        //ls.font.getData().setScale(2, 2);
        ls.fontColor = Color.valueOf("2f312c");
        label.setStyle(ls);
        Container container = new Container(label);
        container.center();
        container.padBottom(screenHeight * 0.1f);
        container.padLeft(screenWidth * 0.05f);
        stack.add(container);
        stack.rotateBy(4);
        return add(stack).top().left().width(convertWidth(texture.getWidth())).height(convertHeight(texture.getHeight()));
    }

    protected Cell drawTopRightPad(float screenWidth, float screenHeight) {
        Stack stack = new Stack();
        stack.setTransform(true);
        Texture texture = imgUrlToTexture(renderable.DAYS_LEFT_IMG_PATH);
        Image image = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        stack.add(image);
        Label label = new Label(renderable.getDaysLeftValue(), getSkin());
        Label.LabelStyle ls = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(25, "controls");
        ls.font = themeController.getFont();
        ls.fontColor = Color.valueOf("912d25");
        label.setStyle(ls);
        Container container = new Container(label);
        container.center();
        container.padRight(0.02f * screenWidth);
        container.padTop(0.1f * screenHeight);

        stack.add(container);
        Cell ret = add(stack).top().right().width(convertWidth(texture.getWidth())).height(convertHeight(texture.getHeight()));
        return ret;
    }

    protected void drawCentral(float screenWidth, float screenHeight) {
        add(navigateButton).width(convertWidth(navigateButton.getWidth())).height(convertHeight(navigateButton.getHeight())).align(Align.bottom);

        Table centralTable = new Table();
        centralTable.defaults();
        centralTable.align(Align.top);
        centralTable.add(contactButton).width(convertWidth(contactButton.getWidth())).height(convertHeight(contactButton.getHeight())).padRight(0.02f * screenWidth);
        centralTable.add(spaceshipPartsButton).width(convertWidth(spaceshipPartsButton.getWidth())).height(convertHeight(spaceshipPartsButton.getHeight()));
        centralTable.row();
        float distanceBetweenRows = convertHeight(contactButton.getHeight()* 0.8f);
        centralTable.add().height(distanceBetweenRows);
        centralTable.row();
        centralTable.add(chargeEpisodeButton).width(convertWidth(chargeEpisodeButton.getWidth())).height(convertHeight(chargeEpisodeButton.getHeight())).padRight(0.02f * screenWidth);
        centralTable.add(mapButton).width(convertWidth(mapButton.getWidth())).height(convertHeight(mapButton.getHeight()));
        add(centralTable).left().height(0.24f * screenHeight).padLeft(0.03f * screenWidth);

        add(launchButton).width(convertWidth(launchButton.getWidth())).height(convertHeight(launchButton.getHeight())).align(Align.bottom).padRight(0.04f * screenWidth);
    }

    protected void drawBottom(float screenWidth, float screenHeight) {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(18, "controls");
        labelStyle.font = themeController.getFont();
        labelStyle.fontColor = Color.valueOf("436272");

        bottomTable = new Table();

        bottomTable.add().height(screenHeight * 0.167f).width(0.28f * screenWidth);

        //Label labelMotorEfficiency = new Label(renderable.getMotorEfficiencyValue() + "%", getSkin());
        Label labelMotorEfficiency = new Label("100%", getSkin());
        //final TextTooltip textTooltip = new TextTooltip("TEST", getSkin());

        //labelMotorEfficiency.addListener(textTooltip);
        labelMotorEfficiency.setStyle(labelStyle);
        labelMotorEfficiency.setAlignment(Align.center);
        motorEfficiencyValueCell = bottomTable.add(labelMotorEfficiency).width(0.06f *screenWidth);

        bottomTable.add().width(0.265f * screenWidth);

        Label labelEnergy = new Label(renderable.getRemainingEnergyValue(), getSkin());
        labelEnergy.setStyle(labelStyle);
        labelEnergy.setAlignment(Align.center);
        remainingEnergyValueCell = bottomTable.add(labelEnergy).width(0.06f *screenWidth);

        bottomTable.add().width(0.27f * screenWidth);

        Label labelDistance = new Label(renderable.getDestinationDistanceValue() + "", getSkin());
        labelDistance.setStyle(labelStyle);
        labelDistance.setAlignment(Align.center);
        remainingDestinationValueCell = bottomTable.add(labelDistance).width(0.06f *screenWidth);
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
            setPosition(this.renderable.getPositionValue());
            setDaysLeft(this.renderable.getDaysLeftValue());
        }
    }


    protected float convertHeight(float initialHeight) {
        int initialBackgroundHeight = 1080;
        float ret = getHeight() * initialHeight;
        ret = ret / initialBackgroundHeight;
        return ret;
    }

    protected float convertWidth(float initialWidth) {
        int initialBackgroundWidth = 1920;
        float ret = getWidth() * initialWidth;
        ret = ret / initialBackgroundWidth;
        return ret;
    }

    protected void setMotorEfficiency(String newValue) {
        updateInfoValueCell(motorEfficiencyValueCell, newValue);
    }

    protected void setRemainingEnergy(String newValue) {
        updateInfoValueCell(remainingEnergyValueCell, newValue);
    }

    protected void setRemainingDestination(String newValue) {
        updateInfoValueCell(remainingDestinationValueCell, newValue);
    }

    protected void setPosition(String newValue) {
        updateInfoValueCell(positionValueCell, newValue);
    }

    protected void setDaysLeft(String newValue) {
        updateInfoValueCell(daysLeftCell, newValue);
    }

    protected void updateInfoValueCell(Cell cell, String newValue) {
        // we need to search for the Label type actor in the group we have stored in the cell
        Group group = (Group) cell.getActor();
        for (Actor actor : group.getChildren()) {
            if (actor instanceof Label) {
                Label label = (Label) actor;
                label.setText(newValue);
            }
        }
    }

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

    public void setContactButton(Button contactButton) {
        this.contactButton = contactButton;
    }

    public void setChargeEpisodeButton(Button chargeEpisodeButton) {
        this.chargeEpisodeButton = chargeEpisodeButton;
    }
}
