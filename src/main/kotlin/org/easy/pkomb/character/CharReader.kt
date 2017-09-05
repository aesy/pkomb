package org.easy.pkomb.character

import org.easy.pkomb.Reader
import org.easy.pkomb.exception.UnexpectedEOFException

/**
 * A character reader for parsing.
 *
 * @param source The string source.
 *
 * @property index The current index.
 * @property line The current line number.
 * @property column The current column.
 */
class CharReader(
    private val source: String
): Reader<Char, CharReader> {

    private var _index: Int = 0
    private var _line: Int = 1
    private var _column: Int = 0

    override val index: Int
        get() = _index
    val line: Int
        get() = _line
    val column: Int
        get() = _column

    /**
     * Gets the next character without incrementing the index.
     *
     * @throws UnexpectedEOFException If there are no remaining characters.
     *
     * @return The next value.
     */
    override fun peek(): Char {
        if (!hasRemaining()) {
            throw UnexpectedEOFException()
        }

        return source[index + 1]
    }

    /**
     * Gets a string of upcoming characters without incrementing the index.
     *
     * @throws UnexpectedEOFException If there are no remaining characters.
     *
     * @return A string of upcoming characters.
     */
    fun peek(count: Int): String {
        if (index + count > source.length) {
            throw UnexpectedEOFException()
        }

        return source.substring(index, index + count)
    }

    /**
     * Gets the next character and increments the index.
     *
     * @throws UnexpectedEOFException If there are no remaining characters.
     *
     * @return The next character.
     */
    override fun read(): Char {
        if (!hasRemaining()) {
            throw UnexpectedEOFException()
        }

        return read(1)[0]
    }

    /**
     * Gets a string of upcoming characters and increments the index accordingly.
     *
     * @throws UnexpectedEOFException If the read operation exceeds the amount of remaining characters.
     *
     * @return A string of upcoming characters.
     */
    fun read(count: Int): String {
        val builder = StringBuilder()

        repeat(count) {
            val char = peek()

            if (char == '\r' || char == '\n') {
                _line++
                _column = 0
            } else {
                _column++
            }

            _index++
            builder.append(char)
        }

        return builder.toString()
    }

    /**
     * Gets a new Reader for the remaining characters.
     *
     * @return A new Reader for the remaining characters.
     */
    override fun remainder(): CharReader {
        val copy = CharReader(source)
        copy._index = _index
        copy._line = _line
        copy._column = _column

        return copy
    }

    /**
     * Checks whether there are remaining characters.
     *
     * @return Indication whether there are remaining characters.
     */
    override fun hasRemaining(): Boolean {
        return index < source.length
    }

}
