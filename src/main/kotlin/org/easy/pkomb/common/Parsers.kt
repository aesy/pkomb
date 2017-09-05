package org.easy.pkomb.common

import org.easy.pkomb.*

fun <R: Reader<*, R>> eof(): Parser<Unit, R> {
    return BlockParser { input ->
        if (input.hasRemaining()) {
            Result.Failure(input, "Expected EOF but found \"${input.peek()}\"")
        } else {
            Result.Success(input, Unit)
        }
    }
}

fun <R: Reader<*, R>> skip(parser: Parser<*, R>): Parser<Unit, R> {
    return BlockParser { input ->
        val result = parser.parse(input)

        when (result) {
            is Result.Failure -> result
            is Result.Success -> Result.Success(result.input, Unit)
        }
    }
}

fun <T, R: Reader<*, R>> sequence(vararg parsers: Parser<T, R>): Parser<List<T>, R> {
    return BlockParser { input ->
        val results: MutableList<T> = ArrayList()
        var source = input

        parsers
            .map { it.parse(source) }
            .forEach {
                when (it) {
                    is Result.Failure -> return@BlockParser it
                    is Result.Success -> {
                        source = it.input.remainder()
                        results += it.value
                    }
                }
            }

        Result.Success(source, results)
    }
}
