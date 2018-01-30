package org.scify.moonwalker.app.ui.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.scify.engine.Renderable;
import org.scify.engine.UserAction;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.game.quiz.Answer;
import org.scify.moonwalker.app.helpers.GameInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserInputHandlerImpl extends ChangeListener implements UserInputHandler {

    /**
     * List of actions captured by the user interaction. User in the Player-derived methods.
     */
    private List<UserAction> pendingUserActions = Collections.synchronizedList(new ArrayList<UserAction>());
    protected GameInfo gameInfo;

    public UserInputHandlerImpl() {
        gameInfo = GameInfo.getInstance();
    }

    private void listenForUserInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pendingUserActions.add(new UserAction(UserActionCode.LEFT));
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            pendingUserActions.add(new UserAction(UserActionCode.RIGHT));
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            pendingUserActions.add(new UserAction(UserActionCode.UP));
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            pendingUserActions.add(new UserAction(UserActionCode.DOWN));
        }

        // Touch screen listeners
        if(Gdx.input.justTouched()) {
            //touchInputs();
        }
    }

    protected void touchInputs() {
        if(Gdx.input.getX() < gameInfo.getScreenWidth() / 2f){
            pendingUserActions.add(new UserAction(UserActionCode.LEFT));
        }
        if(Gdx.input.getX() > gameInfo.getScreenWidth() / 2f){
            pendingUserActions.add(new UserAction(UserActionCode.RIGHT));
        }
        if(Gdx.input.getY() > gameInfo.getScreenHeight() / 2f){
            pendingUserActions.add(new UserAction(UserActionCode.UP));
        }
        if(Gdx.input.getY() < gameInfo.getScreenHeight() / 2f){
            pendingUserActions.add(new UserAction(UserActionCode.DOWN));
        }
    }

    @Override
    public UserAction getNextUserAction() {
        listenForUserInput();
        org.scify.engine.UserAction toReturn = null;
        if(!pendingUserActions.isEmpty()) {
            toReturn = pendingUserActions.get(0);
            pendingUserActions.remove(0);
        }
        return toReturn;
    }

    @Override
    public void addUserActionForRenderable(Renderable renderable, Object payload) {
        if(payload instanceof Answer)
            pendingUserActions.add(new UserAction(UserActionCode.ANSWER_SELECTION, payload));
        else
            pendingUserActions.add(new UserAction(UserActionCode.ANSWER_TEXT, payload));
    }

    public void addUserAction(UserAction userAction) {
        pendingUserActions.add(userAction);
    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void disposeResources() {

    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        pendingUserActions.add(new UserAction(UserActionCode.BUTTON_PRESSED, actor));
    }
}
