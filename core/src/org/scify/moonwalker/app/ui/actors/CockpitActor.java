package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;

public class CockpitActor extends Table {
    protected ResourceLocator resourceLocator;
    protected GameInfo gameInfo;
    protected Image background;
    protected final float TABLES_PADDING_PIXELS = 10;

    /**
     * When adding values to the table, store the created cells
     * into Cell instances, so that they can easily be updated later.
     */
    protected Cell energyEfficiencyValueCell;
    protected Cell remainingEnergyValueCell;
    protected Cell remainingDestinationValueCell;
    protected Cell positionValueCell;
    protected Cell daysLeftCell;

    public CockpitActor(Skin skin, float width, float height) {
        super(skin);
        gameInfo = GameInfo.getInstance();
        resourceLocator = new ResourceLocator();
        setWidth(width);
        setHeight(height);
        //debug();
    }

    public void addBackground(String imgPath) {
        background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath(imgPath)))));
        background.setSize(getWidth(), getHeight());
        addActor(background);
    }

    public void addInfoTable(String energyEfficiencyLabel, String energyEfficiencyValue, String energyLabel, String remainingEnergyValue, String destinationLabel, String remainingDestinationValue) {
        Table infoTable = new Table(getSkin());

        initSubTable(infoTable);
        addLabelCell(infoTable, energyEfficiencyLabel);
        energyEfficiencyValueCell = addValueCell(infoTable, energyEfficiencyValue);
        infoTable.row();
        addLabelCell(infoTable, energyLabel);
        remainingEnergyValueCell = addValueCell(infoTable, remainingEnergyValue);
        infoTable.row();
        addLabelCell(infoTable, destinationLabel);
        remainingDestinationValueCell = addValueCell(infoTable, remainingDestinationValue);

        add(infoTable).bottom().expand();
    }

    public void addMiddleTable(Button navigationBtn, String positionLabel, String positionValue) {
        Table middleTable = new Table(getSkin());
        initSubTable(middleTable);
        
        middleTable.add(navigationBtn).width(navigationBtn.getWidth()).height(navigationBtn.getHeight()).colspan(2).center();
        middleTable.row();
        addLabelCell(middleTable, positionLabel);
        positionValueCell = addValueCell(middleTable, positionValue);
        add(middleTable).expand().bottom();
    }

    public void addDaysAndActionsTable(String daysLeftLabel, String daysLeftValue, Button vesselButton, Button mapBtn, Button contactBtn) {

        Table actionsTable = new Table(getSkin());
        initSubTable(actionsTable);

        addLabelCell(actionsTable, daysLeftLabel).top().expand().left();
        daysLeftCell = actionsTable.add(new Label(daysLeftValue, getSkin())).top().expand().left();

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
