package org.scify.moonwalker.app.ui.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.scify.engine.UserAction;
import org.scify.engine.UserInputHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserInputHandlerImpl implements UserInputHandler, ContactListener {

    /**
     * List of actions captured by the user interaction. User in the Player-derived methods.
     */
    List<UserAction> pendingUserActions = Collections.synchronizedList(new ArrayList<UserAction>());

    public UserInputHandlerImpl() {
    }

    private void listenForUserInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pendingUserActions.add(new org.scify.engine.UserAction(org.scify.moonwalker.app.ui.input.UserActionCode.LEFT));
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            pendingUserActions.add(new org.scify.engine.UserAction(org.scify.moonwalker.app.ui.input.UserActionCode.RIGHT));
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            pendingUserActions.add(new org.scify.engine.UserAction(org.scify.moonwalker.app.ui.input.UserActionCode.UP));
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            pendingUserActions.add(new org.scify.engine.UserAction(org.scify.moonwalker.app.ui.input.UserActionCode.DOWN));
        }
    }

    @Override
    public org.scify.engine.UserAction getNextUserAction() {
        listenForUserInput();
        org.scify.engine.UserAction toReturn = null;
        if(!pendingUserActions.isEmpty()) {
            toReturn = pendingUserActions.get(0);
            pendingUserActions.remove(0);
        }
        return toReturn;
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
}
