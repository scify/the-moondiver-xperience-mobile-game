package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.libgdx.FadeLGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffectList;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

public class RoomActor extends TableActor<RoomRenderable> implements Updateable {

    protected RoomRenderable renderable;

    protected Image phoneOffImage;
    protected Image phoneOnImage;

    protected ActorFactory factory;

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
        // Get actor factory
        factory = ActorFactory.getInstance();
        float screenHeight = getHeight();
        float screenWidth = getWidth();
        phoneOffImage = (Image) factory.createResourceForType(renderable.getPhoneOffRenderable());
        phoneOnImage = (Image) factory.createResourceForType(renderable.getPhoneOnRenderable());
        Stack phonesStack = new Stack();
        phonesStack.add(phoneOffImage);
        phonesStack.add(phoneOnImage);
        add(phonesStack).width(convertWidth(phoneOffImage.getWidth())).height(convertHeight(phoneOffImage.getHeight()));
        getChildrenActorsAndRenderables().put(phoneOffImage,renderable.getPhoneOffRenderable());
        getChildrenActorsAndRenderables().put(phoneOnImage,renderable.getPhoneOnRenderable());
        LGDXEffectList fadeInEffects = new LGDXEffectList();
        fadeInEffects.addEffect(new FadeLGDXEffect(0.0, 1.0, 2000));
        fadeInEffects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                renderable.enablePhoneRinging();
            }
        }));
        renderable.apply(fadeInEffects);
    }

    @Override
    public void update(Renderable renderable) {
        if (this.renderable.getRenderableLastUpdated() > timestamp) {
            this.renderable = (RoomRenderable) renderable;
            this.timestamp = this.renderable.getRenderableLastUpdated();
        }
    }
}
