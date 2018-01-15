package org.scify.moonwalker.app.rules;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import org.scify.engine.Rules;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;

public abstract class PhysicsRules implements Rules, ContactListener {

    protected int wordX, worldY;

    public PhysicsRules(int worldX, int worldY) {
        this.wordX = worldX;
        this.worldY = worldY;
    }

    @Override
    public GameState getInitialState() {
        return null;
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        return gsCurrent;
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return false;
    }

    @Override
    public boolean isGamePaused(GameState gsCurrent) {
        return false;
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
