package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import org.scify.engine.renderables.effects.*;
import org.scify.moonwalker.app.game.Location;

import java.util.*;

public class MapEpisodeRenderable extends Renderable {

    public static final String MAP_SELECT_ACTION = "MAP_SELECT";

    public static final String BG_IMAGE_PATH = "img/episode_map/bg.png";
    public static final String QUIT_BUTTON_IMG_PATH = "img/close.png";

    public static final String DESTINATION_HUD_IMG_PATH = "img/episode_map/destination.png";
    public static final String DISTANCE_HUD_IMG_PATH = "img/episode_map/distance.png";
    public static final String MISSION_HUD_IMG_PATH = "img/episode_map/mission.png";

    public static final String CITY_DOT_IMG_PATH = "img/episode_map/city_dot.png";
    public static final String PIN_IMG_PATH = "img/episode_map/pin.png";
    public static final String SPACESHIP_IMG_PATH = "img/episode_map/spaceship.png";
    public static final String STAR_IMG_PATH = "img/episode_map/star_red1.png";


    protected Location currentLocation;
    protected Location nextAllowedLocation;
    protected boolean locationSelected;
    protected List<Location> locations;
    protected List<Renderable> locationPoints;
    protected Map<Location, Renderable> renderablePerLocation;

    protected ActionButtonRenderable closeButton;
    protected TextLabelRenderable missionHUD;

    protected TextLabelRenderable locationNameHUD;
    protected TextLabelRenderable distanceHUD;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public MapEpisodeRenderable(float xPos, float yPos, float width, float height, String id, List<Location> lLocations,
                                Location nextAllowedLocation, Location currentLocation) {
        super(xPos, yPos, width, height, Renderable.ACTOR_EPISODE_MAP_LOCATION, id);
        locations = lLocations;
        this.nextAllowedLocation = nextAllowedLocation;
        this.currentLocation = currentLocation;

        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();

        allRenderables.add(getMissionHUD());
        allRenderables.add(getLocationNameHUD());
        allRenderables.add(getDistanceHUD());
        allRenderables.add(getCloseButton());

        allRenderables.addAll(getHUDLabels());

        allRenderables.addAll(getLocationPoints());
        allRenderables.add(getBackground());
    }

