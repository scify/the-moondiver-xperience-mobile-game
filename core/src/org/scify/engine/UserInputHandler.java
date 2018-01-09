package org.scify.engine;

import com.badlogic.gdx.physics.box2d.ContactListener;

public interface UserInputHandler extends ContactListener {
    UserAction getNextUserAction();
}
