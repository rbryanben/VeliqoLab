package com.wapazock.veliqolab.utils.validators

import com.wapazock.veliqolab.models.AuthUser


// Validates AuthUser
open class AuthUserValidator {
    companion object {
        // If both username and password length are greater than or equal to true, then return true
        fun isValid(user: AuthUser): Boolean {
            if (user.getEmail().length >= 6 && user.getPassword().length >= 6) {
                return true
            }
            return false
        }
    }

}