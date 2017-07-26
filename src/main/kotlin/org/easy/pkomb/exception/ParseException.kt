package org.easy.pkomb.exception

/**
 * Thrown when a parsing operation has failed.
 */
open class ParseException(
    override val message: String
): Exception(message)
