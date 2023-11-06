package com.wapazock.veliqolab.utils.validators

import com.wapazock.veliqolab.models.Address

class AddressValidator {
    companion object {
        // Validates address object
        public fun isValid(address: Address): Boolean {
            if (
                address.addressLine1.length > 1
                && address.addressLine2.length > 1
                && address.city.length > 1
                && address.province.length > 1
                && address.country.length > 1
                && address.postalCode.length > 1
            )
            {
                return true
            }
            return false
        }
    }
}