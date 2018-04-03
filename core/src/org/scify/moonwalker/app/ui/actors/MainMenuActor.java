package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.ui.renderables.MainMenuRenderable;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

public class MainMenuActor extends TableActor implements Updateable {

    protected AppInfo appInfo;
    protected MainMenuRenderable renderable;

    protected Button startButton;
    protected Button continueButton;
    protected Button toggleAudioButton;
    protected Button aboutButton;
    protected Button quitButton;


    public MainMenuActor(Skin skin, MainMenuRenderable renderable) {
        super(skin);
        this.renderable = renderable;
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        appInfo = AppInfo.getInstance();
        //addBackground(renderable.getImgPath());
    }

    public void init() {
        float screenHeight = getHeight();
        float screenWidth = getWidth();
        top();
        add().colspan(3).width(screenWidth).height(screenHeight * 0.5f);
        row();
        add().width(screenWidth * 0.3f);
        add(addMenuButtons());
        add().width(screenWidth * 0.3f);
        debug();
    }

    protected Table addMenuButtons () {
        Table ret = new Table();
        ret.defaults();
        ret.bottom();
        float screenHeight = getHeight();
        float screenWidth = getWidth();
        float width = 0.3f * screenWidth;
        float height = 0.1f * screenHeight;
        ret.add(startButton).width(width).height(height);
        ret.row();
        ret.add(continueButton).width(width).height(height);
        ret.row();
        ret.add(toggleAudioButton).width(width).height(height);
        ret.row();
        ret.add(aboutButton).width(width).height(height);
        ret.row();
        ret.add(quitButton).width(width).height(height);
        return ret;
    }

    public void setImageButtonsGreyedOutExcept(String selectedButtonId) {
        for (Cell cell : this.getCells()) {
            if (cell.getActor().getClass() == ImageButton.class) {
                if (cell.getActor().getName() != selectedButtonId)
                    setColorToImageButton((ImageButton) cell.getActor(), Color.DARK_GRAY);
                else
                    setColorToImageButton((ImageButton) cell.getActor(), Color.WHITE);
            }
        }
    }

    public void setColorToImageButton(ImageButton imageButton, Color color) {
        imageButton.getImage().setColor(color);
    }

    @Override
    public void update(Renderable renderable) {
        if (this.renderable.getRenderableLastUpdated() > timestamp) {
            this.renderable = (MainMenuRenderable) renderable;
            this.timestamp = this.renderable.getRenderableLastUpdated();
            // setImageButtonsGreyedOutExcept(this.renderable.getSelectedAvatar().getId());
        }
    }

    public void setStartButton(Button button) { startButton = button;}

    public void setContinueButton(Button button) { continueButton = button;}

    public void setToggleAudioButton(Button button) { toggleAudioButton = button;}

    public void setAboutButton(Button button) { aboutButton = button;}

    public void setQuitButton(Button button) { quitButton = button;}
}
