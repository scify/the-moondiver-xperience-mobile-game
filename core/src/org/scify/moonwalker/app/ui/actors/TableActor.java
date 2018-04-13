package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;

public abstract class TableActor extends Table {

    protected ResourceLocator resourceLocator;
    protected AppInfo appInfo;
    protected Image background;
    protected final float TABLES_PADDING_PIXELS = 7;
    protected long timestamp;

    public TableActor(Skin skin) {
        super(skin);
        appInfo = AppInfo.getInstance();
        resourceLocator = new ResourceLocator();
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

    public void addBackground(String imgPath) {
        background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath(imgPath)))));
        background.setSize(getWidth(), getHeight());
        addActor(background);
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
}
