package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import org.scify.engine.renderables.effects.DelayEffect;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.VisibilityEffect;
import org.scify.moonwalker.app.game.Location;

import java.util.HashSet;
import java.util.Set;

public class CockpitRenderable extends FadingTableRenderable {
    //renderable image paths
    protected static final String BG_IMG_PATH = "img/episode_cockpit/bg.png";
    protected static final String DAYS_LEFT_IMG_PATH = "img/cockpit/days_left.png";
    protected static final String TRAVEL_BUTTON_DEFAULT_IMG_PATH = "img/episode_cockpit/travel.png";
    protected static final String TRAVEL_BUTTON_LIGHTED_IMG_PATH = "img/episode_cockpit/travel_lighted.png";
    protected static final String LAUNCH_BUTTON_DEFAULT_IMG_PATH = "img/episode_cockpit/launch.png";
    protected static final String LAUNCH_BUTTON_LIGHTED_IMG_PATH = "img/episode_cockpit/launch_lighted.png";
    protected static final String CONTACT_BUTTON_DEFAULT_IMG_PATH = "img/episode_cockpit/contact.png";
    protected static final String CONTACT_BUTTON_LIGHTED_IMG_PATH = "img/episode_cockpit/contact_lighted.png";
    protected static final String MAP_BUTTON_DEFAULT_IMG_PATH = "img/episode_cockpit/map.png";
    protected static final String MAP_BUTTON_LIGHTED_IMG_PATH = "img/episode_cockpit/map_lighted.png";
    protected static final String CHARGE_BUTTON_DEFAULT_IMG_PATH = "img/episode_cockpit/charge.png";
    protected static final String CHARGE_BUTTON_LIGHTED_IMG_PATH = "img/episode_cockpit/charge_lighted.png";
    protected static final String SPACESHIP_INVENTORY_BUTTON_DEFAULT_IMG_PATH = "img/episode_cockpit/spaceship.png";
    protected static final String SPACESHIP_INVENTORY_BUTTON_LIGHTED_IMG_PATH = "img/episode_cockpit/spaceship_lighted.png";
    protected static final String LEFT_TABLET_IMG_PATH = "img/episode_cockpit/left_tablet.png";
    protected static final String RIGHT_TABLET_IMG_PATH = "img/episode_cockpit/right_tablet.png";
    //OUTSIDE BackGroundsGs
    public static final String FOREST_BG_IMG_PATH = "img/episode_cockpit/outside_bgs/forest.png";

    //renderable ids
    protected static final String MOTOR_EFFICIENCY_ID = "motor_efficiency";
    protected static final String REMAINING_ENERGY_ID = "remaining_energy";
    protected static final String DESTINATION_DISTANCE_ID = "destination_distance";
    protected static final String LEFT_TABLET_ID = "left_tablet";
    protected static final String LOCATION_ID = "location";
    protected static final String RIGHT_TABLET_ID = "right_tablet";
    protected static final String DAYS_LEFT_ID = "days_left";
    protected static final String TRAVEL_BUTTON_DEFAULT_ID = "travel_button";
    protected static final String TRAVEL_BUTTON_LIGHTED_ID = "travel_button_lighted";
    protected static final String LAUNCH_BUTTON_DEFAULT_ID = "launch_button";
    protected static final String LAUNCH_BUTTON_LIGHTED_ID = "launch_button_lighted";
    protected static final String CONTACT_BUTTON_DEFAULT_ID = "contact_button";
    protected static final String CONTACT_BUTTON_LIGHTED_ID = "contact_button_lighted";
    protected static final String MAP_BUTTON_DEFAULT_ID = "map_button";
    protected static final String MAP_BUTTON_LIGHTED_ID = "map_button_lighted";
    protected static final String CHARGE_BUTTON_DEFAULT_ID = "charge_button";
    protected static final String CHARGE_BUTTON_LIGHTED_ID = "charge_button_lighted";
    protected static final String SPACESHIP_INVENTORY_BUTTON_DEFAULT_ID = "spaceship_button";
    protected static final String SPACESHIP_INVENTORY_BUTTON_LIGHTED_ID = "spaceship_button_lighted";
    protected static final String OUTSIDE_BACKGROUND_ID = "outside_background";

    protected TextLabelRenderable motorEfficiencyLabel;
    protected TextLabelRenderable energyLabel;
    protected TextLabelRenderable distanceLabel;
    protected TextLabelRenderable locationLabel;
    protected TextLabelRenderable daysLeftLabel;

    protected ImageRenderable leftTablet;
    protected ImageRenderable rightTablet;
    protected ImageRenderable outside_background;

