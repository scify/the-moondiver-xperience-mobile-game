package org.scify.moonwalker.app.ui.renderables;


import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.FunctionEffect;
import java.util.HashSet;
import java.util.Set;

public class ChargeEpisodeRenderable extends FadingTableRenderable {
    //renderable image paths
    protected static final String BG_IMG_PATH = "img/episode_charge/bg.png";
    protected static final String CHARGE_BUTTON_IMG_PATH = "img/episode_charge/charge_button_green.png";
    protected static final String EXIT_BUTTON_IMG_PATH = "img/episode_charge/exit_button.png";

    //renderable ids
    protected static final String CHARGE_BUTTON_ID = "charge_button";
    protected static final String EXIT_BUTTON_ID = "exit_button";
    protected static final String MOON_PHASE1_ID = "moon_phase";
    protected static final String MOON_PHASE2_ID = "moon_phase";
    protected static final String MOON_PHASE3_ID = "moon_phase";
    protected static final String UNITS_PER_NIGHT1_ID = "units_per_night1";
    protected static final String UNITS_PER_NIGHT2_ID = "units_per_night2";
    protected static final String UNITS_PER_NIGHT3_ID = "units_per_night3";
    protected static final String DISTANCE_ID = "distance";
    protected static final String DISTANCE_PER_UNIT_ID = "distance_per_unit";
    protected static final String ENERGY_ID = "energy";

    protected ImageRenderable moonPhase1 = null;
    protected ImageRenderable moonPhase2 = null;
    protected ImageRenderable moonPhase3 = null;

    protected ActionButtonRenderable exitButton;
    protected ActionButtonRenderable chargeButton;

    protected TextLabelRenderable unitsLabel1;
    protected String nextUnitsValue1;
    protected TextLabelRenderable unitsLabel2;
    protected String nextUnitsValue2;
    protected TextLabelRenderable unitsLabel3;
    protected String nextUnitsValue3;
    protected TextLabelRenderable distanceFromDestinationLabel;
    protected TextLabelRenderable distancePerUnitLabel;
    protected TextLabelRenderable energyLabel;
    protected String nextEnergyValue;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public ChargeEpisodeRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, ACTOR_EPISODE_CHARGER, id, BG_IMG_PATH);
        initSubRenderables();
        nextUnitsValue1 = null;
        nextUnitsValue2 = null;
        nextUnitsValue3 = null;
        nextEnergyValue = null;
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();

        exitButton = createImageButton(EXIT_BUTTON_ID, EXIT_BUTTON_IMG_PATH, new UserAction(UserActionCode.QUIT), false, true, 100);
        allRenderables.add(exitButton);

        chargeButton = createImageButton(CHARGE_BUTTON_ID, CHARGE_BUTTON_IMG_PATH, new UserAction(UserActionCode.CHARGE_SPACESHIP_PASS_DAY), false, true, 100);
        allRenderables.add(chargeButton);

        unitsLabel1 = createTextLabelRenderable(UNITS_PER_NIGHT1_ID, "", false, true, 5);
        allRenderables.add(unitsLabel1);

        unitsLabel2 = createTextLabelRenderable(UNITS_PER_NIGHT2_ID, "", false, true, 5);
        allRenderables.add(unitsLabel2);

        unitsLabel3 = createTextLabelRenderable(UNITS_PER_NIGHT3_ID, "", false, true, 5);
        allRenderables.add(unitsLabel3);

        distanceFromDestinationLabel = createTextLabelRenderable(DISTANCE_ID, "", false, true, 5);
        allRenderables.add(distanceFromDestinationLabel);

        distancePerUnitLabel = createTextLabelRenderable(DISTANCE_PER_UNIT_ID, "", false, true, 5);
        allRenderables.add(distancePerUnitLabel);

        energyLabel = createTextLabelRenderable(ENERGY_ID, "", false, true, 5);
        allRenderables.add(energyLabel);
    }

    public void setCurrentMoonPhaseInfo (int energy, String imgPath) {
        nextUnitsValue1 = energy + " UNITS";
        if (moonPhase1 == null) {
            moonPhase1 = createImageRenderable(MOON_PHASE1_ID, imgPath, false, true, 5);
            allRenderables.add(moonPhase1);
            unitsLabel1.setLabel(nextUnitsValue1);
        }else {
            updateImage(moonPhase1, imgPath);
            updateLabel(unitsLabel1, nextUnitsValue1);
        }
    }

    public void setNextMoonPhaseInfo (int energy, String imgPath) {
        nextUnitsValue2 = energy + " UNITS";
        if (moonPhase2 == null) {
            moonPhase2 = createImageRenderable(MOON_PHASE2_ID, imgPath, false, true, 5);
            allRenderables.add(moonPhase2);
            unitsLabel2.setLabel(nextUnitsValue2);
        }else {
            updateImage(moonPhase2, imgPath);
            updateLabel(unitsLabel2, nextUnitsValue2);
        }
    }

    public void setPostNextMoonPhaseInfo (int energy, String imgPath) {
        nextUnitsValue3 = energy + " UNITS";
        if (moonPhase3 == null) {
            moonPhase3 = createImageRenderable(MOON_PHASE3_ID, imgPath, false, true, 5);
            allRenderables.add(moonPhase3);
            unitsLabel3.setLabel(nextUnitsValue3);
        }else {
            updateImage(moonPhase3, imgPath);
            updateLabel(unitsLabel3, nextUnitsValue3);
        }
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    protected void updateLabel(final TextLabelRenderable label, final String nextValue) {
        EffectSequence effects = new EffectSequence();
        effects.addEffect(new FadeEffect(1, 0, 500));
        effects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                label.setLabel(nextValue);
            }
        }));
        effects.addEffect(new FadeEffect(0, 1, 500));
        label.addEffect(effects);
    }

    protected void updateImage(final ImageRenderable image, final String imagePath) {
        EffectSequence effects = new EffectSequence();
        effects.addEffect(new FadeEffect(1, 0, 500));
        effects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                image.setImgPath(imagePath);
                image.setImgPathWasUpdated(true);
            }
        }));
        effects.addEffect(new FadeEffect(0, 1, 500));
        image.addEffect(effects);
    }

    public ImageRenderable getMoonPhase1() {
        return moonPhase1;
    }

    public ImageRenderable getMoonPhase2() {
        return moonPhase2;
    }

    public ImageRenderable getMoonPhase3() {
        return moonPhase3;
    }

    public ActionButtonRenderable getExitButton() {
        return exitButton;
    }

    public ActionButtonRenderable getChargeButton() {
        return chargeButton;
    }

    public TextLabelRenderable getUnitsLabel1() {
        return unitsLabel1;
    }

    public TextLabelRenderable getUnitsLabel2() {
        return unitsLabel2;
    }

    public TextLabelRenderable getUnitsLabel3() {
        return unitsLabel3;
    }

    public TextLabelRenderable getDistanceFromDestinationLabel() {
        return distanceFromDestinationLabel;
    }

    public TextLabelRenderable getDistancePerUnitLabel() {
        return distancePerUnitLabel;
    }

    public TextLabelRenderable getEnergyLabel() {
        return energyLabel;
    }

    public void setDistanceFromDestinationLabel(String distance) {
        updateLabel(distanceFromDestinationLabel, distance);
    }

    public void setDistancePerUnitLabel(String distancePerUnit) {
        updateLabel(distancePerUnitLabel, distancePerUnit);
    }

    public void setEnergyLabel(String energy) {
        updateLabel(energyLabel, energy);
    }
}
