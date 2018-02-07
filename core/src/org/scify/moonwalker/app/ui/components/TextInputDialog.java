package org.scify.moonwalker.app.ui.components;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import org.scify.moonwalker.app.game.quiz.Question;

import java.util.HashMap;

public class TextInputDialog extends ActionDialog {

    public TextInputDialog(float xPos, float yPos, float width, float height, String title, String bodyText, Skin sKin) {
        super(xPos, yPos, width, height, title, bodyText, sKin);
    }

    public void createForQuestion(Question question, Stage stage) {
        this.setUserInputHandler(userInputHandler);
        TextField textField = new TextField("", skin);
        Table table = new Table();
        table.setSkin(skin);
        table.setFillParent(true);
        stage.addActor(table);
        table.add(question.getBody()).padBottom(10f);
        table.row();
        table.add(textField).padBottom(10f);
        table.row();
        this.getDialog().addActor(table);
        this.addButton("OK", new HashMap.SimpleEntry<>(question, textField));
        stage.addActor(this.getDialog());
    }
}