    protected ActionButtonRenderable travelButton;
    protected ActionButtonRenderable travelLightedButton;
    protected ActionButtonRenderable launchButton;
    protected ActionButtonRenderable launchLightedButton;
    protected ActionButtonRenderable chargeButton;
    protected ActionButtonRenderable chargeLightedButton;
    protected ActionButtonRenderable spaceshipInventoryButton;
    protected ActionButtonRenderable spaceshipInventoryLightedButton;
    protected ActionButtonRenderable mapButton;
    protected ActionButtonRenderable mapLightedButton;
    protected ActionButtonRenderable contactButton;
    protected ActionButtonRenderable contactLightedButton;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public CockpitRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, ACTOR_EPISODE_COCKPIT, id, BG_IMG_PATH);
        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();

        leftTablet = createImageRenderable(LEFT_TABLET_ID, LEFT_TABLET_IMG_PATH, false, true, 2);
        allRenderables.add(leftTablet);
        locationLabel = createTextRotatableLabelRenderable(LOCATION_ID, "", false, true, 3);
        allRenderables.add(locationLabel);

        rightTablet = createImageRenderable(RIGHT_TABLET_ID, RIGHT_TABLET_IMG_PATH, false, true, 2);
        allRenderables.add(rightTablet);
        daysLeftLabel = createTextLabelRenderable(DAYS_LEFT_ID, "", false, true, 3);
        allRenderables.add(daysLeftLabel);

        UserAction travelUserAction = new UserAction(UserActionCode.TRAVEL);
        travelButton = createImageButton(TRAVEL_BUTTON_DEFAULT_ID, TRAVEL_BUTTON_DEFAULT_IMG_PATH, travelUserAction, false, true, 2);
        allRenderables.add(travelButton);
        travelLightedButton = createImageButton(TRAVEL_BUTTON_LIGHTED_ID, TRAVEL_BUTTON_LIGHTED_IMG_PATH, travelUserAction, false, false, 2);
        allRenderables.add(travelLightedButton);

        UserAction launchUserAction = new UserAction(UserActionCode.LAUNCH);
        launchButton = createImageButton(LAUNCH_BUTTON_DEFAULT_ID, LAUNCH_BUTTON_DEFAULT_IMG_PATH, launchUserAction, false, true, 2);
        allRenderables.add(launchButton);
        launchLightedButton = createImageButton(LAUNCH_BUTTON_LIGHTED_ID, LAUNCH_BUTTON_LIGHTED_IMG_PATH, launchUserAction, false, false, 2);
        allRenderables.add(launchLightedButton);

        UserAction contactUserAction = new UserAction(UserActionCode.CONTACT_SCREEN_EPISODE);
        contactButton = createImageButton(CONTACT_BUTTON_DEFAULT_ID, CONTACT_BUTTON_DEFAULT_IMG_PATH, contactUserAction, false, true, 2);
        allRenderables.add(contactButton);
        contactLightedButton = createImageButton(CONTACT_BUTTON_LIGHTED_ID, CONTACT_BUTTON_LIGHTED_IMG_PATH, contactUserAction, false, false, 2);
        allRenderables.add(contactLightedButton);

        UserAction mapUserAction = new UserAction(UserActionCode.MAP_EPISODE);
        mapButton = createImageButton(MAP_BUTTON_DEFAULT_ID, MAP_BUTTON_DEFAULT_IMG_PATH, mapUserAction, false, true, 2);
        allRenderables.add(mapButton);
        mapLightedButton = createImageButton(MAP_BUTTON_LIGHTED_ID, MAP_BUTTON_LIGHTED_IMG_PATH, mapUserAction, false, false, 2);
        allRenderables.add(mapLightedButton);

        UserAction inventoryUserAction = new UserAction(UserActionCode.SPACESHIP_PARTS_EPISODE);
        spaceshipInventoryButton = createImageButton(SPACESHIP_INVENTORY_BUTTON_DEFAULT_ID, SPACESHIP_INVENTORY_BUTTON_DEFAULT_IMG_PATH, inventoryUserAction, false, true, 2);
        allRenderables.add(spaceshipInventoryButton);
        spaceshipInventoryLightedButton = createImageButton(SPACESHIP_INVENTORY_BUTTON_LIGHTED_ID, SPACESHIP_INVENTORY_BUTTON_LIGHTED_IMG_PATH, inventoryUserAction, false, false, 2);
        allRenderables.add(spaceshipInventoryLightedButton);

        UserAction chargeUserAction = new UserAction(UserActionCode.CHARGE_SPACESHIP_EPISODE);
        chargeButton = createImageButton(CHARGE_BUTTON_DEFAULT_ID, CHARGE_BUTTON_DEFAULT_IMG_PATH, chargeUserAction, false, true, 2);
        allRenderables.add(chargeButton);
        chargeLightedButton = createImageButton(CHARGE_BUTTON_LIGHTED_ID, CHARGE_BUTTON_LIGHTED_IMG_PATH, chargeUserAction, false, false, 2);
        allRenderables.add(chargeLightedButton);

        motorEfficiencyLabel = createTextLabelRenderable(MOTOR_EFFICIENCY_ID, "", false, true, 2);
        allRenderables.add(motorEfficiencyLabel);
        energyLabel = createTextLabelRenderable(REMAINING_ENERGY_ID, "", false, true, 2);
        allRenderables.add(energyLabel);
        distanceLabel = createTextLabelRenderable(DESTINATION_DISTANCE_ID, "", false, true, 2);
        allRenderables.add(distanceLabel);
    }

    public void setLocationValue(String location) {
        locationLabel.setLabel(location);
    }

    public void setMotorEfficiencyValue(String motorEfficiency) {
        motorEfficiencyLabel.setLabel(motorEfficiency);
    }

    public void setRemainingEnergyValue(String energy) {
        energyLabel.setLabel(energy);
    }

    public void setDestinationDistanceValue(int distance) {
        distanceLabel.setLabel(distance + "");
    }

    public void setDaysLeftValue(String daysLeft) {
        daysLeftLabel.setLabel(daysLeft);
    }


    public boolean isButtonLighted(ActionButtonRenderable buttonRenderable) {
        return buttonRenderable.isVisible();
    }

    public void toogleButtonLight(ActionButtonRenderable buttonRenderable) {
        if (buttonRenderable.isVisible()) {
            buttonRenderable.setVisible(false);
        } else {
            buttonRenderable.setVisible(true);
        }
    }

    public TextLabelRenderable getMotorEfficiencyLabel() {
        return motorEfficiencyLabel;
    }

    public TextLabelRenderable getEnergyLabel() {
        return energyLabel;
    }

    public TextLabelRenderable getDistanceLabel() {
        return distanceLabel;
    }

    public TextLabelRenderable getLocationLabel() {
        return locationLabel;
    }

    public TextLabelRenderable getDaysLeftLabel() {
        return daysLeftLabel;
    }

    public ImageRenderable getLeftTablet() {

        return leftTablet;
    }

    public ImageRenderable getRightTablet() {
        return rightTablet;
    }

    public ActionButtonRenderable getTravelButton() {

        return travelButton;
    }

    public ActionButtonRenderable getTravelLightedButton() {
        return travelLightedButton;
    }

    public ActionButtonRenderable getLaunchButton() {
        return launchButton;
    }

    public ActionButtonRenderable getLaunchLightedButton() {
        return launchLightedButton;
    }

    public ActionButtonRenderable getChargeButton() {
        return chargeButton;
    }

    public ActionButtonRenderable getChargeLightedButton() {
        return chargeLightedButton;
    }

    public ActionButtonRenderable getSpaceshipInventoryButton() {
        return spaceshipInventoryButton;
    }

    public ActionButtonRenderable getSpaceshipInventoryLightedButton() {
        return spaceshipInventoryLightedButton;
    }

    public ActionButtonRenderable getMapButton() {
        return mapButton;
    }

    public ActionButtonRenderable getMapLightedButton() {
        return mapLightedButton;
    }

    public ActionButtonRenderable getContactButton() {
        return contactButton;
    }

    public ActionButtonRenderable getContactLightedButton() {
        return contactLightedButton;
    }

    public void setOutsideBackground(String imgPath) {
        outside_background = createImageRenderable(OUTSIDE_BACKGROUND_ID, imgPath, true, false, 0);
        outside_background.setHeight(appInfo.getScreenHeight());
        outside_background.setWidth(appInfo.getScreenWidth());
        EffectSequence fadeInEffects = new EffectSequence();
        fadeInEffects.addEffect(new FadeEffect(1.0, 0, 0));
        fadeInEffects.addEffect(new VisibilityEffect(true));
        fadeInEffects.addEffect(new DelayEffect(200));
        fadeInEffects.addEffect(new FadeEffect(0.0, 1.0, 2500));
        outside_background.addEffect(fadeInEffects);
        allRenderables.add(outside_background);
    }

    public void fadeoutOutsideBackground() {
        outside_background.addEffect(new FadeEffect(1.0, 0, 1000));
    }

    public String getBG_IMG_PATH(Location location) {
        switch (location.getName()) {
            case "Αθήνα":
                return null;
            default:
                return null;
        }
    }
}
