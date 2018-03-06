package org.scify.moonwalker.app.ui.actors;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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
    protected Cell moonPhaseValueCell;
    protected Cell nextNightValueCell1;
    protected Cell nextNightValueCell2;
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
        addMoonPhaseImgCell(moonPhasesTable, renderable.getCurrentMoonPhaseImgPath());
        moonPhaseValueCell = addTextCell(moonPhasesTable, String.valueOf(renderable.getCurrentMoonPhaseUnits()));
        moonPhasesTable.row();
        addTextCell(moonPhasesTable, renderable.NEXT_NIGHTS_LABEL);
        addTextCell(moonPhasesTable, renderable.UNITS_LABEL);
        moonPhasesTable.row();
        addMoonPhaseImgCell(moonPhasesTable, renderable.getNextMoonPhaseImgPath());
        nextNightValueCell1 = addMoonPhaseTextCell(moonPhasesTable, String.valueOf(renderable.getNextMoonPhaseUnits()));
        moonPhasesTable.row();
        addMoonPhaseImgCell(moonPhasesTable, renderable.getPostNextMoonPhaseImgPath());
        nextNightValueCell2 = addMoonPhaseTextCell(moonPhasesTable, String.valueOf(renderable.getPostNextMoonPhaseUnits()));
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
            setCellValue(moonPhaseValueCell, this.renderable.getCurrentMoonPhaseUnits());
            setCellValue(nextNightValueCell1, this.renderable.getNextMoonPhaseUnits());
            setCellValue(nextNightValueCell2, this.renderable.getPostNextMoonPhaseUnits());
            setCellValue(motorEfficiencyValueCell, this.renderable.getMotorEfficiency());
            setCellValue(remainingEnergyValueCell, this.renderable.getRemainingEnergy());
            setCellValue(destinationDistanceValueCell, this.renderable.getDestinationDistance());
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

    protected void setCellValue(Cell cell, int newValue) {
        updateLabelCell(cell, String.valueOf(newValue));
    }

    protected void updateLabelCell(Cell cell, String newValue) {
        Label label = (Label) cell.getActor();
        label.setText(newValue);
    }
}
