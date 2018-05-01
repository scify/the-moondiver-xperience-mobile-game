package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.ui.LGDXRenderableBookKeeper;
import org.scify.moonwalker.app.ui.renderables.MapEpisodeRenderable;

public class MapEpisodeActor extends ActorWithEffects implements Updateable<MapEpisodeRenderable> {

    protected AppInfo appInfo = AppInfo.getInstance();
    protected LGDXRenderableBookKeeper bookKeeper = LGDXRenderableBookKeeper.getInstance();
    protected MapEpisodeRenderable renderable;


    public MapEpisodeActor(Skin skin, MapEpisodeRenderable rRenderable) {
        super();
        renderable = rRenderable;
    }


    private Label getMissionHUD() {
        return (Label)((StackWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getMissionHUD())).getChildren().get(0);
    }

    private Label getDistanceHUD() {
        return (Label)((StackWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getDistanceHUD())).getChildren().get(0);
    }

    private Label getLocationNameHUD() {
        return (Label)((StackWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getLocationNameHUD())).getChildren().get(0);
    }

    @Override
    public void update(MapEpisodeRenderable renderable) {
        // If location is selected
        if (renderable.isLocationSelected()) {
            Location lTarget = renderable.getNextAllowedLocation();
            // Update HUD
            getLocationNameHUD().setText(lTarget.getName());
            // Update distance
            getDistanceHUD().setText(String.valueOf(lTarget.getDistanceFromLocation(renderable.getCurrentLocation())));
            // Update mission
            getMissionHUD().setText(lTarget.getMission());
        }
    }
}
