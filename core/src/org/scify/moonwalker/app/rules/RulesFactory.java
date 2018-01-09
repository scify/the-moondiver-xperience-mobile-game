package org.scify.moonwalker.app.rules;

import org.scify.engine.Rules;
import org.scify.moonwalker.app.game.GameType;
import org.scify.moonwalker.app.helpers.UnsupportedGameTypeException;

public class RulesFactory {

    public Rules createRules(GameType gameType) throws UnsupportedGameTypeException {
        switch (gameType) {
            case SINGLE_PLAYER:
                return new MoonWalkerRules();
            default:
                throw new UnsupportedGameTypeException("Unsupported game type: " + gameType);
        }
    }
}
