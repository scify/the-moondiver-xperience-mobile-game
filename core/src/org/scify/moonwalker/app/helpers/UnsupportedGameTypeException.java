package org.scify.moonwalker.app.helpers;

/**
 * This Exception is thrown when trying to instanciate a new GameEngine with an unsupported game type.
 */

public class UnsupportedGameTypeException extends UnsupportedOperationException {
    public UnsupportedGameTypeException(String s) {
    }
}
