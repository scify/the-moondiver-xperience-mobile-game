package org.scify.moonwalker.app.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import org.scify.moonwalker.app.helpers.GameInfo;

public class GameHUD {
    private int score, lives;
    private Label scoreLabel, livesLabel;
    private Image scoreImg, livesImg;
    private Sprite scoreSprite;
    private Sprite livesSprite;
    private Table scoreTable;
    private Table livesTable;
    private GameInfo gameInfo;

    public GameHUD(Skin skin, BitmapFont font) {
        gameInfo = GameInfo.getInstance();
        BitmapFont hudFont = font;
        hudFont.getData().setScale((float)1.2, (float)1.2);
        scoreSprite = new Sprite(new Texture("img/coin.png"));
        scoreSprite.setSize((float) (gameInfo.getScreenWidth() * 0.1), (float) (gameInfo.getScreenWidth() * 0.1));
        scoreImg = new Image(new SpriteDrawable(scoreSprite));
        scoreLabel = new Label("x" + getScore(), new Label.LabelStyle(hudFont, Color.BLACK));
        scoreTable = new Table(skin);
        scoreTable.setFillParent(true);
        scoreTable.top().left();
        scoreTable.add(scoreImg).padLeft(5).padTop(5);
        scoreTable.add(scoreLabel).padLeft(5);
        scoreTable.row();

        livesSprite = new Sprite(new Texture("img/heart.png"));
        livesSprite.setSize((float) (gameInfo.getScreenWidth() * 0.1), (float) (gameInfo.getScreenWidth() * 0.1));
        livesImg = new Image(new SpriteDrawable(livesSprite));
        livesLabel = new Label("x" + getLives(), new Label.LabelStyle(hudFont, Color.BLACK));
        livesTable = new Table();
        livesTable.setFillParent(true);
        livesTable.top().right();
        livesTable.add(livesImg).padRight(5).padTop(5);
        livesTable.row();
        livesTable.add(livesLabel).padRight(5);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        this.scoreLabel.setText("x" + getScore());
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
        this.livesLabel.setText("x" + getLives());
    }

    public Table getScoreTable() {
        return scoreTable;
    }

    public Table getLivesTable() {
        return livesTable;
    }
}
