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
        addInfoSubTable();
        debug();
    }

    public void addBackground(String imgPath) {
        background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath(imgPath)))));
        background.setSize(getWidth(), getHeight());
        addActor(background);
    }

    public void addInfoSubTable() {
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
        add(infoTable).bottom().uniform();
    }

    public void addNavigationSubTable(Button navigationBtn) {
        Table middleTable = new Table(getSkin());
        initSubTable(middleTable);

        middleTable.add(navigationBtn).width(navigationBtn.getWidth()).height(navigationBtn.getHeight()).colspan(2).center();
        middleTable.row();
        addTextCell(middleTable, renderable.POSITION_LABEL);
        positionValueCell = addTextCell(middleTable, renderable.getPositionValue());
        add(middleTable).uniform().bottom();
    }

    public void addDaysAndActionsTable(Button vesselButton, Button mapBtn, Button contactBtn) {
        Table actionsTable = new Table(getSkin());
        initSubTable(actionsTable);

        addTextCell(actionsTable, renderable.DAYS_LEFT_LABEL).top().expand().left();
        daysLeftCell = addTextCell(actionsTable, renderable.getDaysLeftValue()).top().expand().left();

        actionsTable.row();
        actionsTable.add(vesselButton).bottom().width(vesselButton.getWidth()).height(vesselButton.getHeight());
        actionsTable.row();
        actionsTable.add(mapBtn).bottom().width(mapBtn.getWidth()).height(mapBtn.getHeight());
        actionsTable.row();
        actionsTable.add(contactBtn).bottom().width(contactBtn.getWidth()).height(contactBtn.getHeight());
        actionsTable.row();
        //actionsTable.debug();
        add(actionsTable).expand().fillY();
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
}
