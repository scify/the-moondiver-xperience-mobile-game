package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.scify.engine.Renderable;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.renderables.SpaceshipControllerRenderable;

public class SpaceshipControllerActor extends Table implements Updateable {

    protected ResourceLocator resourceLocator;
    protected GameInfo gameInfo;
    protected Image background;
    protected final float TABLES_PADDING_PIXELS = 10;
    protected final float VALUES_PADDING_PIXELS = 80;
    protected SpaceshipControllerRenderable renderable;
    private long timestamp;
    protected Button calculatorButton;
    protected Button travelButton;
    protected Button chargeButton;

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

    public SpaceshipControllerActor(Skin skin, SpaceshipControllerRenderable renderable) {
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

    public void init() {
        add(createMoonPhasesTable()).top().left();
        add(createInfoAndActionsTable()).bottom().right();
    }

    protected Table createMoonPhasesTable() {
        Table moonPhasesTable = new Table(getSkin());
        initSubTable(moonPhasesTable);
        addTextCell(moonPhasesTable, renderable.CURRENT_MOON_PHASE_LABEL);
        addTextCell(moonPhasesTable, renderable.UNITS_LABEL);
        moonPhasesTable.row();
        addMoonPhaseImgCell(moonPhasesTable, renderable.getCurrentMoonPhaseImgPath());
        moonPhaseValueCell = addTextCell(moonPhasesTable, String.valueOf(renderable.getCurrentMoonPhase()));
        moonPhasesTable.row();
        addTextCell(moonPhasesTable, renderable.NEXT_NIGHTS_LABEL);
        addTextCell(moonPhasesTable, renderable.UNITS_LABEL);
        moonPhasesTable.row();
        addMoonPhaseImgCell(moonPhasesTable, renderable.getNextMoonPhaseImgPath1());
        nextNightValueCell1 = addMoonPhaseTextCell(moonPhasesTable, String.valueOf(renderable.getNextMoonPhase1()));
        moonPhasesTable.row();
        addMoonPhaseImgCell(moonPhasesTable, renderable.getNextMoonPhaseImgPath2());
        nextNightValueCell2 = addMoonPhaseTextCell(moonPhasesTable, String.valueOf(renderable.getNextMoonPhase2()));
        moonPhasesTable.debug();
        return moonPhasesTable;
    }

    protected Cell addMoonPhaseImgCell(Table table, String moonPhaseImgPath) {
        return addImageCell(table, imgUrlToTexture(moonPhaseImgPath)).padLeft(gameInfo.pixelsWithDensity(VALUES_PADDING_PIXELS)).padRight(gameInfo.pixelsWithDensity(VALUES_PADDING_PIXELS));
    }

    protected Cell addMoonPhaseTextCell(Table table, String value) {
        return addTextCell(table, value).padLeft(gameInfo.pixelsWithDensity(VALUES_PADDING_PIXELS)).padRight(gameInfo.pixelsWithDensity(VALUES_PADDING_PIXELS));
    }

    protected Table createInfoAndActionsTable() {
        Table infoAndActionsTable = new Table(getSkin());
        initSubTable(infoAndActionsTable);
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
        addButtonCell(infoAndActionsTable, travelButton).colspan(2);
        infoAndActionsTable.row();
        addButtonCell(infoAndActionsTable, chargeButton).colspan(2);
        infoAndActionsTable.row();

        infoAndActionsTable.debug();
        return infoAndActionsTable;
    }

    public Cell addTextCell(Table table, String labelTxt) {
        Label label = new Label(labelTxt, getSkin());
        return table.add(label).left().uniform();
    }

    public Cell addImageCell(Table table, Texture texture) {
        Image img = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        return table.add(img).left().uniform();
    }

    public Texture imgUrlToTexture(String imgPath) {
        return new Texture(resourceLocator.getFilePath(imgPath));
    }

    protected void initSubTable(Table subTable) {
        subTable.defaults().pad(gameInfo.pixelsWithDensity(TABLES_PADDING_PIXELS));
    }

    protected Cell addButtonCell(Table table, Button button) {
        return table.add(button).bottom().width(gameInfo.pixelsWithDensity(button.getWidth())).height(gameInfo.pixelsWithDensity(button.getHeight()));
    }

    @Override
    public void update(Renderable renderable) {
        if(this.renderable.getRenderableLastUpdated() > timestamp) {
            System.out.println("setting renderable: " + renderable.getRenderableLastUpdated() + " over: " + this.renderable.getRenderableLastUpdated());
            this.renderable = (SpaceshipControllerRenderable) renderable;
            setCellValue(moonPhaseValueCell, this.renderable.getCurrentMoonPhase());
            setCellValue(nextNightValueCell1, this.renderable.getNextMoonPhase1());
            setCellValue(nextNightValueCell2, this.renderable.getNextMoonPhase2());
            setCellValue(motorEfficiencyValueCell, this.renderable.getMotorEfficiency());
            setCellValue(remainingEnergyValueCell, this.renderable.getRemainingEnergy());
            setCellValue(destinationDistanceValueCell, this.renderable.getDestinationDistance());
        }
    }


    public void addBackground(String imgPath) {
        background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath(imgPath)))));
        background.setSize(getWidth(), getHeight());
        addActor(background);
    }

    public void setCalculatorButton(Button calculatorButton) {
        this.calculatorButton = calculatorButton;
    }

    public void setTravelButton(Button travelButton) {
        this.travelButton = travelButton;
    }

    public void setChargeButton(Button chargeButton) {
        this.chargeButton = chargeButton;
    }

    protected void setCellValue(Cell cell, int newValue) {
        updateLabelCell(cell, String.valueOf(newValue));
    }

    protected void updateLabelCell(Cell cell, String newValue) {
        Label label = (Label) cell.getActor();
        label.setText(newValue);
    }
}
