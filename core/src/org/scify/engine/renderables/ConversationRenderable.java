package org.scify.engine.renderables;

import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.game.GameInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ConversationRenderable extends TableRenderable {
    protected Set<Renderable> allRenderables;
    protected List<ConversationLine> conversationLines;
    protected boolean keepFirst;
    protected Map<String,String> replaceLexicon;

    public ConversationRenderable(String renderableCode, String id, String bgImgPath, boolean keepFirstDuringParsing, Map<String,String> replaceLexicon) {
        super(0,0,0,0,renderableCode, id, bgImgPath);
        keepFirst = keepFirstDuringParsing;
        this.replaceLexicon = replaceLexicon;
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
            s = second.split("\\}\\}");
            String last = "";
            if (s.length == 2)
                last = s[1];
            String inner = s[0];
            if (replaceLexicon != null && replaceLexicon.containsKey(inner)) {
                ret = first + replaceLexicon.get(inner) + last;
            }else {
                s = inner.split("/");
                if (keepFirst)
                    ret = first + s[0] + last;
                else
                    ret = first + s[1] + last;
            }
        }
        return ret;
    }
}
