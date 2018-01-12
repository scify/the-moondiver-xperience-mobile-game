package org.scify.moonwalker.app.rules;

import org.scify.engine.GameEvent;
import org.scify.engine.GameState;
import org.scify.engine.Positionable;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.actors.Cloud;
import org.scify.moonwalker.app.actors.MoonWalkerPlayer;
import org.scify.moonwalker.app.game.quiz.Answer;
import org.scify.moonwalker.app.game.quiz.Question;
import org.scify.moonwalker.app.game.quiz.QuestionType;
import org.scify.moonwalker.app.helpers.GameInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SinglePlayerRules extends MoonWalkerRules {

    protected MoonWalkerPlayer pPlayer;
    protected Cloud cCloud;
    protected List<Positionable> lClouds;
    protected MoonWalkerPhysicsRules physics;
    protected GameInfo gameInfo;

    public SinglePlayerRules() {
        gameInfo = GameInfo.getInstance();
        worldX = gameInfo.getScreenWidth();
        worldY = gameInfo.getScreenHeight();
        pPlayer = new MoonWalkerPlayer("Paul", worldX / 2f,  worldY / 2f);
        pPlayer.setLives(5);
        pPlayer.setScore(0);
        cCloud = new Cloud(0, 10);
        lClouds = new ArrayList<>();
        physics = new MoonWalkerPhysicsRules(worldX, worldY);
    }

    @Override
    public GameState getInitialState() {
        List<GameEvent> eventQueue = Collections.synchronizedList(new LinkedList<GameEvent>());
        return new MoonWalkerGameState(eventQueue, pPlayer, physics.world);
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        gsCurrent = physics.getNextState(gsCurrent, userAction);
        handlePositionEvents(gsCurrent);
        return gsCurrent;
    }

    protected void handlePositionEvents(GameState gameState) {
        if(gameState.eventsQueueContainsEvent("PLAYER_BOTTOM_BORDER")) {
            if (!gameState.eventsQueueContainsEvent("QUESTION_1")) {
                gameState.getEventQueue().add(new GameEvent("QUESTION_1"));
                // add dialog object in game event
                gameState.getEventQueue().add(new GameEvent("QUESTION_UI", createSelectionQuestion((MoonWalkerGameState) gameState)));
                gameState.getEventQueue().add(new GameEvent("PAUSE_GAME"));
            }
        }
        if(gameState.eventsQueueContainsEvent("PLAYER_LEFT_BORDER")) {
            if (!gameState.eventsQueueContainsEvent("QUESTION_2")) {
                gameState.getEventQueue().add(new GameEvent("QUESTION_2"));
                // add dialog object in game event
                gameState.getEventQueue().add(new GameEvent("QUESTION_UI", createTextQuestion((MoonWalkerGameState) gameState)));
                gameState.getEventQueue().add(new GameEvent("PAUSE_GAME"));
            }
        }
    }

    protected Question createTextQuestion(MoonWalkerGameState gameState) {
        List answers = new LinkedList();
        answers.add(new Answer("6", true));
        answers.add(new Answer("Έξι", true));
        answers.add(new Answer("Έξι", true));
        answers.add(new Answer("Six", true));
        Collections.shuffle(answers);
        return new Question(
                "Πόσες φορές μικρότερή είναι η βαρύτητα στη Σελήνη \nσε σχέση με τη Γή;",
                answers,
                QuestionType.FREE_TEXT
        );
    }

    protected Question createSelectionQuestion(MoonWalkerGameState gameState) {
        List answers = new LinkedList();
        answers.add(new Answer("Λούνα", true));
        answers.add(new Answer("Apollo"));
        answers.add(new Answer("NASA"));
        answers.add(new Answer("Eclipse"));
        Collections.shuffle(answers);
        return new Question(
                "Πώς λεγόταν το πρόγραμμα της Σοβιετικής \nΈνωσης που έφτασε στο φεγγάρι το 1959;",
                answers,
                QuestionType.MULTIPLE_CHOICE
        );
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return false;
    }
}
