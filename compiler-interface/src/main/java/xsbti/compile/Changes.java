/*
 * Scala compiler interface
 *
 * Copyright Lightbend, Inc. and Mark Harrah
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package xsbti.compile;

import java.util.Set;

/**
 * Defines an interface to query for changes of certain items that have an effect on
 * incremental compilation.
 * <p>
 * The common use case for this interface is to detect added, removed, unmodified and
 * changed source files.
 *
 * @param <T> The type parameter that defines the items that have changed.
 */
public interface Changes<T> {
    /**
     * @return The added items in a certain environment.
     */
    Set<T> getAdded();

    /**
     * @return The removed items in a certain environment.
     */
    Set<T> getRemoved();

    /**
     * @return The changed items in a certain environment.
     */
    Set<T> getChanged();

    /**
     * @return The unmodified items in a certain environment.
     */
    Set<T> getUnmodified();

    /**
     * @return Whether there was a change at all.
     */
    Boolean isEmpty();
}
