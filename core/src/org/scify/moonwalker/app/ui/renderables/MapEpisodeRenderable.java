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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapEpisodeRenderable extends Renderable {

    public static final String MAP_SELECT_ACTION = "MAP_SELECT";
    public static final String BG_IMAGE_PATH = "img/episode_map/bg.png";
    public static final String QUIT_BUTTON_IMG_PATH = "img/close.png";
    public static final String CITY_DOT_IMG_PATH = "img/episode_map/city_dot.png";

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

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public MapEpisodeRenderable(float xPos, float yPos, float width, float height, String id, List<Location> lLocations,
                                Location nextAllowedLocation) {
        super(xPos, yPos, width, height, Renderable.ACTOR_EPISODE_MAP_LOCATION, id);
        locations = lLocations;
        this.nextAllowedLocation = nextAllowedLocation;

        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();

        allRenderables.add(getMissionHUD());
        allRenderables.add(getLocationNameHUD());
        allRenderables.add(getDistanceHUD());
        allRenderables.add(getCloseButton());

        allRenderables.addAll(getLocationPoints());
        allRenderables.addAll(getLocationNameLabels());
        allRenderables.add(getBackground());
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
        }

        return closeButton;

    }

    public List<Location> getLocations() {
        return locations;
    }

    public Renderable getMissionHUD() {
        if (missionHUD == null) {
            missionHUD = new TextLabelRenderable(appInfo.convertX(50f),appInfo.convertY(1000f), appInfo.convertX(300), appInfo.convertY(50), Renderable.ACTOR_ROTATABLE_LABEL, "missionHUD");
            missionHUD.setLabel("Mission");
//            missionHUD.setPositionDrawable(false);
            missionHUD.setVisible(false);
        }

        return missionHUD;
    }


    public TextLabelRenderable getLocationNameHUD() {
        if (locationNameHUD == null) {
            locationNameHUD = new TextLabelRenderable(appInfo.convertX(50f),appInfo.convertY(700f), appInfo.convertX(300), appInfo.convertY(50), Renderable.ACTOR_ROTATABLE_LABEL, "locationNameHUD");
            locationNameHUD.setLabel("Location");
//            locationNameHUD.setPositionDrawable(false);
            locationNameHUD.setVisible(false);
        }

        return locationNameHUD;
    }

    public TextLabelRenderable getDistanceHUD() {
        if (distanceHUD == null) {
            distanceHUD = new TextLabelRenderable(appInfo.convertX(50f),appInfo.convertY(400f), appInfo.convertX(300), appInfo.convertY(50), Renderable.ACTOR_ROTATABLE_LABEL, "distanceHUD");
            distanceHUD.setLabel("Distance");
//            distanceHUD.setPositionDrawable(false);
            distanceHUD.setVisible(false);
        }

        return distanceHUD;
    }

    public List<Renderable> getLocationPoints() {
        if (locationPoints == null) {
            locationPoints = new ArrayList<>();

            // For each location (point)
            for (Location lCur : getLocations()) {
                ActionButtonRenderable btn = new ActionButtonRenderable( appInfo.convertX(lCur.getPosX() - 8), appInfo.convertY(lCur.getPosY() - 8),
                        appInfo.convertX(16), appInfo.convertY(16),  Renderable.ACTOR_IMAGE_BUTTON, "nextAllowedPoint");

                btn.setImgPath(CITY_DOT_IMG_PATH);
                btn.setZIndex(5);
                btn.setVisible(false);

                locationPoints.add(btn);

                if (lCur.equals(nextAllowedLocation)) {
                    btn.setUserAction(new UserAction(MAP_SELECT_ACTION, lCur));
                }
            }
        }

        return locationPoints;
    }

    public Renderable getBackground() {
        ImageRenderable ir = new ImageRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), "bg", BG_IMAGE_PATH);
        ir.setZIndex(0);
        ir.setVisible(false);
        return ir;
    }

    public List<Renderable> getLocationNameLabels() {
        if (locationNames == null) {
            List<Renderable> lRes = new ArrayList<>();

            // For each location (name)
            for (Location lCur : getLocations()) {
                // Create corresponding image with effect
                ImageRenderable irCur = new ImageRenderable("NameLabel" + lCur.getName(), lCur.getImgPath());
                irCur.setxPos(lCur.getPosX());
                irCur.setyPos(lCur.getPosY());
                lRes.add(irCur);
            }

            locationNames = lRes;
        }

        return locationNames;
    }

    public void fadeIn() {
        Effect eFadeIn = getDefaultFadeInEffect();
        for (Renderable r: getAllRenderables()) {
            r.addEffect(eFadeIn);
        }
    }

    public void fadeOut() {
        Effect eFadeOut = getDefaultFadeOutEffect();
        for (Renderable r: getAllRenderables()) {
            r.addEffect(eFadeOut);
        }
    }


    public void setNextLocation(Location newLocation) {
        this.locationSelected = true;
        // DEBUG LINES
        System.err.println("Location selected: " + newLocation.toString());
        //////////////
        renderableWasUpdated();
    }

    public EffectSequence getDefaultFadeInEffect() {
        EffectSequence fadeInEffects = new EffectSequence();
        // Add actual fade effect
        fadeInEffects.addEffect(new FadeEffect(1.0, 0.0, 0));
        fadeInEffects.addEffect(new VisibilityEffect(true));
        fadeInEffects.addEffect(new FadeEffect(0.0, 1.0, 2000));
        return fadeInEffects;
    }


    public EffectSequence getDefaultFadeOutEffect() {
        EffectSequence fadeOutEffects = new EffectSequence();
        // Add actual fade effect
        fadeOutEffects.addEffect(new FadeEffect(1.0, 0.0, 2000));
        fadeOutEffects.addEffect(new VisibilityEffect(false));
        return fadeOutEffects;
    }

}
