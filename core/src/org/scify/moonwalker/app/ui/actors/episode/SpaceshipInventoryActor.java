package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import org.scify.moonwalker.app.ui.ThemeController;
import org.scify.moonwalker.app.ui.actors.FadingTableActor;
import org.scify.moonwalker.app.ui.actors.ImageWithEffect;
import org.scify.moonwalker.app.ui.renderables.SpaceshipInventoryRenderable;

public class SpaceshipInventoryActor extends FadingTableActor<SpaceshipInventoryRenderable> {

    public SpaceshipInventoryActor(Skin skin, SpaceshipInventoryRenderable renderable) {
        super(skin, renderable);
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
        init();
    }

    protected void init() {
        float screenHeight = getHeight();
        float screenWidth = getWidth();
        center();
        createLeftColumn(screenWidth, screenHeight, 0.3f * screenWidth);
        add().width(0.4f * screenWidth).height(screenHeight);
        createRightColumn(screenWidth, screenHeight, 0.3f * screenWidth);
    }

    protected void createLeftColumn(float screenWidth, float screenHeight, float columnWidth) {
        Image image = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getMoonPhase());
        float ratio = image.getHeight() / image.getWidth();
        float imageWidth = 0.10f * screenWidth;
        float imageHeight = ratio * imageWidth;
        Table table = new Table();
        table.add().width(columnWidth).height(0.45f * screenHeight);
        table.row();
        table.add(image).width(imageWidth).height(imageHeight);
        table.row();

        Label label = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getUnitsLabel());
        label.setAlignment(Align.center);
        Label.LabelStyle ls = new Label.LabelStyle();
        ThemeController themeController = ThemeController.getInstance();
        ls.font = themeController.getFont();
        ls.fontColor = Color.valueOf("e8ffff");
        label.setStyle(ls);
        table.add(label);
        table.row();
        table.add().expand();

        add(table).width(columnWidth).height(screenHeight);
    }

    protected void createRightColumn(float screenWidth, float screenHeight, float columnWidth) {
        Table table = new Table();
        table.top();

        table.add().width(columnWidth).height(0.6f * screenHeight);

        table.row();
        Label label = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getDistancePerUnitLabel());
        label.setAlignment(Align.center);
        Label.LabelStyle ls = new Label.LabelStyle();
        ThemeController themeController = ThemeController.getInstance();
        ls.font = themeController.getFont();
        ls.fontColor = Color.valueOf("e8ffff");
        label.setStyle(ls);
        table.add(label);

        table.row();
        table.add().expand();

        add(table).width(columnWidth).height(screenHeight);

    }
}
