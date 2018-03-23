package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.badlogic.gdx.utils.Align;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

public class CockpitActor extends TableActor implements Updateable{

    protected CockpitRenderable renderable;
    protected Table infoAndActionsTable;
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
        // Create 4 rows
        // 1st row:days
        row().height(0.2f * getHeight());
        add(createDaysLeftTable()).right();
        // 2nd row: empty/post-it
        row().expandY();
        Table empty = new Table();

        add(empty);
        // 3rd row: launch
        row().height(0.1f *getHeight());
        add(createLaunchTable());
        // 4th row: info & actions
        row().height(0.4f * getHeight());
        add(createInfoSubTable());
        debug();
    }

    public Table createDaysLeftTable() {
        Table dayLeftTable = new Table(getSkin());
        initSubTable(dayLeftTable);
        dayLeftTable.setWidth(getWidth());
        dayLeftTable.setHeight(getHeight() * 0.2f);
        addImageCell(dayLeftTable,
                imgUrlToTexture(renderable.DAYS_LEFT_IMG_PATH)).width(dayLeftTable.getWidth() * 0.1f - getDefaultCellPadding()*2);

        //dayLeftTable.debug();
        return dayLeftTable;
    }

    public Table createLaunchTable() {
        Table launchTable = new Table(getSkin());
        initSubTable(launchTable);
        launchTable.add(launchButton).fillY().width(0.2f * getWidth());
        return launchTable;
    }

    public Table createInfoSubTable() {
        infoAndActionsTable = new Table(getSkin());
        initSubTable(infoAndActionsTable);
        infoAndActionsTable.setWidth(getWidth());
        infoAndActionsTable.setHeight(getHeight() * 0.4f);

        motorEfficiencyValueCell = addInfoAndActionTableRow(renderable.MOTOR_EFFICIENCY_IMG_PATH, renderable.getMotorEfficiencyValue(), contactButton);
        remainingEnergyValueCell = addInfoAndActionTableRow(renderable.REMAINING_ENERGY_IMG_PATH, renderable.getRemainingEnergyValue(), chargeEpisodeButton);
        remainingDestinationValueCell = addInfoAndActionTableRow(renderable.DESTINATION_DISTANCE_IMG_PATH, renderable.getDestinationDistanceValue() + "", mapButton);
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
        if(this.renderable.getRenderableLastUpdated() > timestamp){
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
        for(Actor actor : group.getChildren()) {
            if(actor instanceof Label) {
                Label label = (Label) actor;
                label.setText(newValue);
            }
        }
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
