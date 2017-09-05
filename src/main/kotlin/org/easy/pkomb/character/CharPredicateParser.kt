package org.easy.pkomb.character

import org.easy.pkomb.Parser
import org.easy.pkomb.Result
import org.easy.pkomb.common.*

class CharPredicateParser(
    private val predicate: (Char) -> Boolean
): Parser<String, CharReader> {

    override fun parse(source: CharReader): Result<String, CharReader> {
        return PredicateParser<Char, CharReader>(predicate)
            .fold(StringBuilder(), { acc, curr -> acc.append(curr) })
            .map(StringBuilder::toString)
            .parse(source)
    }

}
