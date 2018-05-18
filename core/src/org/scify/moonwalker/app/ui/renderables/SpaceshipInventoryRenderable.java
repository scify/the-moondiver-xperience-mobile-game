package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.GameEvent;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
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
    protected static final String BG_IMG_PATH = "img/episode_spaceship_inventory/bg.png";
    protected static final String SOLAR_PANELS_1_IMG_PATH = "img/episode_spaceship_inventory/solar1.png";
    protected static final String SOLAR_PANELS_2_IMG_PATH = "img/episode_spaceship_inventory/solar2.png";
    protected static final String SOLAR_PANELS_3_IMG_PATH = "img/episode_spaceship_inventory/solar3.png";
    protected static final String SOLAR_PANELS_4_IMG_PATH = "img/episode_spaceship_inventory/solar4.png";
    protected static final String EXTRA_TURBINES_IMG_PATH = "img/episode_spaceship_inventory/extraTurbines.png";
    protected static final String CENTRAL_TURBINE_IMG_PATH = "img/episode_spaceship_inventory/centralTurbine.png";
    protected static final String BATTERY_IMG_PATH = "img/episode_spaceship_inventory/battery.png";
    protected static final String EXIT_BUTTON_IMG_PATH = "img/episode_spaceship_inventory/exit.png";

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
    protected static final String EXIT_BUTTON_ID = "exit";

    //audio
    public final static String ADD_ITEM_AUDIO_PATH = "audio/episode_spaceship_inventory/addSpaceshipItemToInventory.mp3";
    public static final String UPGRADE_STATS_AUDIO_PATH = "audio/episode_spaceship_inventory/increaseStat.mp3";

    protected ImageRenderable solarPanel1;
    protected ImageRenderable solarPanel2;
    protected ImageRenderable solarPanel3;
    protected ImageRenderable solarPanel4;
    protected ImageRenderable centralTurbine;
    protected ImageRenderable extraTurbines;
    protected ImageRenderable battery;
    protected ImageRenderable moonPhase;

    protected ActionButtonRenderable exitButton;

    protected TextLabelRenderable unitsLabel;
    protected String nextUnitsValue;
    protected TextLabelRenderable distancePerUnitLabel;
    protected String nextDistancePerUnitValue;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public SpaceshipInventoryRenderable(float xPos, float yPos, float width, float height, String id, int inventoryItemsCounter) {
        super(xPos, yPos, width, height, ACTOR_EPISODE_SPACESHIP_INVENTORY, id, BG_IMG_PATH);
        initSubRenderables(inventoryItemsCounter);
        nextUnitsValue = null;
        nextDistancePerUnitValue = null;
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

        exitButton = createImageButton(EXIT_BUTTON_ID, EXIT_BUTTON_IMG_PATH, new UserAction(UserActionCode.QUIT),true, false, 100);
        exitButton.setHeight(appInfo.convertY(157));
        exitButton.setWidth(appInfo.convertX(157));
        exitButton.setxPos(0.88f * width);
        exitButton.setyPos(0.83f * height);

        allRenderables.add(exitButton);
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

    protected EffectSequence addStatsUpdateToEffectSequence(final TextLabelRenderable label, final String nextValue, final GameState gameState) {
        EffectSequence fadeInEffects = getShowPartEffectSequence();
        fadeInEffects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                EffectSequence effects = new EffectSequence();
                effects.addEffect(new FunctionEffect(new Runnable() {
                    @Override
                    public void run() {
                        if (label != null) {
                            gameState.addGameEvent(new GameEvent("GAME_EVENT_AUDIO_START_UI", UPGRADE_STATS_AUDIO_PATH));
                        }
                    }
                }));
                effects.addEffect(new FadeEffect(1, 0, 500));
                effects.addEffect(new FunctionEffect(new Runnable() {
                    @Override
                    public void run() {
                        if (label != null) {
                            label.setLabel(nextValue);
                        }
                    }
                }));
                effects.addEffect(new FadeEffect(0, 1, 500));
                effects.addEffect(new FunctionEffect(new Runnable() {
                    @Override
                    public void run() {
                        showExitButton();
                    }
                }));
                label.addEffect(effects);
            }
        }));
        return fadeInEffects;
    }

    public void showExitButton () {
        EffectSequence effects = new EffectSequence();
        effects.addEffect(new FadeEffect(1.0, 0, 0));
        effects.addEffect(new VisibilityEffect(true));
        effects.addEffect(new FadeEffect(0, 1.0, 500));
        exitButton.addEffect(effects);
    }

    public void addSolarPanel1(final GameState gameState) { solarPanel1.addEffect(addStatsUpdateToEffectSequence(unitsLabel, nextUnitsValue, gameState)); }

    public void addSolarPanel2(final GameState gameState) { solarPanel2.addEffect(addStatsUpdateToEffectSequence(unitsLabel, nextUnitsValue, gameState)); }

    public void addSolarPanel3(final GameState gameState) {
        solarPanel3.addEffect(addStatsUpdateToEffectSequence(unitsLabel, nextUnitsValue, gameState));
    }

    public void addSolarPanel4(final GameState gameState) {
        solarPanel4.addEffect(addStatsUpdateToEffectSequence(unitsLabel, nextUnitsValue, gameState));
    }

    public void addCentralTurbine(final GameState gameState) { centralTurbine.addEffect(addStatsUpdateToEffectSequence(distancePerUnitLabel, nextDistancePerUnitValue, gameState)); }

    public void addExtraTurbines(final GameState gameState) { extraTurbines.addEffect(addStatsUpdateToEffectSequence(distancePerUnitLabel, nextDistancePerUnitValue, gameState)); }

    public void addBattery(final GameState gameState) { battery.addEffect(addStatsUpdateToEffectSequence(null, null, gameState)); }

    public void addNextItem(int inventoryItemsCounter, final GameState gameState) {
        if (inventoryItemsCounter == 1)
            addCentralTurbine(gameState);
        else if (inventoryItemsCounter == 2)
            addExtraTurbines(gameState);
        else if (inventoryItemsCounter == 3)
            addSolarPanel1(gameState);
        else if (inventoryItemsCounter == 4)
            addSolarPanel2(gameState);
        else if (inventoryItemsCounter == 5)
            addSolarPanel3(gameState);
        else if (inventoryItemsCounter == 6)
            addSolarPanel4(gameState);
        else if (inventoryItemsCounter == 7)
            addBattery(gameState);
    }

    public void setUnitsValue(String units) {
        unitsLabel.setLabel(units);
    }

    public void setNextUnitsValue(String units) {
        nextUnitsValue = units;
    }

    public void setDistancePerUnitValue(String distancePerUnit) {
        distancePerUnitLabel.setLabel(distancePerUnit);
    }

    public void setNextDistancePerUnitValue(String distancePerUnit) {
        nextDistancePerUnitValue = distancePerUnit;
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

    public ActionButtonRenderable getExitButton() {
        return exitButton;
    }

    public void fadeOutAllExtraRenderables() {
        Effect effect = new FadeEffect(1, 0, 2000);
        exitButton.addEffect(effect);
        if (solarPanel1.isVisible())
            solarPanel1.addEffect(effect);
        if (solarPanel2.isVisible())
            solarPanel2.addEffect(effect);
        if (solarPanel3.isVisible())
            solarPanel3.addEffect(effect);
        if (solarPanel4.isVisible())
            solarPanel4.addEffect(effect);
        if (extraTurbines.isVisible())
            extraTurbines.addEffect(effect);
        if (centralTurbine.isVisible())
            centralTurbine.addEffect(effect);
        if (battery.isVisible())
            battery.addEffect(effect);
    }
}
