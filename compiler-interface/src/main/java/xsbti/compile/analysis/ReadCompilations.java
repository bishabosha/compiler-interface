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

package xsbti.compile.analysis;

import java.io.Serializable;

/**
 * Defines an interface to read information about Zinc's incremental compilations.
 * <p>
 * This API is useful to check how many times Zinc has compiled a set of sources and
 * when that compilation took place. One can also use it to test Zinc's regressions.
 */
public interface ReadCompilations extends Serializable {
    /**
     * Returns an array of {@link Compilation compilation instances} that provide
     * information on Zinc's compilation runs.
     * <p>
     * Note that the array may be empty if Zinc has not compiled your project yet.
     *
     * @return An array of compilation information.
     */
    public Compilation[] getAllCompilations();
}
