package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.Renderable;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

public class CockpitActor2 extends TableActor implements Updateable {

    protected CockpitRenderable renderable;
    protected Table infoAndActionsTable;
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
        Texture texture;
        top();

        //Location
        Table topLeftTable = new Table();
        topLeftTable.defaults();
        topLeftTable.align(Align.left);
        texture = imgUrlToTexture(renderable.POSITION_LABEL_IMG_PATH);
        addImageCell(topLeftTable,texture);
        add(topLeftTable).top().left().width(convertWidth(texture.getWidth())).height(convertHeight(texture.getHeight())).padTop(0.03f * screenHeight).padLeft(0.04f * screenWidth);

        //Mid empty cell
        add().width(0.55f* screenWidth).height(0.56f * screenHeight);
        //DaysToGo
        Table topRightTable = new Table();
        topRightTable.defaults();
        topRightTable.align(Align.right);
        texture = imgUrlToTexture(renderable.DAYS_LEFT_IMG_PATH);
        addImageCell(topRightTable, texture);
        add(topRightTable).top().width(convertWidth(texture.getWidth())).height(convertHeight(texture.getHeight()));
        row();

        //Central
        add(navigateButton).width(convertWidth(navigateButton.getWidth())).height(convertHeight(navigateButton.getHeight())).align(Align.center).padTop(0.1f * screenHeight);

        Table centralTable = new Table();
        centralTable.defaults();
        centralTable.align(Align.top);
        centralTable.add(contactButton).width(convertWidth(contactButton.getWidth())).height(convertHeight(contactButton.getHeight())).padRight(0.02f*screenWidth);
        centralTable.add(spaceshipPartsButton).width(convertWidth(spaceshipPartsButton.getWidth())).height(convertHeight(spaceshipPartsButton.getHeight()));
        centralTable.row();
        float distanceBetweenRows = convertHeight(contactButton.getHeight());
        centralTable.add().height(distanceBetweenRows);
        centralTable.row();
        centralTable.add(chargeEpisodeButton).width(convertWidth(chargeEpisodeButton.getWidth())).height(convertHeight(chargeEpisodeButton.getHeight())).padRight(0.02f*screenWidth);
        centralTable.add(mapButton).width(convertWidth(mapButton.getWidth())).height(convertHeight(mapButton.getHeight()));
        add(centralTable).height(0.3f * screenHeight);

        add(launchButton).width(convertWidth(launchButton.getWidth())).height(convertHeight(launchButton.getHeight())).align(Align.center).padTop(0.08f * screenHeight).padRight(0.04f * screenWidth);

        debug();
        // 2nd row: empty/post-it
        //row().expandY();
        //Table empty = new Table();

        //add(empty);
        // 3rd row: launch
        /*row().height(0.1f *getHeight());
        add(createLaunchTable());*/
        // 4th row: info & actions
        /*row().height(0.4f * getHeight());
        add(createInfoSubTable());*/
        //debug();
    }

    public Table createDaysLeftTable() {
        Table dayLeftTable = new Table(getSkin());
        initSubTable(dayLeftTable);
        dayLeftTable.setWidth(getWidth());
        dayLeftTable.setHeight(getHeight() * 0.2f);
        addImageCell(dayLeftTable, imgUrlToTexture(renderable.DAYS_LEFT_IMG_PATH)).width(dayLeftTable.getWidth() * 0.1f - getDefaultCellPadding() * 2);
        //dayLeftTable.debug();
        return dayLeftTable;
    }

    public Table createLaunchTable() {
        Table launchTable = new Table(getSkin());
        initSubTable(launchTable);
        launchTable.add(navigateButton).fillY().width(0.2f * getWidth());
        return launchTable;
    }

    public Table createInfoSubTable() {
        infoAndActionsTable = new Table(getSkin());
        initSubTable(infoAndActionsTable);
        infoAndActionsTable.setWidth(getWidth());
        infoAndActionsTable.setHeight(getHeight() * 0.4f);

        motorEfficiencyValueCell = addInfoAndActionTableRow(renderable.MOTOR_EFFICIENCY_IMG_PATH, renderable.getMotorEfficiencyValue(), contactButton);
        remainingEnergyValueCell = addInfoAndActionTableRow(renderable.REMAINING_ENERGY_IMG_PATH, renderable.getRemainingEnergyValue(), chargeEpisodeButton);
        remainingDestinationValueCell = addInfoAndActionTableRow(renderable.DESTINATION_DISTANCE_IMG_PATH, renderable.getDestinationDistanceValue(), mapButton);
        positionValueCell = addInfoAndActionTableRow(renderable.POSITION_LABEL_IMG_PATH, renderable.getPositionValue(), spaceshipPartsButton);

        //infoAndActionsTable.debug();
        return infoAndActionsTable;
    }

    protected Cell addInfoAndActionTableRow(String imgPath, String valueData, Button button) {
        Cell imageCell = addImageCell(infoAndActionsTable, imgUrlToTexture(imgPath));
        setCellDimensions(imageCell, infoAndActionsTable, 0.5f, 4);
        //Cell valueCell = addTextCell(infoAndActionsTable, valueData);

        Group group = new Group();
        Image cellBagkckground = new Image(new TextureRegionDrawable(new TextureRegion(imgUrlToTexture("img/cockpit/cell_background.png"))));
        cellBagkckground.setWidth(infoAndActionsTable.getWidth() * 0.1f - getDefaultCellPadding() * 2);
        cellBagkckground.setHeight((infoAndActionsTable.getHeight() / 4) - getDefaultCellPadding() * 2);
        group.addActor(cellBagkckground);
        Label valueLabel = new Label(valueData, getSkin());
        valueLabel.setAlignment(Align.center);
        //valueLabel.setDebug(true);
        valueLabel.setWidth(infoAndActionsTable.getWidth() * 0.1f - getDefaultCellPadding() * 2);
        valueLabel.setHeight((infoAndActionsTable.getHeight() / 4) - getDefaultCellPadding() * 2);
        //group.setDebug(true);
        group.addActor(valueLabel);
        Cell valueCell = infoAndActionsTable.add(group);

        setCellDimensions(valueCell, infoAndActionsTable, 0.1f, 4);
        Cell buttonCell = infoAndActionsTable.add(button);
        setCellDimensions(buttonCell, infoAndActionsTable, 0.4f, 4);

        infoAndActionsTable.row().fillY();
        return valueCell;
    }


    @Override
    public void update(Renderable renderable) {
        if (this.renderable.getRenderableLastUpdated() > timestamp) {
            System.out.println("setting renderable: " + renderable.getRenderableLastUpdated() + " over: " + this.renderable.getRenderableLastUpdated());
            this.renderable = (CockpitRenderable) renderable;
            this.timestamp = this.renderable.getRenderableLastUpdated();
            setMotorEfficiency(this.renderable.getMotorEfficiencyValue());
            setRemainingEnergy(this.renderable.getRemainingEnergyValue());
            setRemainingDestination(this.renderable.getDestinationDistanceValue());
            setPosition(this.renderable.getPositionValue());
            setDaysLeft(this.renderable.getDaysLeftValue());
        }
    }

    protected float convertHeight (float initialHeight) {
        int initialBackgroundHeight  = 1080;
        float ret = getHeight() * initialHeight;
        ret = ret / initialBackgroundHeight;
        return ret;
    }

    protected float convertWidth (float initialWidth) {
        int initialBackgroundWidth  = 1920;
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
