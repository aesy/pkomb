package org.easy.pkomb.common

import org.easy.pkomb.*

class PredicateParser<out T, R: Reader<T, R>>(
    private val predicate: (T) -> Boolean
): Parser<List<T>, R> {

    override fun parse(source: R): Result<List<T>, R> {
        val results: MutableList<T> = ArrayList()

        loop@ do {
            val token = source.read()
            val result = predicate(token)

            when (result) {
                true -> results += token
                false -> break@loop
            }
        } while (source.hasRemaining())

        if (results.isNotEmpty()) {
            return Result.Success(source, results)
        }

        return Result.Failure(source,
            "Failed to parse using predicate. " +
            "Expected one or more successful results, but found none.")
    }

}
