package org.scify.engine.renderables;

import org.scify.engine.conversation.ConversationLine;
import java.util.List;
import java.util.Set;

public abstract class ConversationRenderable extends TableRenderable {
    protected Set<Renderable> allRenderables;
    protected List<ConversationLine> conversationLines;
    protected boolean keepFirst;

    public ConversationRenderable(String renderableCode, String id, String bgImgPath, boolean keepFirstDuringParsing) {
        super(0,0,0,0,renderableCode, id, bgImgPath);
        keepFirst = keepFirstDuringParsing;
    }

    public abstract void setConversationLines(List<ConversationLine> conversationLines);

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    protected String parseText (String text) {
        String ret = text;
        while (ret.contains("{{")) {
            String[] s = ret.split("\\{\\{");
            String first = s[0];
            String second = s[1];
            s = second.split("}}");
            String last = s[1];
            String inner = s[0];
            s = inner.split("/");
            if (keepFirst)
                ret = first + s[0] + last;
            else
                ret = first + s[1] + last;
        }
        return ret;
    }
}
