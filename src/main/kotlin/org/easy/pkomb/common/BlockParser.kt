package org.easy.pkomb.common

import org.easy.pkomb.*

class BlockParser<out T, out U, R: Reader<U, R>>(
    private val parser: (source: R) -> Result<T, R>
): Parser<T, R> {

    override fun parse(source: R): Result<T, R> {
        return parser(source)
    }

}
