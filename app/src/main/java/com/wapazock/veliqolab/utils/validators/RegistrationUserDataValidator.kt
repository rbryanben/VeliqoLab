package com.wapazock.veliqolab.utils.validators

import RegisterUserData
import com.wapazock.veliqolab.models.AuthUser


// Validates AuthUser
class RegistrationUserDataValidator {
    companion object {

        // Validate registration user data
        // Not the best validator in the world but gets things done
        fun isValid(registerUserData: RegisterUserData): Boolean {
            // Validate email and password
            if (!AuthUserValidator.isValid(AuthUser(
                    registerUserData.getEmail(),
                    registerUserData.getPassword()
                ))
            ){
                return false
            }

            // Validate other attributes
            if (!(registerUserData.getFirstName().length > 2
                && registerUserData.getMobile().length == 10
                && registerUserData.getLastName().length > 2)){
                return false
            }
            return true
        }
    }

}