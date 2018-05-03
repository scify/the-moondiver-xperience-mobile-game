package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.Renderable;

import java.util.HashSet;
import java.util.Set;

public class CockpitRenderable extends FadingTableRenderable {
    //renderable image paths
    protected final static String BG_IMG_PATH = "img/episode_cockpit/bg.png";
    protected final static String MOTOR_EFFICIENCY_IMG_PATH = "img/cockpit/motor_efficiency.png";
    protected final static String REMAINING_ENERGY_IMG_PATH = "img/cockpit/remaining_energy.png";
    protected final static String DESTINATION_DISTANCE_IMG_PATH = "img/cockpit/destination_distance.png";
    protected final static String LOCATION_IMG_PATH = "img/cockpit/currentLocation.png";
    protected final static String DAYS_LEFT_IMG_PATH = "img/cockpit/days_left.png";
    protected final static String TRAVEL_BUTTON_DEFAULT_IMG_PATH = "img/episode_cockpit/travel.png";
    protected final static String TRAVEL_BUTTON_LIGHTED_IMG_PATH = "img/episode_cockpit/travel_lighted.png";
    protected final static String LAUNCH_BUTTON_DEFAULT_IMG_PATH = "img/episode_cockpit/launch.png";
    protected final static String LAUNCH_BUTTON_LIGHTED_IMG_PATH = "img/episode_cockpit/launch_lighted.png";
    protected final static String CONTACT_BUTTON_DEFAULT_IMG_PATH = "img/episode_cockpit/contact.png";
    protected final static String CONTACT_BUTTON_LIGHTED_IMG_PATH = "img/episode_cockpit/contact_lighted.png";
    protected final static String MAP_BUTTON_DEFAULT_IMG_PATH = "img/episode_cockpit/map.png";
    protected final static String MAP_BUTTON_LIGHTED_IMG_PATH = "img/episode_cockpit/map_lighted.png";
    protected final static String CHARGE_BUTTON_DEFAULT_IMG_PATH = "img/episode_cockpit/charge.png";
    protected final static String CHARGE_BUTTON_LIGHTED_IMG_PATH = "img/episode_cockpit/charge_lighted.png";
    protected final static String SPACESHIP_INVENTORY_BUTTON_DEFAULT_IMG_PATH = "img/episode_cockpit/spaceship.png";
    protected final static String SPACESHIP_INVENTORY_BUTTON_LIGHTED_IMG_PATH = "img/episode_cockpit/spaceship_lighted.png";

    //renderable ids
    protected final static String MOTOR_EFFICIENCY_ID = "motor_efficiency";
    protected final static String REMAINING_ENERGY_ID = "remaining_energy";
    protected final static String DESTINATION_DISTANCE_ID = "destination_distance";
    protected final static String LOCATION_ID = "location";
    protected final static String DAYS_LEFT_ID = "days_left";
    protected final static String TRAVEL_BUTTON_DEFAULT_ID = "travel_button";
    protected final static String TRAVEL_BUTTON_LIGHTED_ID = "travel_button_lighted";
    protected final static String LAUNCH_BUTTON_DEFAULT_ID = "launch_button";
    protected final static String LAUNCH_BUTTON_LIGHTED_ID = "launch_button_lighted";
    protected final static String CONTACT_BUTTON_DEFAULT_ID = "contact_button";
    protected final static String CONTACT_BUTTON_LIGHTED_ID = "contact_button_lighted";
    protected final static String MAP_BUTTON_DEFAULT_ID = "map_button";
    protected final static String MAP_BUTTON_LIGHTED_ID = "map_button_lighted";
    protected final static String CHARGE_BUTTON_DEFAULT_ID = "charge_button";
    protected final static String CHARGE_BUTTON_LIGHTED_ID = "charge_button_lighted";
    protected final static String SPACESHIP_INVENTORY_BUTTON_DEFAULT_ID = "spaceship_button";
    protected final static String SPACESHIP_INVENTORY_BUTTON_LIGHTED_ID = "spaceship_button_lighted";

