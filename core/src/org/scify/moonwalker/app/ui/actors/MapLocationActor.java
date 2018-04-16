package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.scify.moonwalker.app.ui.renderables.MapLocationRenderable;

public class MapLocationActor extends TableActor {

    protected MapLocationRenderable renderable;

    public MapLocationActor(Skin skin, MapLocationRenderable renderable, Button button) {
        super(skin, renderable);
        this.renderable = renderable;
        timestamp = renderable.getRenderableLastUpdated();
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getImgPath());
        initSubTable(this);
        addTextCell(this, renderable.getDestinationName()).top();
        row();
        addButtonCell(this, button);
        row();
        addTextCell(this, String.valueOf(renderable.getDestinationDistance()) + " km").bottom();

        //debug();
    }

    @Override
    public Cell addTextCell(Table table, String labelTxt) {
        Label label = new Label(labelTxt, getSkin());
        return table.add(label).expand();
    }
}
