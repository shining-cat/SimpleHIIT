/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.commonutils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class NonEmptyListTest {
    @Test
    fun `constructor creates NonEmptyList with single element`() {
        val list = NonEmptyList("first")

        assertEquals("first", list.head)
        assertEquals(emptyList<String>(), list.tail)
        assertEquals(1, list.size)
    }

    @Test
    fun `constructor creates NonEmptyList with multiple elements`() {
        val list = NonEmptyList("first", listOf("second", "third"))

        assertEquals("first", list.head)
        assertEquals(listOf("second", "third"), list.tail)
        assertEquals(3, list.size)
    }

    @Test
    fun `toList returns correct list for single element`() {
        val list = NonEmptyList("only")

        assertEquals(listOf("only"), list.toList())
    }

    @Test
    fun `toList returns correct list for multiple elements`() {
        val list = NonEmptyList(1, listOf(2, 3, 4, 5))

        assertEquals(listOf(1, 2, 3, 4, 5), list.toList())
    }

    @Test
    fun `size returns 1 for single element`() {
        val list = NonEmptyList(42)

        assertEquals(1, list.size)
    }

    @Test
    fun `size returns correct count for multiple elements`() {
        val list = NonEmptyList("a", listOf("b", "c", "d"))

        assertEquals(4, list.size)
    }

    @Test
    fun `get operator returns head for index 0`() {
        val list = NonEmptyList("first", listOf("second", "third"))

        assertEquals("first", list[0])
    }

    @Test
    fun `get operator returns tail elements for indices greater than 0`() {
        val list = NonEmptyList(10, listOf(20, 30, 40))

        assertEquals(20, list[1])
        assertEquals(30, list[2])
        assertEquals(40, list[3])
    }

    @Test
    fun `fromList returns null for empty list`() {
        val result = NonEmptyList.fromList(emptyList<String>())

        assertNull(result)
    }

    @Test
    fun `fromList returns NonEmptyList for single element list`() {
        val result = NonEmptyList.fromList(listOf("only"))

        assertNotNull(result)
        assertEquals("only", result?.head)
        assertEquals(emptyList<String>(), result?.tail)
        assertEquals(1, result?.size)
    }

    @Test
    fun `fromList returns NonEmptyList for multiple element list`() {
        val result = NonEmptyList.fromList(listOf("a", "b", "c", "d"))

        assertNotNull(result)
        assertEquals("a", result?.head)
        assertEquals(listOf("b", "c", "d"), result?.tail)
        assertEquals(4, result?.size)
    }

    @Test
    fun `fromList preserves order of elements`() {
        val original = listOf(1, 2, 3, 4, 5)
        val result = NonEmptyList.fromList(original)

        assertNotNull(result)
        assertEquals(original, result?.toList())
    }

    @Test
    fun `equality works correctly for same content`() {
        val list1 = NonEmptyList("a", listOf("b", "c"))
        val list2 = NonEmptyList("a", listOf("b", "c"))

        assertEquals(list1, list2)
    }

    @Test
    fun `equality works correctly for different content`() {
        val list1 = NonEmptyList("a", listOf("b", "c"))
        val list2 = NonEmptyList("a", listOf("b", "d"))

        assert(list1 != list2)
    }

    @Test
    fun `hashCode is consistent for same content`() {
        val list1 = NonEmptyList(1, listOf(2, 3))
        val list2 = NonEmptyList(1, listOf(2, 3))

        assertEquals(list1.hashCode(), list2.hashCode())
    }

    @Test
    fun `works with different types`() {
        data class Person(
            val name: String,
            val age: Int,
        )

        val people =
            listOf(
                Person("Alice", 30),
                Person("Bob", 25),
                Person("Charlie", 35),
            )

        val result = NonEmptyList.fromList(people)

        assertNotNull(result)
        assertEquals(Person("Alice", 30), result?.head)
        assertEquals(3, result?.size)
        assertEquals(people, result?.toList())
    }

    @Test
    fun `fromList and toList are inverse operations`() {
        val original = listOf("x", "y", "z")
        val nonEmpty = NonEmptyList.fromList(original)
        val backToList = nonEmpty?.toList()

        assertEquals(original, backToList)
    }
}
