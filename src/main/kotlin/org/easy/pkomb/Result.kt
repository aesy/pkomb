package org.easy.pkomb

/**
 * The value of a parsing operation.
 *
 * @param <T> The type of produced value.
 * @param <R> The type of reader used.
 *
 * @property input The reader used to read the value.
 */
sealed class Result<out T, R: Reader<*, R>> {

    abstract val input: R

    /**
     * The value of a successful parsing operation.
     *
     * @property value The parsing product.
     */
    class Success<out T, R: Reader<*, R>>(
        override val input: R,
        val value: T
    ): Result<T, R>()

    /**
     * The value of an unsuccessful parsing operation.
     *
     * @property message A message describing the failure.
     */
    class Failure<R: Reader<*, R>>(
        override val input: R,
        val message: String
    ): Result<Nothing, R>()

}
