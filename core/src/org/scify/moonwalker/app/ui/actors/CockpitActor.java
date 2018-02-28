package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import org.scify.engine.Renderable;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

public class CockpitActor extends TableActor implements Updateable{

    protected CockpitRenderable renderable;

    protected Button navigationButton;
    protected Button vesselButton;
    protected Button mapButton;
    protected Button contactButton;

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
        gameInfo = GameInfo.getInstance();
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
        add(createInfoSubTable()).bottom().width(getWidth() / 2f);
        add(createNavigationSubTable()).bottom().width(getWidth() / 4f);
        add(createDaysAndActionsTable()).bottom().height(getHeight()).width(getWidth() / 4f);
    }

    public Table createInfoSubTable() {
        Table infoTable = new Table(getSkin());

        initSubTable(infoTable);
        addTextCell(infoTable, renderable.ENGINE_EFFICIENCY_LABEL);
        energyEfficiencyValueCell = addTextCell(infoTable, renderable.getEngineEfficiencyValue());
        infoTable.row();
        addTextCell(infoTable, renderable.REMAINING_ENERGY_LABEL);
        remainingEnergyValueCell = addTextCell(infoTable, renderable.getRemainingEnergyValue());
        infoTable.row();
        addTextCell(infoTable, renderable.DESTINATION_DISTANCE_LABEL);
        remainingDestinationValueCell = addTextCell(infoTable, renderable.getDestinationDistanceValue());
        infoTable.debug();
        return infoTable;
    }

    public Table createNavigationSubTable() {
        Table middleTable = new Table(getSkin());
        initSubTable(middleTable);

        middleTable.add(navigationButton).width(navigationButton.getWidth()).height(navigationButton.getHeight()).colspan(2).center();
        middleTable.row();
        addTextCell(middleTable, renderable.POSITION_LABEL);
        positionValueCell = addTextCell(middleTable, renderable.getPositionValue());
        return middleTable;
    }

    public Table createDaysAndActionsTable() {
        Table actionsTable = new Table(getSkin());
        initSubTable(actionsTable);

        addTextCell(actionsTable, renderable.DAYS_LEFT_LABEL).top().expand().left();
        daysLeftCell = addTextCell(actionsTable, renderable.getDaysLeftValue()).top().expand().left();

        actionsTable.row();
        actionsTable.add(vesselButton).bottom().width(vesselButton.getWidth()).height(vesselButton.getHeight()).colspan(2);
        actionsTable.row();
        actionsTable.add(mapButton).bottom().width(mapButton.getWidth()).height(mapButton.getHeight()).colspan(2);
        actionsTable.row();
        actionsTable.add(contactButton).bottom().width(contactButton.getWidth()).height(contactButton.getHeight()).colspan(2);
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
            setEnergyEfficiency(this.renderable.getEngineEfficiencyValue());
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

    public void setNavigationButton(Button navigationButton) {
        this.navigationButton = navigationButton;
    }

    public void setVesselButton(Button vesselButton) {
        this.vesselButton = vesselButton;
    }

    public void setMapButton(Button mapButton) {
        this.mapButton = mapButton;
    }

    public void setContactButton(Button contactButton) {
        this.contactButton = contactButton;
    }
}
