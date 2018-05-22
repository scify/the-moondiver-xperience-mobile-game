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
import org.scify.moonwalker.app.ui.actors.calculator.CalculatorController;

import java.util.HashSet;
import java.util.Set;

public class ChargeEpisodeRenderable extends ChattableRenderable {
    //renderable image paths
    protected static final String BG_IMG_PATH = "img/episode_charge/bg.png";
    protected static final String CHARGE_BUTTON_IMG_PATH = "img/episode_charge/charge_button_green.png";
    protected static final String EXIT_BUTTON_IMG_PATH = "img/episode_charge/exit_button.png";
    protected static final String TRANSPARENT_BUTTON_IMG_PATH = "img/episode_charge/transparent_button.png";
    public static final String CONVERSATION_BG_IMG_PATH = "img/episode_charge/conversation_bg.png";

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
    protected static final String CALCULATOR_RESULT_ID = "calculator_result";
    protected static final String CALCULATOR_7_ID = "calculator_7";
    protected static final String CALCULATOR_8_ID = "calculator_8";
    protected static final String CALCULATOR_9_ID = "calculator_9";
    protected static final String CALCULATOR_DIV_ID = "calculator_div";
    protected static final String CALCULATOR_4_ID = "calculator_4";
    protected static final String CALCULATOR_5_ID = "calculator_5";
    protected static final String CALCULATOR_6_ID = "calculator_6";
    protected static final String CALCULATOR_MULT_ID = "calculator_mult";
    protected static final String CALCULATOR_1_ID = "calculator_1";
    protected static final String CALCULATOR_2_ID = "calculator_2";
    protected static final String CALCULATOR_3_ID = "calculator_3";
    protected static final String CALCULATOR_MINUS_ID = "calculator_minus";
    protected static final String CALCULATOR_0_ID = "calculator_0";
    protected static final String CALCULATOR_C_ID = "calculator_c";
    protected static final String CALCULATOR_EQUALS_ID = "calculator_equals";
    protected static final String CALCULATOR_PLUS_ID = "calculator_plus";

    //audio
    public static final String POWER_UP_AUDIO_PATH = "audio/episode_charge/power_up.mp3";
    public static final String CLICK_AUDIO_PATH = "audio/button1.mp3";
    public static final String WRONG_BUTTON_AUDIO_PATH = "audio/wrong.mp3";

    protected ImageRenderable moonPhase1 = null;
    protected ImageRenderable moonPhase2 = null;
    protected ImageRenderable moonPhase3 = null;

    protected ActionButtonRenderable exitButton;
    protected ActionButtonRenderable chargeButton;
    protected ActionButtonRenderable calculatorButton1;
    protected ActionButtonRenderable calculatorButton2;
    protected ActionButtonRenderable calculatorButton3;
    protected ActionButtonRenderable calculatorButton4;
    protected ActionButtonRenderable calculatorButton5;
    protected ActionButtonRenderable calculatorButton6;
    protected ActionButtonRenderable calculatorButton7;
    protected ActionButtonRenderable calculatorButton8;
    protected ActionButtonRenderable calculatorButton9;
    protected ActionButtonRenderable calculatorButton0;
    protected ActionButtonRenderable calculatorButtonC;
    protected ActionButtonRenderable calculatorButtonEquals;
    protected ActionButtonRenderable calculatorButtonPlus;
    protected ActionButtonRenderable calculatorButtonMinus;
    protected ActionButtonRenderable calculatorButtonMulti;
    protected ActionButtonRenderable calculatorButtonDiv;

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
    protected TextLabelRenderable calculatorLabel;

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
        chatEnabled = false;
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

        calculatorLabel = createTextLabelRenderable(CALCULATOR_RESULT_ID, "0", false, true, 5);
        allRenderables.add(calculatorLabel);

