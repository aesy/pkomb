package org.easy.pkomb

/**
 * A parser reads a source to produce a value.
 *
 * @param <T> The type of produced value.
 * @param <R> The type of reader used.
 */
interface Parser<out T, R: Reader<*, R>> {

    /**
     * Parses a source, either successfully or not. A successful value contains the resulting value.
     *
     * @param source The source.
     *
     * @return The value.
     */
    fun parse(source: R): Result<T, R>

}
