package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.ui.LGDXRenderableBookKeeper;
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
        if (renderable.isLocationSelected() && !bSelectedOnce) {
            bSelectedOnce = true;
            Location lTarget = renderable.getTargetLocation();
            // Update HUD
            Label lCur = getLocationNameHUD();
            lCur.setAlignment(Align.center);
            lCur.setText(lTarget.getName());
            centerRenderableByActorSize(renderable.getLocationNameHUD(), lCur);
            // Update distance
            lCur = getDistanceHUD();
            lCur.setAlignment(Align.center);
            lCur.setText(String.valueOf(lTarget.getDistanceFromLocation(renderable.getOriginLocation())) + "KM");
            centerRenderableByActorSize(renderable.getDistanceHUD(), lCur);
            // Update mission
            lCur = getMissionHUD();
            lCur.setAlignment(Align.center);
            lCur.setText(lTarget.getMission());
            centerRenderableByActorSize(renderable.getMissionHUD(), lCur);
        }
    }

    protected void centerRenderableByActorSize(Renderable rToMove, Label lLabel) {
//        double dNewX = rToMove.getxPos() - lLabel.getWidth() / 2.0;
//        double dNewY = rToMove.getyPos() + lLabel.getHeight() / 2.0;
//
//        rToMove.setxPos((float) dNewX);
//        rToMove.setyPos((float) dNewY);
//        rToMove.markAsNeedsUpdate();
    }
}
