package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import org.scify.engine.renderables.effects.*;
import org.scify.moonwalker.app.game.GameInfo;
import org.scify.moonwalker.app.game.MoonPhase;

import java.util.HashSet;
import java.util.Set;

public class SpaceshipInventoryRenderable extends FadingTableRenderable {
    //renderable image paths
    protected final static String BG_IMG_PATH = "img/episode_spaceship_inventory/bg.png";
    protected static final String SOLAR_PANELS_1_IMG_PATH = "img/episode_spaceship_inventory/solar1.png";
    protected static final String SOLAR_PANELS_2_IMG_PATH = "img/episode_spaceship_inventory/solar2.png";
    protected static final String SOLAR_PANELS_3_IMG_PATH = "img/episode_spaceship_inventory/solar3.png";
    protected static final String SOLAR_PANELS_4_IMG_PATH = "img/episode_spaceship_inventory/solar4.png";
    protected static final String EXTRA_TURBINES_IMG_PATH = "img/episode_spaceship_inventory/extraTurbines.png";
    protected static final String CENTRAL_TURBINE_IMG_PATH = "img/episode_spaceship_inventory/centralTurbine.png";
    protected static final String BATTERY_IMG_PATH = "img/episode_spaceship_inventory/battery.png";

    //renderable ids
    protected static final String SOLAR_PANELS_1_ID = "solar1";
    protected static final String SOLAR_PANELS_2_ID = "solar2";
    protected static final String SOLAR_PANELS_3_ID = "solar3";
    protected static final String SOLAR_PANELS_4_ID = "solar4";
    protected static final String EXTRA_TURBINES_ID = "extraTurbines";
    protected static final String CENTRAL_TURBINE_ID = "centralTurbine";
    protected static final String BATTERY_ID = "battery";
    protected static final String UNITS_PER_NIGHT_ID = "units_per_night";
    protected static final String DISTANCE_PER_UNIT_ID = "distance_per_unit";
    protected static final String MOON_PHASE_ID = "moon_phase";

    protected ImageRenderable solarPanel1;
    protected ImageRenderable solarPanel2;
    protected ImageRenderable solarPanel3;
    protected ImageRenderable solarPanel4;
    protected ImageRenderable centralTurbine;
    protected ImageRenderable extraTurbines;
    protected ImageRenderable battery;
    protected ImageRenderable moonPhase;

    protected TextLabelRenderable unitsLabel;
    protected TextLabelRenderable distancePerUnitLabel;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public SpaceshipInventoryRenderable(float xPos, float yPos, float width, float height, String id, int inventoryItemsCounter) {
        super(xPos, yPos, width, height, ACTOR_EPISODE_SPACESHIP_INVENTORY, id, BG_IMG_PATH);
        initSubRenderables(inventoryItemsCounter);
    }

