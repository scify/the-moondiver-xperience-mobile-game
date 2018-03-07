package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import org.scify.engine.Renderable;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

public class CockpitActor extends TableActor implements Updateable{

    protected CockpitRenderable renderable;

    protected Button launchButton;
    protected Button spaceshipPartsButton;
    protected Button mapButton;
    protected Button contactButton;
    protected Button chargeEpisodeButton;

    /**
     * When adding values to the table, store the created cells
     * into Cell instances, so that they can easily be updated later.
     */
    protected Cell energyEfficiencyValueCell;
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
        debug();
    }

    public void addBackground(String imgPath) {
        background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath(imgPath)))));
        background.setSize(getWidth(), getHeight());
        addActor(background);
    }

    public void addSubTables() {
        add(createInfoSubTable()).bottom().width(getWidth() * 0.4f);
        add(createNavigationSubTable()).bottom().width(getWidth() * 0.3f);
        add(createDaysAndActionsTable()).bottom().height(getHeight()).width(getWidth() * 0.3f);
    }

    public Table createInfoSubTable() {
        Table infoTable = new Table(getSkin());
        infoTable.setWidth(getWidth() * 0.4f);
        initSubTable(infoTable);
        addImageCell(infoTable, imgUrlToTexture(renderable.MOTOR_EFFICIENCY_IMG_PATH));
        energyEfficiencyValueCell = addTextCell(infoTable, renderable.getMotorEfficiencyValue());
        infoTable.row();
        addImageCell(infoTable, imgUrlToTexture(renderable.REMAINING_ENERGY_IMG_PATH));
        remainingEnergyValueCell = addTextCell(infoTable, renderable.getRemainingEnergyValue());
        infoTable.row();
        addImageCell(infoTable, imgUrlToTexture(renderable.DESTINATION_DISTANCE_IMG_PATH));
        remainingDestinationValueCell = addTextCell(infoTable, renderable.getDestinationDistanceValue());
        infoTable.debug();
        return infoTable;
    }

    public Table createNavigationSubTable() {
        Table middleTable = new Table(getSkin());
        middleTable.setWidth(getWidth() * 0.3f);
        initSubTable(middleTable);

        middleTable.add(launchButton).width(middleTable.getWidth()).left();
        middleTable.row();
        middleTable.add(chargeEpisodeButton).width(middleTable.getWidth()).left();
        middleTable.row();
        addImageCell(middleTable, imgUrlToTexture(renderable.POSITION_LABEL_IMG_PATH)).width(middleTable.getWidth() / 2f);
        positionValueCell = addTextCell(middleTable, renderable.getPositionValue());
        middleTable.debug();
        return middleTable;
    }

    public Table createDaysAndActionsTable() {
        Table actionsTable = new Table(getSkin());
        actionsTable.setHeight(getHeight());
        actionsTable.setWidth(getWidth() * 0.3f);
        System.out.println(getWidth());
        System.out.println(getHeight());
        initSubTable(actionsTable);

        addTextCell(actionsTable, renderable.DAYS_LEFT_LABEL).top().expand().left();
        daysLeftCell = addTextCell(actionsTable, renderable.getDaysLeftValue()).top().expand().left();

        actionsTable.row();
        actionsTable.add(spaceshipPartsButton).bottom().width(actionsTable.getWidth());
        actionsTable.row();
        actionsTable.add(mapButton).bottom().width(actionsTable.getWidth());
        actionsTable.row();
        actionsTable.add(contactButton).bottom().width(actionsTable.getWidth());
        actionsTable.row();
        actionsTable.debug();
        return actionsTable;
    }

    @Override
    public void update(Renderable renderable) {
        if(this.renderable.getRenderableLastUpdated() > timestamp){
        System.out.println("setting renderable: " + renderable.getRenderableLastUpdated() + " over: " + this.renderable.getRenderableLastUpdated());
            this.renderable = (CockpitRenderable) renderable;
            this.timestamp = this.renderable.getRenderableLastUpdated();
            setEnergyEfficiency(this.renderable.getMotorEfficiencyValue());
            setRemainingEnergy(this.renderable.getRemainingEnergyValue());
            setRemainingDestination(this.renderable.getDestinationDistanceValue());
            setPosition(this.renderable.getPositionValue());
            setDaysLeft(this.renderable.getDaysLeftValue());
        }
    }


    protected void setEnergyEfficiency(String newValue) {
        updateLabelCell(energyEfficiencyValueCell, newValue);
    }

    protected void setRemainingEnergy(String newValue) {
        updateLabelCell(remainingEnergyValueCell, newValue);
    }

    protected void setRemainingDestination(String newValue) {
        updateLabelCell(remainingDestinationValueCell, newValue);
    }

    protected void setPosition(String newValue) {
        updateLabelCell(positionValueCell, newValue);
    }

    protected void setDaysLeft(String newValue) {
        updateLabelCell(daysLeftCell, newValue);
    }

    protected void updateLabelCell(Cell cell, String newValue) {
        Label label = (Label) cell.getActor();
        label.setText(newValue);
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
