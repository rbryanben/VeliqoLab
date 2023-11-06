package com.wapazock.veliqolab.models

import java.util.Date
import com.wapazock.veliqolab.models.Address
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

public class AddressUnitTest {

    @Test
    fun testConstructor() {
        // Create a new Address object
        val address = Address(
            1,
            "123 Main Street",
            "Apartment 2",
            "Anytown",
            "12345",
            "USA",
            "CA",
            "Home",
            Date(),
            Date(),
            1234567890
        )

        // Assert that the constructor set the values correctly
        assertEquals(1, address.id)
        assertEquals("123 Main Street", address.addressLine1)
        assertEquals("Apartment 2", address.addressLine2)
        assertEquals("Anytown", address.city)
        assertEquals("12345", address.postalCode)
        assertEquals("USA", address.country)
        assertEquals("CA", address.province)
        assertEquals("Home", address.addressType)
        assertNotNull(address.createdAt)
        assertNotNull(address.updatedAt)
        assertEquals(1234567890, address.userId)
    }

    @Test
    fun testEquals() {
        // Create two Address objects with the same values
        val address1 = Address(
            1,
            "123 Main Street",
            "Apartment 2",
            "Anytown",
            "12345",
            "USA",
            "CA",
            "Home",
            Date(),
            Date(),
            1234567890
        )
        val address2 = Address(
            1,
            "123 Main Street",
            "Apartment 2",
            "Anytown",
            "12345",
            "USA",
            "CA",
            "Home",
            Date(),
            Date(),
            1234567890
        )

        // Assert that the equals() method returns true
        assertTrue(address1.equals(address2))
    }

    @Test
    fun testCopy() {
        // Create a new Address object
        val address = Address(
            1,
            "123 Main Street",
            "Apartment 2",
            "Anytown",
            "12345",
            "USA",
            "CA",
            "Home",
            Date(),
            Date(),
            1234567890
        )

        // Create a copy of the Address object
        val copy = address.copy()

        // Assert that the copy is a new object with the same values
        assertNotSame(address, copy)
        assertEquals(address.id, copy.id)
        assertEquals(address.addressLine1, copy.addressLine1)
        assertEquals(address.addressLine2, copy.addressLine2)
        assertEquals(address.city, copy.city)
        assertEquals(address.postalCode, copy.postalCode)
        assertEquals(address.country, copy.country)
        assertEquals(address.province, copy.province)
        assertEquals(address.addressType, copy.addressType)
        assertEquals(address.createdAt, copy.createdAt)
        assertEquals(address.updatedAt, copy.updatedAt)
        assertEquals(address.userId, copy.userId)
    }

    @Test
    fun testToJSON() {
        // Create a new Address object
        val address = Address(
            1,
            "123 Main Street",
            "Apartment 2",
            "Anytown",
            "12345",
            "USA",
            "CA",
            "Home",
            Date(),
            Date(),
            1234567890
        )

        // Convert the Address object to JSON
        val jsonObject = address.toJSON()

        // Assert that the JSON object contains the correct values
        assertEquals(1, jsonObject.getInt("id"))
        assertEquals("123 Main Street", jsonObject.getString("addressLine1"))
        assertEquals("Apartment 2", jsonObject.getString("addressLine2"))
        assertEquals("Anytown", jsonObject.getString("city"))
        assertEquals("12345", jsonObject.getString("postalCode"))
    }
}