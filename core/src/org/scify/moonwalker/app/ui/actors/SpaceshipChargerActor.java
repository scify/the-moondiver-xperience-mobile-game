package org.scify.moonwalker.app.ui.actors;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.scify.engine.Renderable;
import org.scify.moonwalker.app.ui.renderables.SpaceshipChargerRenderable;

public class SpaceshipChargerActor extends TableActor implements Updateable {

    protected SpaceshipChargerRenderable renderable;
    protected Button calculatorButton;
    protected Button chargeButton;
    protected Button escapeButton;

    /**
     * When adding values to the table, store the created cells
     * into Cell instances, so that they can easily be updated later.
     */
    protected Cell currentMoonPhaseEnergyCell;
    protected Cell nextMoonPhaseEnergyCell;
    protected Cell postNextMoonPhaseEnergyCell;

    protected Cell currentMoonPhaseImgCell;
    protected Cell nextMoonPhaseImgCell;
    protected Cell postNextMoonPhaseImgCell;

    protected Cell motorEfficiencyValueCell;
    protected Cell remainingEnergyValueCell;
    protected Cell destinationDistanceValueCell;

    public SpaceshipChargerActor(Skin skin, SpaceshipChargerRenderable renderable) {
        super(skin);

        this.renderable = renderable;
        timestamp = this.renderable.getRenderableLastUpdated();
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getImgPath());
        debug();
    }

    public void addSubTables() {
        add(createMoonPhasesTable()).top().left().expand().width(getWidth() / 2f).height(getHeight());
        add(createInfoAndActionsTable()).bottom().right().expand().width(getWidth() / 2f).height(getHeight());
    }

    protected Table createMoonPhasesTable() {
        Table moonPhasesTable = new Table(getSkin());
        initSubTable(moonPhasesTable);
        addTextCell(moonPhasesTable, renderable.CURRENT_MOON_PHASE_LABEL);
        addTextCell(moonPhasesTable, renderable.UNITS_LABEL);
        moonPhasesTable.row();
        currentMoonPhaseImgCell = addMoonPhaseImgCell(moonPhasesTable, renderable.getCurrentMoonPhaseImgPath());
        currentMoonPhaseEnergyCell = addTextCell(moonPhasesTable, String.valueOf(renderable.getCurrentMoonPhaseUnits()));
        moonPhasesTable.row();
        addTextCell(moonPhasesTable, renderable.NEXT_NIGHTS_LABEL);
        addTextCell(moonPhasesTable, renderable.UNITS_LABEL);
        moonPhasesTable.row();
        nextMoonPhaseImgCell = addMoonPhaseImgCell(moonPhasesTable, renderable.getNextMoonPhaseImgPath());
        nextMoonPhaseEnergyCell = addMoonPhaseTextCell(moonPhasesTable, String.valueOf(renderable.getNextMoonPhaseUnits()));
        moonPhasesTable.row();
        postNextMoonPhaseImgCell = addMoonPhaseImgCell(moonPhasesTable, renderable.getPostNextMoonPhaseImgPath());
        postNextMoonPhaseEnergyCell = addMoonPhaseTextCell(moonPhasesTable, String.valueOf(renderable.getPostNextMoonPhaseUnits()));
        moonPhasesTable.debug();
        return moonPhasesTable;
    }

    protected Cell addMoonPhaseImgCell(Table table, String moonPhaseImgPath) {
        return addImageCell(table, imgUrlToTexture(moonPhaseImgPath));
    }

    protected Cell addMoonPhaseTextCell(Table table, String value) {
        return addTextCell(table, value);
    }

    protected Table createInfoAndActionsTable() {
        Table infoAndActionsTable = new Table(getSkin());
        initSubTable(infoAndActionsTable);
        infoAndActionsTable.add(escapeButton).colspan(2).right();
        infoAndActionsTable.row();
        addTextCell(infoAndActionsTable, renderable.MOTOR_EFFICIENCY_LABEL);
        motorEfficiencyValueCell = addTextCell(infoAndActionsTable, String.valueOf(renderable.getMotorEfficiency()));
        infoAndActionsTable.row();
        addTextCell(infoAndActionsTable, renderable.REMAINING_ENERGY_LABEL);
        remainingEnergyValueCell = addTextCell(infoAndActionsTable, String.valueOf(renderable.getRemainingEnergy()));
        infoAndActionsTable.row();
        addTextCell(infoAndActionsTable, renderable.DESTINATION_DISTANCE_LABEL);
        destinationDistanceValueCell = addTextCell(infoAndActionsTable, String.valueOf(renderable.getDestinationDistance()));
        infoAndActionsTable.row();

        addButtonCell(infoAndActionsTable, calculatorButton).colspan(2);
        infoAndActionsTable.row();
        addButtonCell(infoAndActionsTable, chargeButton).colspan(2);
        infoAndActionsTable.row();

        infoAndActionsTable.debug();
        return infoAndActionsTable;
    }

    @Override
    public void update(Renderable renderable) {
        if(this.renderable.getRenderableLastUpdated() > timestamp) {
            System.out.println("setting renderable: " + renderable.getRenderableLastUpdated() + " over: " + this.renderable.getRenderableLastUpdated());
            this.renderable = (SpaceshipChargerRenderable) renderable;
            timestamp = this.renderable.getRenderableLastUpdated();
            setTextCellValue(currentMoonPhaseEnergyCell, this.renderable.getCurrentMoonPhaseUnits());
            setTextCellValue(nextMoonPhaseEnergyCell, this.renderable.getNextMoonPhaseUnits());
            setTextCellValue(postNextMoonPhaseEnergyCell, this.renderable.getPostNextMoonPhaseUnits());
            setTextCellValue(motorEfficiencyValueCell, this.renderable.getMotorEfficiency());
            setTextCellValue(remainingEnergyValueCell, this.renderable.getRemainingEnergy());
            setTextCellValue(destinationDistanceValueCell, this.renderable.getDestinationDistance());
            updateImgCell(currentMoonPhaseImgCell, this.renderable.getCurrentMoonPhaseImgPath());
            updateImgCell(nextMoonPhaseImgCell, this.renderable.getNextMoonPhaseImgPath());
            updateImgCell(postNextMoonPhaseImgCell, this.renderable.getPostNextMoonPhaseImgPath());
        }
    }

    public void setCalculatorButton(Button calculatorButton) {
        this.calculatorButton = calculatorButton;
    }

    public void setChargeButton(Button chargeButton) {
        this.chargeButton = chargeButton;
    }

    public void setEscapeButton(Button escapeButton) {
        this.escapeButton = escapeButton;
    }

    protected void setTextCellValue(Cell cell, int newValue) {
        updateLabelCell(cell, String.valueOf(newValue));
    }
}
