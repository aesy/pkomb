package org.easy.pkomb

import org.easy.pkomb.exception.UnexpectedEOFException

/**
 * A generic reader for parsing.
 *
 * @param <T> The type of read values.
 * @param <R> The type of itself.
 *
 * @property index The current index.
 */
interface Reader<out T, R: Reader<T, R>> {

    val index: Int

    /**
     * Gets the next value without incrementing the index.
     *
     * @throws UnexpectedEOFException If there are no remaining values.
     *
     * @return The next value.
     */
    fun peek(): T

    /**
     * Gets the next value and increments the index.
     *
     * @throws UnexpectedEOFException If there are no remaining values.
     *
     * @return The next value.
     */
    fun read(): T

    /**
     * Gets a new Reader for the remaining values.
     *
     * @return A new Reader for the remaining values.
     */
    fun remainder(): R

    /**
     * Checks whether there are remaining values.
     *
     * @return Indication whether there are remaining values.
     */
    fun hasRemaining(): Boolean

}

