package org.easy.pkomb.common

import org.easy.pkomb.*

fun <T, U, R: Reader<*, R>> Parser<Iterable<T>, R>.fold(initial: U, operation: (acc: U, T) -> U): Parser<U, R> {
    return BlockParser { input ->
        val result = parse(input)

        when (result) {
            is Result.Failure -> result
            is Result.Success -> Result.Success(result.input, result.value.fold(initial, operation))
        }
    }
}

/**
 * Creates a new parser that take the value of this parser, transforms it, and returns the new value.
 *
 * Example usage:
 * `parser1 and parser2 map { it.size } // result is 2`
 *
 * @param transformer The transform function.
 * @return A new parser returning the transformed value.
 */
infix fun <T, U, R: Reader<*, R>> Parser<T, R>.map(transformer: (T) -> U): Parser<U, R> {
    return BlockParser { input ->
        val result = parse(input)

        when (result) {
            is Result.Success -> Result.Success(result.input, result.value.let(transformer))
            is Result.Failure -> result
        }
    }
}

infix fun <U, R: Reader<*, R>> Parser<*, R>.map(value: U): Parser<U, R> {
    return BlockParser { input ->
        val result = parse(input)

        when (result) {
            is Result.Success -> Result.Success(result.input, value)
            is Result.Failure -> result
        }
    }
}

/**
 * Pairs the value of this parser with the value of another parser.
 *
 * @param other
 * @return
 */
@JvmName("pair")
infix fun <T, U, R: Reader<*, R>> Parser<T, R>.pair(other: Parser<U, R>): Parser<Pair<T, U>, R> {
    return BlockParser { input ->
        val result1 = parse(input)

        when (result1) {
            is Result.Failure -> result1
            is Result.Success -> {
                val result2 = other.parse(result1.input)

                when (result2) {
                    is Result.Failure -> result2
                    is Result.Success -> Result.Success(result2.input, Pair(result1.value, result2.value))
                }
            }
        }
    }
}

@JvmName("before")
infix fun <T, R: Reader<T, R>> Parser<T, R>.and(other: Parser<Unit, R>): Parser<T, R> {
    return this pair other map { it.first }
}

@JvmName("then")
infix fun <T, R: Reader<*, R>> Parser<Unit, R>.and(other: Parser<T, R>): Parser<T, R> {
    return this pair other map { it.second }
}

@JvmName("and")
infix fun <T, R: Reader<*, R>> Parser<T, R>.and(other: Parser<T, R>): Parser<List<T>, R> {
    return sequence(this, other)
}

@JvmName("concat")
infix fun <T, R: Reader<*, R>> Parser<List<T>, R>.and(other: Parser<T, R>): Parser<List<T>, R> {
    return BlockParser { input ->
        val result1 = parse(input)

        when (result1) {
            is Result.Failure -> result1
            is Result.Success -> {
                val result2 = other.parse(result1.input)

                when (result2) {
                    is Result.Failure -> result2
                    is Result.Success -> Result.Success(result2.input, result1.value + result2.value)
                }
            }
        }
    }
}