        calculatorButton0 = createImageButton(CALCULATOR_0_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.ZERO), false, true, 100);
        allRenderables.add(calculatorButton0);

        calculatorButton1 = createImageButton(CALCULATOR_1_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.ONE), false, true, 100);
        allRenderables.add(calculatorButton1);

        calculatorButton2 = createImageButton(CALCULATOR_2_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.TWO), false, true, 100);
        allRenderables.add(calculatorButton2);

        calculatorButton3 = createImageButton(CALCULATOR_3_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.THREE), false, true, 100);
        allRenderables.add(calculatorButton3);

        calculatorButton4 = createImageButton(CALCULATOR_4_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.FOUR), false, true, 100);
        allRenderables.add(calculatorButton4);

        calculatorButton5 = createImageButton(CALCULATOR_5_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.FIVE), false, true, 100);
        allRenderables.add(calculatorButton5);

        calculatorButton6 = createImageButton(CALCULATOR_6_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.SIX), false, true, 100);
        allRenderables.add(calculatorButton6);

        calculatorButton7 = createImageButton(CALCULATOR_7_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.SEVEN), false, true, 100);
        allRenderables.add(calculatorButton7);

        calculatorButton8 = createImageButton(CALCULATOR_8_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.EIGHT), false, true, 100);
        allRenderables.add(calculatorButton8);

        calculatorButton9 = createImageButton(CALCULATOR_9_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.NINE), false, true, 100);
        allRenderables.add(calculatorButton9);

        calculatorButtonC = createImageButton(CALCULATOR_C_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.RESET), false, true, 100);
        allRenderables.add(calculatorButtonC);

        calculatorButtonPlus = createImageButton(CALCULATOR_PLUS_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.ADD), false, true, 100);
        allRenderables.add(calculatorButtonPlus);

        calculatorButtonMinus = createImageButton(CALCULATOR_MINUS_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.SUBTRACT), false, true, 100);
        allRenderables.add(calculatorButtonMinus);

        calculatorButtonDiv = createImageButton(CALCULATOR_DIV_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.DIVIDE), false, true, 100);
        allRenderables.add(calculatorButtonDiv);

        calculatorButtonMulti = createImageButton(CALCULATOR_MULT_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.MULTIPLY), false, true, 100);
        allRenderables.add(calculatorButtonMulti);

        calculatorButtonEquals = createImageButton(CALCULATOR_EQUALS_ID, TRANSPARENT_BUTTON_IMG_PATH, new UserAction(UserActionCode.CALCULATOR_OPERATION, CalculatorController.RESULT), false, true, 100);
        allRenderables.add(calculatorButtonEquals);
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

    public TextLabelRenderable getCalculatorLabel() { return calculatorLabel; }

    public ActionButtonRenderable getCalculatorButton1() { return calculatorButton1; }

    public ActionButtonRenderable getCalculatorButton2() { return calculatorButton2; }

    public ActionButtonRenderable getCalculatorButton3() { return calculatorButton3; }

    public ActionButtonRenderable getCalculatorButton4() { return calculatorButton4; }

    public ActionButtonRenderable getCalculatorButton5() { return calculatorButton5; }

    public ActionButtonRenderable getCalculatorButton6() { return calculatorButton6; }

    public ActionButtonRenderable getCalculatorButton7() { return calculatorButton7; }

    public ActionButtonRenderable getCalculatorButton8() { return calculatorButton8; }

    public ActionButtonRenderable getCalculatorButton9() { return calculatorButton9; }

    public ActionButtonRenderable getCalculatorButton0() { return calculatorButton0; }

    public ActionButtonRenderable getCalculatorButtonC() { return calculatorButtonC; }

    public ActionButtonRenderable getCalculatorButtonEquals() { return calculatorButtonEquals; }

    public ActionButtonRenderable getCalculatorButtonPlus() { return calculatorButtonPlus; }

    public ActionButtonRenderable getCalculatorButtonMinus() { return calculatorButtonMinus; }

    public ActionButtonRenderable getCalculatorButtonMulti() { return calculatorButtonMulti; }

    public ActionButtonRenderable getCalculatorButtonDiv() { return calculatorButtonDiv; }

    public void setCalculatorLabel(String value) {
        calculatorLabel.setLabel(value);
    }

    public String getCalculatorValue() {
        return calculatorLabel.getLabel();
    }
}
