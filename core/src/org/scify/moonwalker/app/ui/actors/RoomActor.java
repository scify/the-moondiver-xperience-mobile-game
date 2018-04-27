package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.VisibilityEffect;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

public class RoomActor extends TableActor<RoomRenderable> implements Updateable {

    protected RoomRenderable renderable;

    protected ImageWithEffect phoneOffImage;
    protected ImageWithEffect phoneOnImage;

    public RoomActor(Skin skin, RoomRenderable renderable) {
        super(skin, renderable);
        this.renderable = renderable;
        timestamp = this.renderable.getRenderableLastUpdated();
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
        init();
    }

    public void init() {
        float screenHeight = getHeight();
        float screenWidth = getWidth();
        phoneOffImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getPhoneOffRenderable());
        phoneOnImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getPhoneOnRenderable());
        Stack phonesStack = new Stack();
        phonesStack.add(phoneOffImage);
        phonesStack.add(phoneOnImage);
        add(phonesStack).width(convertWidth(phoneOffImage.getWidth())).height(convertHeight(phoneOffImage.getHeight()));
        getChildrenActorsAndRenderables().put(phoneOffImage,renderable.getPhoneOffRenderable());
        getChildrenActorsAndRenderables().put(phoneOnImage,renderable.getPhoneOnRenderable());

        EffectSequence fadeInEffects = new EffectSequence();
        fadeInEffects.addEffect(new FadeEffect(1.0, 0.0, 0));
        fadeInEffects.addEffect(new VisibilityEffect(true));
        fadeInEffects.addEffect(new FadeEffect(0.0, 1.0, 2000));
        fadeInEffects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                renderable.enableChat();
            }
        }));
        renderable.addEffect(fadeInEffects);
    }

    @Override
    public void update(Renderable renderable) {
        if (this.renderable.getRenderableLastUpdated() > timestamp) {
            this.renderable = (RoomRenderable) renderable;
            this.timestamp = this.renderable.getRenderableLastUpdated();
        }
    }
}