    private void initSubRenderables(int inventoryItemsCounter) {
        allRenderables = new HashSet<>();
        float height = appInfo.getScreenHeight();
        float width = appInfo.getScreenWidth();
        EffectSequence effect = getShowPartEffectSequence();

        centralTurbine = createImageRenderable(CENTRAL_TURBINE_ID, CENTRAL_TURBINE_IMG_PATH, true, false, 14);
        centralTurbine.setHeight(height);
        centralTurbine.setWidth(width);
        if (inventoryItemsCounter > 0) {
            centralTurbine.addEffect(effect);
        }
        allRenderables.add(centralTurbine);

        extraTurbines = createImageRenderable(EXTRA_TURBINES_ID, EXTRA_TURBINES_IMG_PATH, true, false, 15);
        extraTurbines.setHeight(height);
        extraTurbines.setWidth(width);
        if (inventoryItemsCounter > 1)
            extraTurbines.addEffect(effect);
        allRenderables.add(extraTurbines);

        solarPanel1 = createImageRenderable(SOLAR_PANELS_1_ID, SOLAR_PANELS_1_IMG_PATH, true, false, 10);
        solarPanel1.setHeight(height);
        solarPanel1.setWidth(width);
        if (inventoryItemsCounter > 2)
            solarPanel1.addEffect(effect);
        allRenderables.add(solarPanel1);

        solarPanel2 = createImageRenderable(SOLAR_PANELS_2_ID, SOLAR_PANELS_2_IMG_PATH, true, false, 11);
        solarPanel2.setHeight(height);
        solarPanel2.setWidth(width);
        if (inventoryItemsCounter > 3)
            solarPanel2.addEffect(effect);
        allRenderables.add(solarPanel2);

        solarPanel3 = createImageRenderable(SOLAR_PANELS_3_ID, SOLAR_PANELS_3_IMG_PATH, true, false, 12);
        solarPanel3.setHeight(height);
        solarPanel3.setWidth(width);
        if (inventoryItemsCounter > 4)
            solarPanel3.addEffect(effect);
        allRenderables.add(solarPanel3);

        solarPanel4 = createImageRenderable(SOLAR_PANELS_4_ID, SOLAR_PANELS_4_IMG_PATH, true, false, 13);
        solarPanel4.setHeight(height);
        solarPanel4.setWidth(width);
        if (inventoryItemsCounter > 5)
            solarPanel4.addEffect(effect);
        allRenderables.add(solarPanel4);


        battery = createImageRenderable(BATTERY_ID, BATTERY_IMG_PATH, true, false, 16);
        battery.setHeight(height);
        battery.setWidth(width);
        if (inventoryItemsCounter > 6)
            battery.addEffect(effect);
        allRenderables.add(battery);

        unitsLabel = createTextLabelRenderable(UNITS_PER_NIGHT_ID, "", false, true, 5);
        allRenderables.add(unitsLabel);
        distancePerUnitLabel = createTextLabelRenderable(DISTANCE_PER_UNIT_ID, "", false, true, 5);
        allRenderables.add(distancePerUnitLabel);
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    protected EffectSequence getShowPartEffectSequence() {
        EffectSequence fadeInEffects = new EffectSequence();
        fadeInEffects.addEffect(new FadeEffect(1.0, 0, 0));
        fadeInEffects.addEffect(new VisibilityEffect(true));
        fadeInEffects.addEffect(new FadeEffect(0.0, 1.0, 2000));
        return fadeInEffects;
    }

    public void addSolarPanel1() {
        solarPanel1.addEffect(getShowPartEffectSequence());
        /*gameInfo.set
        EffectSequence labelUpdateEffect = new EffectSequence();
        labelUpdateEffect.addEffect(new FadeEffect(1.0, 0, 1000));
        labelUpdateEffect.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {

            }
        }));
        labelUpdateEffect.addEffect(new FadeEffect(0, 1.0, 1000));
        unitsLabel.addEffect(labelUpdateEffect);*/
    }

    public void addSolarPanel2() {
        solarPanel2.addEffect(getShowPartEffectSequence());
    }

    public void addSolarPanel3() {
        solarPanel3.addEffect(getShowPartEffectSequence());
    }

    public void addSolarPanel4() {
        solarPanel4.addEffect(getShowPartEffectSequence());
    }

    public void addCentralTurbine() {
        centralTurbine.addEffect(getShowPartEffectSequence());
    }

    public void addExtraTurbines() {
        extraTurbines.addEffect(getShowPartEffectSequence());
    }

    public void addBattery() {
        battery.addEffect(getShowPartEffectSequence());
    }

    public void addNextItem(int inventoryItemsCounter) {
        if (inventoryItemsCounter == 1)
            addCentralTurbine();
        else if (inventoryItemsCounter == 2)
            addExtraTurbines();
        else if (inventoryItemsCounter == 3)
            addSolarPanel1();
        else if (inventoryItemsCounter == 4)
            addSolarPanel2();
        else if (inventoryItemsCounter == 5)
            addSolarPanel3();
        else if (inventoryItemsCounter == 6)
            addSolarPanel4();
        else if (inventoryItemsCounter == 7)
            addBattery();

    }

    public void setUnitsValue(String units) {
        unitsLabel.setLabel(units);
    }

    public void setDistancePerUnitValue(String distancePerUnit) {
        distancePerUnitLabel.setLabel(distancePerUnit);
    }

    public TextLabelRenderable getUnitsLabel() {

        return unitsLabel;
    }

    public TextLabelRenderable getDistancePerUnitLabel() {
        return distancePerUnitLabel;
    }

    public void setMoonPhase(MoonPhase moonPhase) {
        this.moonPhase = createImageRenderable(MOON_PHASE_ID, moonPhase.getImgPath(), false, true, 4);
        allRenderables.add(this.moonPhase);
    }

    public ImageRenderable getMoonPhase() {
        return moonPhase;
    }

}
