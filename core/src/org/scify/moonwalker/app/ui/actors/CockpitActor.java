package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

public class CockpitActor extends Table {
    protected ResourceLocator resourceLocator;
    protected GameInfo gameInfo;
    protected Image background;
    protected final float TABLES_PADDING_PIXELS = 10;
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
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getImgPath());
        addInfoSubTable();
    }

    public void addBackground(String imgPath) {
        background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath(imgPath)))));
        background.setSize(getWidth(), getHeight());
        addActor(background);
    }

    public void addInfoSubTable() {
        Table infoTable = new Table(getSkin());

        initSubTable(infoTable);
        addLabelCell(infoTable, renderable.ENGINE_EFFICIENCY_LABEL);
        energyEfficiencyValueCell = addValueCell(infoTable, renderable.getEngineEfficiencyValue());
        infoTable.row();
        addLabelCell(infoTable, renderable.REMAINING_ENERGY_LABEL);
        remainingEnergyValueCell = addValueCell(infoTable, renderable.getRemainingEnergyValue());
        infoTable.row();
        addLabelCell(infoTable, renderable.DESTINATION_DISTANCE_LABEL);
        remainingDestinationValueCell = addValueCell(infoTable, renderable.getDestinationDistanceValue());

        add(infoTable).bottom().expand();
    }

    public void addNavigationSubTable(Button navigationBtn) {
        Table middleTable = new Table(getSkin());
        initSubTable(middleTable);
        
        middleTable.add(navigationBtn).width(navigationBtn.getWidth()).height(navigationBtn.getHeight()).colspan(2).center();
        middleTable.row();
        addLabelCell(middleTable, renderable.POSITION_LABEL);
        positionValueCell = addValueCell(middleTable, renderable.getPositionValue());
        add(middleTable).expand().bottom();
    }

    public void addDaysAndActionsTable(Button vesselButton, Button mapBtn, Button contactBtn) {
        Table actionsTable = new Table(getSkin());
        initSubTable(actionsTable);

        addLabelCell(actionsTable, renderable.DAYS_LEFT_LABEL).top().expand().left();
        daysLeftCell = addValueCell(actionsTable, renderable.getDaysLeftValue()).top().expand().left();

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

    protected void initSubTable(Table subTable) {
        subTable.defaults().pad(gameInfo.pixelsWithDensity(TABLES_PADDING_PIXELS));
    }

    public Cell addLabelCell(Table table, String labelTxt) {
        Label label = new Label(labelTxt, getSkin());
        return table.add(label).left();
    }

    public Cell addValueCell(Table table, String value) {
        Label label = new Label(value, getSkin());
        return table.add(label).right();
    }
    
    public void setEnergyEfficiency(String newValue) {
        updateLabelCell(energyEfficiencyValueCell, newValue);
    }

    public void setRemainingEnergy(String newValue) {
        updateLabelCell(remainingEnergyValueCell, newValue);
    }

    public void setRemainingDestination(String newValue) {
        updateLabelCell(remainingDestinationValueCell, newValue);
    }

    public void setPosition(String newValue) {
        updateLabelCell(positionValueCell, newValue);
    }

    public void setDaysLeft(String newValue) {
        updateLabelCell(daysLeftCell, newValue);
    }

    protected void updateLabelCell(Cell cell, String newValue) {
        Label label = (Label) cell.getActor();
        label.setText(newValue);
    }

}
