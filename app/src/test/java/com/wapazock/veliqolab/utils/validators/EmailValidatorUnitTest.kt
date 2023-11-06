package com.wapazock.veliqolab.utils.validators

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class EmailValidatorUnitTest {

    @Test
    fun testValidEmails() {
        val validEmails = listOf(
            "email@example.com",
            "user.name@example.com",
            "username@example.co.uk",
            "firstname.lastname@example.net",
            "email+address@example.org"
        )

        for (email in validEmails) {
            assertTrue(EmailValidator.isValid(email))
        }
    }

    @Test
    fun testInvalidEmails() {
        val invalidEmails = listOf(
            "email@example",
            "user name@example.com",
            "username@example@example.co.uk",
            "firstname.lastname@example.",
            "email+address@example.org."
        )

        for (email in invalidEmails) {
            assertFalse(EmailValidator.isValid(email))
        }
    }
}