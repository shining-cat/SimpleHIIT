package fr.shiningcat.simplehiit.commonutils

/**
 * A type-safe list that guarantees at least one element.
 * This prevents the possibility of having an empty list in a Success state.
 *
 * @param T the type of elements in this list
 * @property head the first element (guaranteed to exist)
 * @property tail the remaining elements (may be empty)
 */
data class NonEmptyList<T>(
    val head: T,
    val tail: List<T> = emptyList(),
) {
    /**
     * Converts this NonEmptyList to a regular List.
     */
    fun toList(): List<T> = listOf(head) + tail

    /**
     * Returns the number of elements in this list (always >= 1).
     */
    val size: Int get() = 1 + tail.size

    /**
     * Returns the element at the specified index.
     * @param index the index of the element to return (0-based)
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    operator fun get(index: Int): T = if (index == 0) head else tail[index - 1]

    companion object {
        /**
         * Creates a NonEmptyList from a regular List.
         * Returns null if the list is empty.
         *
         * @param list the list to convert
         * @return a NonEmptyList if the input list is not empty, null otherwise
         */
        fun <T> fromList(list: List<T>): NonEmptyList<T>? = if (list.isEmpty()) null else NonEmptyList(list.first(), list.drop(1))
    }
}
