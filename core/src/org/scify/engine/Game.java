package org.scify.engine;

/**
 * Created by pisaris on 5/1/2018.
 */

import java.util.concurrent.Callable;

public interface Game<T> extends Callable<T> {
    void initialize(GameProps props);
    T call();
    void finalize();
}