    protected List<Renderable> getHUDLabels() {
        List<Renderable> lRes = new ArrayList<>();

        Renderable rCur;
        // Destination HUD
        rCur = new ImageRenderable("destHUDLbl", DESTINATION_HUD_IMG_PATH);
        rCur.setxPos(appInfo.convertX(85));
        rCur.setyPos(appInfo.convertY(1080 - 85 - 124));
        rCur.setWidth(appInfo.convertX(314));
        rCur.setHeight(appInfo.convertY(124));
        rCur.setZIndex(4);
        rCur.setVisible(false);
        lRes.add(rCur);

        // Distance HUD
        rCur = new ImageRenderable("distHUDLbl", DISTANCE_HUD_IMG_PATH);
        rCur.setxPos(appInfo.convertX(110));
        rCur.setyPos(appInfo.convertY(1080 - 380 - 200));
        rCur.setWidth(appInfo.convertX(246));
        rCur.setHeight(appInfo.convertY(200));
        rCur.setZIndex(4);
        rCur.setVisible(false);
        lRes.add(rCur);

        // Mission HUD
        rCur = new ImageRenderable("missionHUDLbl", MISSION_HUD_IMG_PATH);
        rCur.setxPos(appInfo.convertX(110));
        rCur.setyPos(appInfo.convertY(1080 - 650 - 132));
        rCur.setWidth(appInfo.convertX(246));
        rCur.setHeight(appInfo.convertY(132));
        rCur.setZIndex(4);
        rCur.setVisible(false);
        lRes.add(rCur);


        return lRes;
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

    public TextLabelRenderable getLocationNameHUD() {
        if (locationNameHUD == null) {
            locationNameHUD = new TextLabelRenderable(appInfo.convertX(250f),appInfo.convertY(1080 - 275f), appInfo.convertX(0), appInfo.convertY(0), Renderable.ACTOR_ROTATABLE_LABEL, "locationNameHUD");
            locationNameHUD.setLabel("");
            missionHUD.setZIndex(5);
            locationNameHUD.setVisible(false);
        }

        return locationNameHUD;
    }


    public TextLabelRenderable getDistanceHUD() {
        if (distanceHUD == null) {
            distanceHUD = new TextLabelRenderable(appInfo.convertX(250f),appInfo.convertY(1080 - 525f), appInfo.convertX(0), appInfo.convertY(0), Renderable.ACTOR_ROTATABLE_LABEL, "distanceHUD");
            distanceHUD.setLabel("");
            missionHUD.setZIndex(5);
            distanceHUD.setVisible(false);
        }

        return distanceHUD;
    }

    public Renderable getMissionHUD() {
        if (missionHUD == null) {
            missionHUD = new TextLabelRenderable(appInfo.convertX(250f),appInfo.convertY(1080 - 875f), appInfo.convertX(0), appInfo.convertY(0), Renderable.ACTOR_ROTATABLE_LABEL, "missionHUD");
            missionHUD.setLabel("");
            missionHUD.setZIndex(5);
            missionHUD.setVisible(false);
        }

        return missionHUD;
    }

    public List<Renderable> getLocationPoints() {
        if (locationPoints == null) {
            locationPoints = new ArrayList<>();
            renderablePerLocation = new HashMap<>();

            // For each location (point)
            for (Location lCur : getLocations()) {
                // Create appropriate renderable
                Renderable btn;
                if (lCur.equals(nextAllowedLocation)) {
                    btn = createNextAllowedLocationRenderable(lCur);
                }
                else if (lCur.equals(currentLocation)) {
                    btn = createCurrentLocationRenderable(lCur);
                }
                else {
                    btn = createCityPointRenderable(lCur);
                }

                // Set z-index high (to show on top)
                btn.setZIndex(5);
                // Set invisible (to support fade-in smoothly)
                btn.setVisible(false);

                // Keep mapping between location and renderable
                renderablePerLocation.put(lCur, btn);


                // Add to return list
                locationPoints.add(btn);

            }
        }

        return locationPoints;
    }

    protected ActionButtonRenderable createCityPointRenderable(Location lCur) {
        ActionButtonRenderable btn;
        btn = new ActionButtonRenderable( appInfo.convertX(lCur.getPosX() - 8), appInfo.convertY(lCur.getPosY() - 8),
                appInfo.convertX(16), appInfo.convertY(16),  Renderable.ACTOR_IMAGE_BUTTON, "point" + lCur.getName());
        btn.setImgPath(CITY_DOT_IMG_PATH);
        return btn;
    }

    protected ImageRenderable createCurrentLocationRenderable(Location lCur) {
        ImageRenderable btn;
        btn = new ImageRenderable( appInfo.convertX(lCur.getPosX() - 33), appInfo.convertY(lCur.getPosY() - 72),
                appInfo.convertX(66), appInfo.convertY(36),  "currentPoint", SPACESHIP_IMG_PATH);
        btn.setImgPath(SPACESHIP_IMG_PATH);

        EffectSequence esCur = new EffectSequence();
        esCur.addEffect(new DelayEffect(2000));
        RotateEffect rEffect = new RotateEffect(0.0, 360.0, 2000);
        rEffect.setOriginPoint(16, 16);
        esCur.addEffect(rEffect);
        btn.addEffect(esCur);

        return btn;
    }

    protected ActionButtonRenderable createNextAllowedLocationRenderable(Location lCur) {
        ActionButtonRenderable btn;
        btn = new ActionButtonRenderable( appInfo.convertX(lCur.getPosX() - 45), appInfo.convertY(lCur.getPosY() + 5),
                appInfo.convertX(40), appInfo.convertY(60),  Renderable.ACTOR_IMAGE_BUTTON, "nextAllowedPoint");
        btn.setImgPath(PIN_IMG_PATH);

        btn.setUserAction(new UserAction(MAP_SELECT_ACTION, lCur));

        // and highlighted
        EffectSequence eConstant = new EffectSequence();
        eConstant.addEffect(new DelayEffect(3000)); // Await normal fade in
        eConstant.addEffect(new BounceEffect(0, 20, 2000));
        btn.addEffect(eConstant);

        return btn;
    }

    public Renderable getBackground() {
        ImageRenderable ir = new ImageRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), "bg", BG_IMAGE_PATH);
        ir.setZIndex(0);
        ir.setVisible(false);
        return ir;
    }

//    public void fadeIn(Runnable rAfterFadeIn) {
    public void fadeIn() {
        // Apply to all except close button (see below)
        EffectSequence eFadeIn = getDefaultFadeInEffect();
        for (Renderable rCur: getAllRenderables()) {
                rCur.addEffect(eFadeIn);
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

    public Renderable getRenderableForLocation(Location lToLoopUp) {
        return renderablePerLocation.get(lToLoopUp);
    }

}
