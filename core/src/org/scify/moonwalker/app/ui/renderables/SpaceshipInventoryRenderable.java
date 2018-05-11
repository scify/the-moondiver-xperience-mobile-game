package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.*;

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

    protected ImageRenderable solarPanel1;
    protected ImageRenderable solarPanel2;
    protected ImageRenderable solarPanel3;
    protected ImageRenderable solarPanel4;
    protected ImageRenderable centralTurbine;
    protected ImageRenderable extraTurbines;
    protected ImageRenderable battery;

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

        if (inventoryItemsCounter > 0)
            centralTurbine = createImageRenderable(CENTRAL_TURBINE_ID, CENTRAL_TURBINE_IMG_PATH, true, true, 14);
        else
            centralTurbine = createImageRenderable(CENTRAL_TURBINE_ID, CENTRAL_TURBINE_IMG_PATH, true, false, 14);
        centralTurbine.setHeight(appInfo.convertY(centralTurbine.getHeight()));
        centralTurbine.setWidth(appInfo.convertX(centralTurbine.getWidth()));
        allRenderables.add(centralTurbine);

        if (inventoryItemsCounter > 1)
            extraTurbines = createImageRenderable(EXTRA_TURBINES_ID, EXTRA_TURBINES_IMG_PATH, true, true, 15);
        else
            extraTurbines = createImageRenderable(EXTRA_TURBINES_ID, EXTRA_TURBINES_IMG_PATH, true, false, 15);
        extraTurbines.setHeight(height);
        extraTurbines.setWidth(width);
        allRenderables.add(extraTurbines);

        if (inventoryItemsCounter > 2)
            solarPanel1 = createImageRenderable(SOLAR_PANELS_1_ID, SOLAR_PANELS_1_IMG_PATH, true, true, 10);
        else
            solarPanel1 = createImageRenderable(SOLAR_PANELS_1_ID, SOLAR_PANELS_1_IMG_PATH, true, false, 10);
        solarPanel1.setHeight(height);
        solarPanel1.setWidth(width);
        allRenderables.add(solarPanel1);

        if (inventoryItemsCounter > 3)
            solarPanel2 = createImageRenderable(SOLAR_PANELS_2_ID, SOLAR_PANELS_2_IMG_PATH, true, true, 11);
        else
            solarPanel2 = createImageRenderable(SOLAR_PANELS_2_ID, SOLAR_PANELS_2_IMG_PATH, true, false, 11);
        solarPanel2.setHeight(height);
        solarPanel2.setWidth(width);
        allRenderables.add(solarPanel2);

        if (inventoryItemsCounter > 4)
            solarPanel3 = createImageRenderable(SOLAR_PANELS_3_ID, SOLAR_PANELS_3_IMG_PATH, true, true, 12);
        else
            solarPanel3 = createImageRenderable(SOLAR_PANELS_3_ID, SOLAR_PANELS_3_IMG_PATH, true, false, 12);
        solarPanel3.setHeight(height);
        solarPanel3.setWidth(width);
        allRenderables.add(solarPanel3);

        if (inventoryItemsCounter > 5)
            solarPanel4 = createImageRenderable(SOLAR_PANELS_4_ID, SOLAR_PANELS_4_IMG_PATH, true, true, 13);
        else
            solarPanel4 = createImageRenderable(SOLAR_PANELS_4_ID, SOLAR_PANELS_4_IMG_PATH, true, false, 13);
        solarPanel4.setHeight(height);
        solarPanel4.setWidth(width);
        allRenderables.add(solarPanel4);

        if (inventoryItemsCounter > 5)
            battery = createImageRenderable(BATTERY_ID, BATTERY_IMG_PATH, true, true, 16);
        else
            battery = createImageRenderable(BATTERY_ID, BATTERY_IMG_PATH, true, false, 16);
        battery.setHeight(height);
        battery.setWidth(width);
        allRenderables.add(battery);
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    protected EffectSequence getShowPartEffectSequence() {
        EffectSequence fadeInEffects = new EffectSequence();
        fadeInEffects.addEffect(new FadeEffect(1.0, 0, 0));
        fadeInEffects.addEffect(new VisibilityEffect(true));
        fadeInEffects.addEffect(new FadeEffect(0.0, 1.0, 2500));
        return fadeInEffects;
    }

    public void addSolarPanel1() {
        solarPanel1.addEffect(getShowPartEffectSequence());
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

}
