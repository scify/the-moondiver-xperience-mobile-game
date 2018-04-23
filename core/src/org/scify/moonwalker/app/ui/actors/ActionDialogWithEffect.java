package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;
import org.scify.moonwalker.app.helpers.AppInfo;

import java.util.HashSet;
import java.util.Set;

public class ActionDialogWithEffect extends Renderable implements EffectTarget {

    protected Dialog dialog;
    protected Skin skin;
    protected String title;
    protected AppInfo appInfo;
    private ActionDialogWithEffect instance = this;
    protected Set<Effect> effects;

    public ActionDialogWithEffect(float xPos, float yPos, float width, float height, String title, String bodyText, Skin sSkin) {
        super(xPos, yPos, width, height, "dialog", title);
        appInfo = AppInfo.getInstance();
        this.skin = sSkin;
        this.title = title;
        dialog = new Dialog(title, skin, "dialog") {
            public void result(Object obj) {
                // TODO add inputHandler execute here
                if (userInputHandler != null)
                    userInputHandler.addUserActionForRenderable(instance, obj);
            }
        };
        dialog.setWidth(width);
        dialog.setPosition(xPos - width / 2f, yPos - height / 2f);
        dialog.text(bodyText);

        effects = new HashSet<>();
    }

    public void addButton(String buttonTitle, Object buttonProps) {
        dialog.button(buttonTitle, buttonProps);
    }

    public Dialog getDialog() {
        return dialog;
    }

    @Override
    public EffectTarget apply(Effect toApply) {
        return toApply.applyTo(this);
    }

    @Override
    public Set<Effect> getEffects() {
        return new HashSet<>(effects);
    }

    @Override
    public void addEffect(Effect effectOfInterest) {
        effects.add(effectOfInterest);
    }

    @Override
    public void removeEffect(Effect eToRemove) {
        effects.remove(eToRemove);
    }
}
