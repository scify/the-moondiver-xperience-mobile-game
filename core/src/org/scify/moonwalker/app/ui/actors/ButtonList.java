package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;

public class ButtonList extends Table{

    protected Label label;
    protected GameInfo gameInfo;
    protected final float MAIN_LABEL_PADDING_PIXELS = 12;
    protected final float BUTTON_PADDING_PIXELS = 10;
    protected boolean isVertical;
    protected int columnsNum;

    public ButtonList(Skin skin, boolean isVertical) {
        super(skin);
        this.gameInfo = GameInfo.getInstance();
        this.isVertical = isVertical;
        setFillParent(true);
        center().center();
    }

    public void setColumnsNum(int columnsNum) {
        this.columnsNum = columnsNum;
    }

    public void addMainLabel(String labelTxt) {
        label = new Label(labelTxt, getSkin());
        label.setFontScale(2);
        label.setAlignment(Align.center);
        Cell labelCell = add(label).pad(gameInfo.pixelsWithDensity(MAIN_LABEL_PADDING_PIXELS));
        if(columnsNum != 0)
            labelCell.colspan(columnsNum);
        labelCell.row();
    }

    public void addButton(Actor button, UserInputHandlerImpl userInputHandler) {
        button.addListener(userInputHandler);
        Cell buttonCell = add(button).pad(gameInfo.pixelsWithDensity(BUTTON_PADDING_PIXELS)).height(button.getHeight());
        if(isVertical) {
            buttonCell.width(button.getWidth());
            row();
        } else {
            buttonCell.width(gameInfo.getScreenWidth() / columnsNum).center().expand();
        }
    }

    public void setImageButtonsGreyedOutExcept(Actor clickedActor) {
        for(Cell cell : this.getCells()) {
            if(cell.getActor().getClass() == ImageButton.class) {
                if(cell.getActor().getName() != clickedActor.getName())
                    setColorToImageButton((ImageButton) cell.getActor(), Color.DARK_GRAY);
                else
                    setColorToImageButton((ImageButton) cell.getActor(), Color.WHITE);
            }
        }
    }

    public void setColorToImageButton(ImageButton imageButton, Color color) {
        imageButton.getImage().setColor(color);
    }
}
