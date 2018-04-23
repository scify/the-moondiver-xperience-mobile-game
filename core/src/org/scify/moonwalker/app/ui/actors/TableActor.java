package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.renderables.MainMenuRenderable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class TableActor<T extends Renderable> extends Table implements IContainerActor<T>, EffectTarget {

    protected ResourceLocator resourceLocator;
    protected AppInfo appInfo;
    protected Image background;
    protected final float TABLES_PADDING_PIXELS = 7;
    protected long timestamp;
    protected Set<Effect> effects;

    protected T renderable;
    protected Map<Actor, Renderable> childrenActorsToRenderables;

    public TableActor(Skin skin, T rRenderable) {
        super(skin);
        appInfo = AppInfo.getInstance();
        resourceLocator = new ResourceLocator();
        childrenActorsToRenderables = new HashMap<>();
        renderable = rRenderable;
        effects  = new HashSet<>();
    }

    public Cell addTextCell(Table table, String labelTxt) {
        Label label = new Label(labelTxt, getSkin());
        return table.add(label).left().expandX();
    }

    public Cell addImageCell(Table table, Texture texture) {
        Image img = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        return table.add(img).left().expandX();
    }

    public Texture imgUrlToTexture(String imgPath) {
        return new Texture(resourceLocator.getFilePath(imgPath));
    }

    protected void initSubTable(Table subTable) {
        subTable.defaults().pad(getDefaultCellPadding());
    }

    protected Cell addButtonCell(Table table, Button button) {
        return table.add(button).bottom().center().width(appInfo.pixelsWithDensity(button.getWidth())).height(appInfo.pixelsWithDensity(button.getHeight()));
    }

    public void addBackground(ImageRenderable imageRenderable, float width, float height) {
        // Remove existing background
        if (background != null)
        {
            removeActor(background);
        }

        // Get new image
        background = ActorFactory.getInstance().createImage(imageRenderable.getImgPath(), imageRenderable);
        // Update size
        background.setSize(width, height);
        // Add to important children
        getChildrenActorsAndRenderables().put(background, imageRenderable);
        // Add it to the table, as an actor
        addActor(background);
    }

    public void addBackground(ImageRenderable imageRenderable) {
        addBackground(imageRenderable, getWidth(), getHeight());
    }

    protected void updateLabelCell(Cell cell, String newValue) {
        Label label = (Label) cell.getActor();
        label.setText(newValue);
    }

    protected void updateImgCell(Cell cell, String newImgPath) {
        Image image = (Image) cell.getActor();
        image.setDrawable(new TextureRegionDrawable(new TextureRegion(imgUrlToTexture(newImgPath))));
    }

    protected float getDefaultCellPadding() {
        return appInfo.pixelsWithDensity(TABLES_PADDING_PIXELS);
    }

    protected void setCellDimensions(Cell cell, Table table, float widthPercentage, int numOfRows) {
        cell.width(table.getWidth() * widthPercentage - getDefaultCellPadding() * 2).height((table.getHeight() / numOfRows) - getDefaultCellPadding() * 2);
    }

    protected float convertHeight(float initialHeight) {
        int initialBackgroundHeight = 1080;
        float ret = getHeight() * initialHeight;
        ret = ret / initialBackgroundHeight;
        return ret;
    }

    protected float convertWidth(float initialWidth) {
        int initialBackgroundWidth = 1920;
        float ret = getWidth() * initialWidth;
        ret = ret / initialBackgroundWidth;
        return ret;
    }

    @Override
    public Map<Actor, Renderable> getChildrenActorsAndRenderables() {
        return childrenActorsToRenderables;
    }

    @Override
    public T getRenderable() {
        return renderable;
    }

    @Override
    public EffectTarget apply(Effect toApply) {
        return toApply.applyTo(this);
    }

    @Override
    public Set<Effect> getEffects() {
        return new HashSet<>(effects);
    }

    @Override
    public void addEffect(Effect effectOfInterest) {
        effects.add(effectOfInterest);
    }

    @Override
    public void removeEffect(Effect eToRemove) {
        effects.remove(eToRemove);
    }
}
