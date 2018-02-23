package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.engine.Renderable;
import org.scify.moonwalker.app.ui.actors.CockpitActor;
import org.scify.moonwalker.app.ui.actors.calculator.CalculatorComponent;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

public class ActorFactory extends ComponentFactory{

    public ActorFactory(Skin skin) {
        super(skin);
    }

    public Actor createResourceForType(Renderable renderable) {
        Actor toReturn;
        switch (renderable.getType()) {
            case "label":
                Label label = new Label("", skin);
                label.setWidth(renderable.getWidth());
                label.setHeight(renderable.getHeight());
                label.setWrap(true);
                toReturn = label;
                break;
            case "yoda":
                toReturn = createImage("img/yoda.png", renderable);
                break;
            case "player":
                toReturn = createImage("img/player.png", renderable);
                break;
            case "boy":
                toReturn = createImage("img/boy.png", renderable);
                break;
            case "girl":
                toReturn = createImage("img/girl.png", renderable);
                break;
            case "image":
                toReturn = createImage(renderable.getImgPath(), renderable);
                break;
            case "cockpit":
                toReturn = createCockpitActor((CockpitRenderable) renderable);
                break;
            case "text_button":
                toReturn = createTextButton((ActionButton) renderable);
                break;
            case "image_button":
                toReturn = createImageButton((ActionButton) renderable);
                break;
            case "calculator":
                toReturn = new CalculatorComponent(skin);
                break;
            default:
                throw new UnsupportedRenderableTypeException("renderable with type " + renderable.getType() + " is unsupported.");
        }
        return toReturn;
    }

    protected Image createImage(String imgFileRelevantPath, Renderable renderable) {
        Image img = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath(imgFileRelevantPath)))));
        img.setSize(renderable.getWidth(), renderable.getHeight());
        return img;
    }

    protected TextButton createTextButton(ActionButton actionButton) {
        TextButton btn = new TextButton(actionButton.getTitle(), skin);
        setButtonDimensions(actionButton, btn);
        btn.setName(actionButton.getId());
        return btn;
    }

    protected ImageButton createImageButton(ActionButton actionButton) {
        Drawable btnImage = new SpriteDrawable(new Sprite(new Texture(resourceLocator.getFilePath(actionButton.getImgPath()))));
        ImageButton btn = new ImageButton(btnImage);
        setButtonDimensions(actionButton, btn);
        btn.setName(actionButton.getId());
        return btn;
    }

    protected void setButtonDimensions(ActionButton actionButton, Button btn) {
        btn.setPosition(actionButton.getxPos(), actionButton.getyPos());
        if(actionButton.getWidth() != 0)
            btn.setWidth(actionButton.getWidth());
        if(actionButton.getHeight() != 0)
            btn.setHeight(actionButton.getHeight());
        btn.pad(actionButton.getPadding());
    }

    private Actor createCockpitActor(CockpitRenderable renderable) {
        CockpitActor actor = new CockpitActor(skin, renderable.getWidth(), renderable.getHeight());
        actor.setPosition(renderable.getxPos(), renderable.getyPos());
        actor.addBackground(renderable.getImgPath());
        actor.addInfoTable(renderable.ENGINE_PERFORMANCE_LABEL, renderable.getEnginePerformanceValue(),
                renderable.REMAINING_ENERGY_LABEL, renderable.getRemainingEnergyValue(), renderable.DESTINATION_DISTANCE_LABEL, renderable.getDestinationDistanceValue());
        actor.addMiddleTable(createImageButton(renderable.getNavigationButton()), renderable.POSITION_LABEL, renderable.getPositionValue());
        actor.addActionsTable(createImageButton(renderable.getVesselButton()), createImageButton(renderable.getMapButton()), createImageButton(renderable.getContactButton()));
        return actor;
    }
}
