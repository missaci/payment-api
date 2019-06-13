package com.wirecard.payment.api.infrastructure

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class StringExtensionsTest {

    @Test
    fun `should identify a correct natural number`(){
        assertTrue("12345".isNaturalNumber())
        assertTrue("012345".isNaturalNumber())
    }

    @Test
    fun `should not accept fractions as natural numbers`() {
        assertFalse("1234567.43".isNaturalNumber())
        assertFalse("1234567,43".isNaturalNumber())
    }

    @Test
    fun `should not identify as natural numbers mixed strings` () {
        assertFalse("23342A".isNaturalNumber())
    }

    @Test
    fun `should not identify as natural numbers null or empty values` () {
        assertFalse("".isNaturalNumber())
        assertFalse((null as String?).isNaturalNumber())
    }

    @Test
    fun `should check the minimal length of a string`() {
        assertTrue("1234".hasMinLengthOf(3))
        assertFalse("12".hasMinLengthOf(3))
        assertTrue("123".hasMinLengthOf(3))
        assertFalse("".hasMinLengthOf(3))
    }

    @Test
    fun `should consider a null value as not having the minimum length specified unless it is 0`(){
        assertFalse((null as String?).hasMinLengthOf(2))
        assertTrue((null as String?).hasMinLengthOf(0))
    }

    @Test
    fun `should check the max length of a string`() {
        assertTrue("1234".hasMaxLengthOf(4))
        assertFalse("12".hasMaxLengthOf(1))
        assertTrue("123".hasMaxLengthOf(3))
        assertTrue("".hasMaxLengthOf(3))
    }

    @Test
    fun `should consider a null value as having the max length specified`(){
        assertTrue((null as String?).hasMaxLengthOf(2))
        assertTrue((null as String?).hasMaxLengthOf(0))
    }

    @Test
    fun `should check if a string is between a determined length` () {
        assertTrue("1234".hasLengthBetween(3, 4))
        assertFalse("12".hasLengthBetween(3, 4))
        assertTrue("123".hasLengthBetween(3, 4))
        assertFalse("".hasLengthBetween(3,4))
        assertFalse("12345".hasLengthBetween(3,4))
        assertTrue("".hasLengthBetween(0,4))
        assertTrue((null as String?).hasLengthBetween(0,4))
        assertFalse((null as String?).hasLengthBetween(1,4))
    }

    @Test
    fun `should check if a String starts with one of a given number of strings in a case sensitive fashion` () {
        assertTrue("MAT431234423".startsWithOneOf("A", "B", "M"))
        assertTrue("MAT431234423".startsWithOneOf("A", "B", "MAT"))
        assertFalse("MAT431234423".startsWithOneOf("A", "B", "AT"))
        assertFalse("MAT431234423".startsWithOneOf("A", "B", "m"))
    }

    @Test
    fun `should accept a numeric range to check if a string starts with one of the values of the range` () {
        assertTrue("3254123".startsWithOneOf(1..3))
        assertTrue("3256215".startsWithOneOf(300..400))
        assertFalse("431234423".startsWithOneOf(10..30))
        assertFalse("MAT431234423".startsWithOneOf(10..30))
    }

    @Test
    fun `should identify as false when a null string is checked with startsWithOneOf function`() {
        assertFalse((null as String?).startsWithOneOf("a", "N", "n"))
        assertFalse((null as String?).startsWithOneOf(0..2))
    }

}