package org.scify.engine;

import com.badlogic.gdx.physics.box2d.ContactListener;
import org.scify.engine.renderables.Renderable;

public interface UserInputHandler extends ContactListener {
    UserAction getNextUserAction();
    void addUserActionForRenderable(Renderable r, Object payload);
    void disposeResources();
    void addUserAction(UserAction userAction);
}