    protected String motorEfficiencyValue;
    protected String remainingEnergyValue;
    protected int destinationDistanceValue;
    protected String positionValue;
    protected String daysLeftValue;

    protected ActionButtonRenderable travelButton;
    protected ActionButtonRenderable travelLightedButton;
    protected ActionButtonRenderable launchButton;
    protected ActionButtonRenderable launchLightedButton;
    protected ActionButtonRenderable chargeButton;
    protected ActionButtonRenderable chargeLighedButton;
    protected ActionButtonRenderable spaceshipInventoryButton;
    protected ActionButtonRenderable spaceshipInventoryLightedButton;
    protected ActionButtonRenderable mapButton;
    protected ActionButtonRenderable mapLightedButton;
    protected ActionButtonRenderable contactButton;
    protected ActionButtonRenderable contactLightedButton;

    protected boolean contactButtonIsLighted;
    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public CockpitRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id, BG_IMG_PATH);
        contactButtonIsLighted = false;
        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();

        travelButton = createImageButton(TRAVEL_BUTTON_DEFAULT_ID, TRAVEL_BUTTON_DEFAULT_IMG_PATH, UserActionCode.TRAVEL, true);
        allRenderables.add(travelButton);
        travelLightedButton = createImageButton(TRAVEL_BUTTON_LIGHTED_ID, TRAVEL_BUTTON_LIGHTED_IMG_PATH, UserActionCode.TRAVEL, false);
        allRenderables.add(travelLightedButton);

        launchButton = createImageButton(LAUNCH_BUTTON_DEFAULT_ID, LAUNCH_BUTTON_DEFAULT_IMG_PATH, UserActionCode.LAUNCH, true);
        allRenderables.add(launchButton);
        launchLightedButton = createImageButton(LAUNCH_BUTTON_LIGHTED_ID, LAUNCH_BUTTON_LIGHTED_IMG_PATH, UserActionCode.LAUNCH, false);
        allRenderables.add(launchLightedButton);

        contactButton = createImageButton(CONTACT_BUTTON_DEFAULT_ID, CONTACT_BUTTON_DEFAULT_IMG_PATH, UserActionCode.CONTACT_SCREEN_EPISODE, true);
        allRenderables.add(contactButton);
        contactLightedButton = createImageButton(CONTACT_BUTTON_LIGHTED_ID, CONTACT_BUTTON_LIGHTED_IMG_PATH, UserActionCode.CONTACT_SCREEN_EPISODE, false);
        allRenderables.add(contactLightedButton);

        mapButton = createImageButton(MAP_BUTTON_DEFAULT_ID, MAP_BUTTON_DEFAULT_IMG_PATH, UserActionCode.MAP_EPISODE, true);
        allRenderables.add(mapButton);
        mapLightedButton = createImageButton(MAP_BUTTON_LIGHTED_ID, MAP_BUTTON_LIGHTED_IMG_PATH, UserActionCode.MAP_EPISODE, false);
        allRenderables.add(mapLightedButton);

        spaceshipInventoryButton = createImageButton(SPACESHIP_INVENTORY_BUTTON_DEFAULT_ID, SPACESHIP_INVENTORY_BUTTON_DEFAULT_IMG_PATH, UserActionCode.SPACESHIP_PARTS_EPISODE, true);
        allRenderables.add(spaceshipInventoryButton);
        spaceshipInventoryLightedButton = createImageButton(SPACESHIP_INVENTORY_BUTTON_LIGHTED_ID, SPACESHIP_INVENTORY_BUTTON_LIGHTED_IMG_PATH, UserActionCode.SPACESHIP_PARTS_EPISODE, false);
        allRenderables.add(spaceshipInventoryLightedButton);

        chargeButton = createImageButton(CHARGE_BUTTON_DEFAULT_ID, CHARGE_BUTTON_DEFAULT_IMG_PATH, UserActionCode.CHARGE_SPACESHIP_EPISODE, true);
        allRenderables.add(chargeButton);
        chargeLighedButton = createImageButton(CHARGE_BUTTON_LIGHTED_ID, CHARGE_BUTTON_LIGHTED_IMG_PATH, UserActionCode.CHARGE_SPACESHIP_EPISODE, false);
        allRenderables.add(chargeLighedButton);
    }

    protected ActionButtonRenderable createImageButton(String id, String img, String code, boolean visibility) {
        ActionButtonRenderable ret = new ActionButtonRenderable(Renderable.ACTOR_IMAGE_BUTTON, id);
        ret.setZIndex(1);
        ret.setVisible(visibility);
        ret.setPositionDrawable(false);
        ret.setUserAction(new UserAction(code));
        return ret;
    }

    public String getMotorEfficiencyValue() {
        return motorEfficiencyValue;
    }

    public void setMotorEfficiencyValue(String motorEfficiencyValue) {
        this.motorEfficiencyValue = motorEfficiencyValue;
        renderableWasUpdated();
    }

    public String getRemainingEnergyValue() {
        return remainingEnergyValue;
    }

    public void setRemainingEnergyValue(String remainingEnergyValue) {
        this.remainingEnergyValue = remainingEnergyValue;
        renderableWasUpdated();
    }

    public int getDestinationDistanceValue() {
        return destinationDistanceValue;
    }

    public void setDestinationDistanceValue(int destinationDistanceValue) {
        this.destinationDistanceValue = destinationDistanceValue;
        renderableWasUpdated();
    }

    public String getPositionValue() {
        return positionValue;
    }

    public void setPositionValue(String positionValue) {
        this.positionValue = positionValue;
        renderableWasUpdated();
    }

    public ActionButtonRenderable getNavigateButton() {
        return navigateButton;
    }

    public ActionButtonRenderable getLaunchButton() {
        return launchButton;
    }

    public void setNavigateButton(ActionButtonRenderable navigateButton) {
        this.navigateButton = navigateButton;
    }

    public void setLaunchButton(ActionButtonRenderable launchButton) {
        this.launchButton = launchButton;
    }

    public ActionButtonRenderable getSpaceshipPartsButton() {
        return spaceshipPartsButton;
    }

    public void setSpaceshipPartsButton(ActionButtonRenderable spaceshipPartsButton) {
        this.spaceshipPartsButton = spaceshipPartsButton;
    }

    public ActionButtonRenderable getMapButton() {
        return mapButton;
    }

    public void setMapButton(ActionButtonRenderable mapButton) {
        this.mapButton = mapButton;
    }

    public boolean isContactButtonLighted() {
        return contactButtonIsLighted;
    }

    public ActionButtonRenderable getContactButtonSimple() {
        return contactButtonSimple;
    }

    public ActionButtonRenderable getContactButtonLighted() {
        return contactButtonLighted;
    }

    public void setContactButtons(ActionButtonRenderable contactButtonSimple, ActionButtonRenderable contactButtonLighted) {
        this.contactButtonSimple = contactButtonSimple;
        this.contactButtonLighted = contactButtonLighted;
    }

    public void toogleContactButton() {
        if (contactButtonIsLighted) {
            contactButtonIsLighted = false;
        } else {
            contactButtonIsLighted = true;
        }
        renderableWasUpdated();
    }

    public String getDaysLeftValue() {
        return daysLeftValue;
    }

    public void setDaysLeftValue(String daysLeftValue) {
        this.daysLeftValue = daysLeftValue;
        renderableWasUpdated();
    }

    public ActionButtonRenderable getChargeButton() {
        return chargeButton;
    }

    public void setChargeButton(ActionButtonRenderable chargeButton) {
        this.chargeButton = chargeButton;
    }
}
