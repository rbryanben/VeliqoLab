package com.wapazock.veliqolab.models

import android.util.Log
import org.json.JSONObject
import java.io.Serializable
import java.util.Date

class Address : Serializable {

    // Variables
    var id: Int
    var addressLine1: String
    var addressLine2: String
    var city: String
    var postalCode: String
    var country: String
    var province: String
    var addressType: String
    var createdAt: Date?
    var updatedAt: Date?
    var userId: Int

    constructor(
        id: Int,
        addressLine1: String,
        addressLine2: String,
        city: String,
        postalCode: String,
        country: String,
        province: String,
        addressType: String,
        createdAt: Date?,
        updatedAt: Date?,
        userId: Int
    ) {
        this.id = id
        this.addressLine1 = addressLine1
        this.addressLine2 = addressLine2
        this.city = city
        this.postalCode = postalCode
        this.country = country
        this.province = province
        this.addressType = addressType
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        this.userId = userId
    }

    constructor(jsonObject: JSONObject) {
        id = jsonObject.getInt("id")
        addressLine1 = jsonObject.getString("addressLine1")
        addressLine2 = jsonObject.getString("addressLine2")
        city = jsonObject.getString("city")
        postalCode = jsonObject.getString("postalCode")
        country = jsonObject.getString("country")
        province = jsonObject.getString("province")
        addressType = jsonObject.getString("addressType")
        createdAt = null
        updatedAt = null
        userId = jsonObject.getInt("userId")
    }

    // Compares two given address objects
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Address) return false

        if (id != other.id) return false
        if (addressLine1 != other.addressLine1) return false
        if (addressLine2 != other.addressLine2) return false
        if (city != other.city) return false
        if (postalCode != other.postalCode) return false
        if (country != other.country) return false
        if (province != other.province) return false
        if (addressType != other.addressType) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        if (userId != other.userId) return false

        return true
    }

    // Returns a new object copy
    fun copy(): Address {
        return Address(
            id,
            addressLine1,
            addressLine2,
            city,
            postalCode,
            country,
            province,
            addressType,
            createdAt,
            updatedAt,
            userId
        )
    }

    // To JSONObject
    fun toJSON(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("id", id)
        jsonObject.put("addressLine1", addressLine1)
        jsonObject.put("addressLine2", addressLine2)
        jsonObject.put("city", city)
        jsonObject.put("postalCode", postalCode)
        jsonObject.put("country", country)
        jsonObject.put("province", province)
        jsonObject.put("addressType", addressType)
        createdAt?.let { jsonObject.put("createdAt", it.time) }
        updatedAt?.let { jsonObject.put("updatedAt", it.time) }
        jsonObject.put("userId", userId)
        return jsonObject
    }


}

