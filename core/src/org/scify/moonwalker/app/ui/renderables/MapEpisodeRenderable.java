package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.VisibilityEffect;
import org.scify.moonwalker.app.game.Location;

import java.util.ArrayList;
import java.util.List;

public class MapEpisodeRenderable extends FadingTableRenderable {

    public static final String MAP_SELECT_ACTION = "MAP_SELECT";
    public static final String BG_IMAGE_PATH = "img/episode_map/bg.png";
    public static final String QUIT_BUTTON_IMG_PATH = "img/close.png";

    protected Location currentLocation;
    protected Location nextAllowedLocation;
    protected boolean locationSelected;
    protected List<Location> locations;
    protected List<Renderable> locationPoints;
    protected List<Renderable> locationNames;

    protected ActionButtonRenderable closeButton;
    protected TextLabelRenderable missionHUD;

    protected TextLabelRenderable locationNameHUD;
    protected TextLabelRenderable distanceHUD;


    public MapEpisodeRenderable(float xPos, float yPos, float width, float height, String id, List<Location> lLocations) {
        super(xPos, yPos, width, height, Renderable.ACTOR_EPISODE_MAP_LOCATION, id, BG_IMAGE_PATH);

        locations = lLocations;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        renderableWasUpdated();
    }

    public void setNextAllowedLocation(Location nextAllowedLocation) {
        this.nextAllowedLocation = nextAllowedLocation;
        renderableWasUpdated();
    }

    public void setLocationSelected(boolean locationSelected) {
        this.locationSelected = locationSelected;
        renderableWasUpdated();
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Location getNextAllowedLocation() {
        return nextAllowedLocation;
    }

    public boolean isLocationSelected() {
        return locationSelected;
    }


    public ActionButtonRenderable  getCloseButton() {
        if (closeButton == null) {
            closeButton = new ActionButtonRenderable(appInfo.convertX(1750f), appInfo.convertY(1090 - 30 - 128f),
                    appInfo.convertX(128),appInfo.convertY(128), Renderable.ACTOR_IMAGE_BUTTON, "closeBtn");
            closeButton.setImgPath(QUIT_BUTTON_IMG_PATH);
            closeButton.setUserAction(new UserAction(UserActionCode.QUIT, null));

            closeButton.setVisible(false);
            closeButton.setZIndex(5);
            closeButton.addEffect(getDefaultFadeInEffect());
        }

        return closeButton;

    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Renderable getMissionHUD() {
        if (missionHUD == null) {
            missionHUD = new TextLabelRenderable(0f,0f, appInfo.convertX(100), appInfo.convertY(50), Renderable.ACTOR_LABEL, "missionHUD");
            missionHUD.setLabel("Mission");
            missionHUD.setPositionDrawable(false);
//            missionHUD.setVisible(false);
        }

        return missionHUD;
    }
    public TextLabelRenderable getLocationNameHUD() {
        if (locationNameHUD == null) {
            locationNameHUD = new TextLabelRenderable(0f,25f, 100, 20, Renderable.ACTOR_LABEL, "locationNameHUD");
            locationNameHUD.setLabel("Location");
            locationNameHUD.setPositionDrawable(false);

//            locationNameHUD.setVisible(false);
        }
        return locationNameHUD;
    }

    public TextLabelRenderable getDistanceHUD() {
        if (distanceHUD == null) {
            distanceHUD = new TextLabelRenderable(0f,65f, 100, 20, Renderable.ACTOR_LABEL, "distanceHUD");
            distanceHUD.setLabel("Distance");
            distanceHUD.setPositionDrawable(false);

//            distanceHUD.setVisible(false);
        }
        return distanceHUD;
    }

    public List<Renderable> getLocationPoints() {
        if (locationPoints == null) {
            locationPoints = new ArrayList<>();

            // For each location (point)
            for (Location lCur : getLocations()) {
                Renderable rNew;
                if (lCur.equals(nextAllowedLocation)) {
                    ActionButtonRenderable btn = new ActionButtonRenderable(lCur.getPosX(), lCur.getPosY(), 100, 50, Renderable.ACTOR_IMAGE_BUTTON, "nextAllowedPoint");
                    btn.setImgPath("img/episode_map/athens.png");
                    btn.setUserAction(new UserAction(MAP_SELECT_ACTION, lCur));
                    locationPoints.add(btn);
                    rNew = btn;
                }
                else {
                    // Create corresponding image with effectrNew
                    rNew = new ImageRenderable(lCur.getPosX(), lCur.getPosY(), 10, 10, lCur.toString(), "img/episode_map/pin.png");
                    locationPoints.add(rNew);

                }
//                rNew.setVisible(false);
            }
        }

        return locationPoints;
    }

    @Override
    public void fadeIn() {
        Effect eFadeIn = getDefaultFadeInEffect();
        // Apply fadeOut effects to all renderables
        this.closeButton.addEffect(eFadeIn);
        this.distanceHUD.addEffect(eFadeIn);
        this.locationNameHUD.addEffect(eFadeIn);
        this.missionHUD.addEffect(eFadeIn);
        for (Renderable rCur : this.getLocationPoints()) {
            rCur.addEffect(eFadeIn);
        }
        for (Renderable rCur : this.getLocationNameLabels()) {
            rCur.addEffect(eFadeIn);
        }

        super.fadeIn();
    }

    @Override
    public void fadeOut() {
        Effect eFadeOut = getDefaultFadeOutEffect();
        // Apply fadeOut effects to all renderables
        this.closeButton.addEffect(eFadeOut);
        this.distanceHUD.addEffect(eFadeOut);
        this.locationNameHUD.addEffect(eFadeOut);
        this.missionHUD.addEffect(eFadeOut);
        for (Renderable rCur : this.getLocationPoints()) {
            rCur.addEffect(eFadeOut);
        }
        for (Renderable rCur : this.getLocationNameLabels()) {
            rCur.addEffect(eFadeOut);
        }


        super.fadeOut();
    }

    private Effect getDefaultFadeInEffect() {
        EffectSequence fadeInEffects = new EffectSequence();
        // Add actual fade effect
        fadeInEffects.addEffect(new FadeEffect(1.0, 0.0, 0));
        fadeInEffects.addEffect(new VisibilityEffect(true));
        fadeInEffects.addEffect(new FadeEffect(0.0, 1.0, 2000));
        return fadeInEffects;
    }


    private Effect getDefaultFadeOutEffect() {
        EffectSequence fadeOutEffects = new EffectSequence();
        // Add actual fade effect
        fadeOutEffects.addEffect(new FadeEffect(1.0, 0.0, 2000));
        fadeOutEffects.addEffect(new VisibilityEffect(false));
        return fadeOutEffects;
    }

    public List<Renderable> getLocationNameLabels() {
        if (locationNames == null) {
            List<Renderable> lRes = new ArrayList<>();

            // For each location (name)
            for (Location lCur : getLocations()) {
                // Create corresponding image with effect
                lRes.add(new ImageRenderable(lCur.getPosX(), lCur.getPosY(), 200, 20, lCur.toString(), lCur.getImgPath()));
            }

            locationNames = lRes;
        }

        return locationNames;
    }

    public void setNextLocation(Location newLocation) {
        this.locationSelected = true;
        renderableWasUpdated();
    }
}
