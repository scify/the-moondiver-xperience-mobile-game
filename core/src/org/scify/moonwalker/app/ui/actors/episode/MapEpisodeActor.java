package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.*;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.ui.LGDXRenderableBookKeeper;
import org.scify.moonwalker.app.ui.actors.ActorLabelWithEffect;
import org.scify.moonwalker.app.ui.actors.ActorWithEffects;
import org.scify.moonwalker.app.ui.actors.StackWithEffect;
import org.scify.moonwalker.app.ui.actors.Updateable;
import org.scify.moonwalker.app.ui.renderables.MapEpisodeRenderable;

public class MapEpisodeActor extends ActorWithEffects implements Updateable<MapEpisodeRenderable> {

    protected AppInfo appInfo = AppInfo.getInstance();
    protected LGDXRenderableBookKeeper bookKeeper = LGDXRenderableBookKeeper.getInstance();
    protected MapEpisodeRenderable renderable;
    protected boolean bSelectedOnce = false;


    public MapEpisodeActor(Skin skin, MapEpisodeRenderable rRenderable) {
        super();
        renderable = rRenderable;

    }


    private ActorLabelWithEffect getMissionHUD() {
        return (ActorLabelWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getMissionHUD());
    }

    private ActorLabelWithEffect getDistanceHUD() {
        return (ActorLabelWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getDistanceHUD());
    }

    private ActorLabelWithEffect getLocationNameHUD() {
        return (ActorLabelWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getLocationNameHUD());
    }

    @Override
    public void update(final MapEpisodeRenderable renderable) {
//        // If location is selected
        if (renderable.isLocationSelected() && !bSelectedOnce) {
            bSelectedOnce = true;
            Location lTarget = renderable.getTargetLocation();

            // Update HUD
            final ActorLabelWithEffect locationName = getLocationNameHUD();
            locationName.setAlignment(Align.center);
            renderable.getLocationNameHUD().setLabel(lTarget.getName());
            // Update distance
            final ActorLabelWithEffect distance = getDistanceHUD();
            distance.setAlignment(Align.center);
            renderable.getDistanceHUD().setLabel(String.valueOf(lTarget.getDistanceFromLocation(renderable.getOriginLocation())) + " Km");
            //Update Mission
            final ActorLabelWithEffect mission = getMissionHUD();
            mission.setAlignment(Align.center);
            renderable.getMissionHUD().setLabel(lTarget.getMission());

            if (!renderable.isTravelOnly()) {
                renderable.getLocationNameHUD().addEffect(getFadeInSequence(0.0));
                renderable.getDistanceHUD().addEffect(getFadeInSequence(1000.0));
                renderable.getMissionHUD().addEffect(getFadeInSequence(2000.0));
            }
        }
        renderable.wasUpdated();
    }

    protected EffectSequence getFadeInSequence(double dInitialDelay) {
        EffectSequence effectSequence = new EffectSequence();
        effectSequence.addEffect(new FadeEffect(1, 0, 0));
        effectSequence.addEffect(new VisibilityEffect(true));
        effectSequence.addEffect(new DelayEffect(dInitialDelay));
        effectSequence.addEffect(new FadeEffect(0, 1, 1000));

        return effectSequence;
    }
}
