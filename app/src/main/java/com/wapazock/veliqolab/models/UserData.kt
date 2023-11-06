package com.wapazock.veliqolab.models

import org.json.JSONObject

class UserData {

    private var id: Int
    private var firstName: String
    private var middleNames: String
    private var lastName: String
    private var email: String
    private var phoneNumber: String
    private var isEmailVerified: Boolean

    constructor(
        id: Int,
        firstName: String,
        middleNames: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        isEmailVerified: Boolean
    ) {
        this.id = id
        this.firstName = firstName
        this.middleNames = middleNames
        this.lastName = lastName
        this.email = email
        this.phoneNumber = phoneNumber
        this.isEmailVerified = isEmailVerified
    }

    // Construct from JSON
    constructor(jsonObject: JSONObject) {
        id = jsonObject.getInt("id")
        firstName = jsonObject.getString("firstName")
        middleNames = jsonObject.getString("middleNames")
        lastName = jsonObject.getString("lastName")
        email = jsonObject.getString("email")
        phoneNumber = jsonObject.getString("phoneNumber")
        isEmailVerified = jsonObject.getBoolean("isEmailVerified")
    }
    fun getId(): Int {
        return id
    }

    fun getFirstName(): String {
        return firstName
    }

    fun getMiddleNames(): String {
        return middleNames
    }

    fun getLastName(): String {
        return lastName
    }

    fun getEmail(): String {
        return email
    }

    fun getPhoneNumber(): String {
        return phoneNumber
    }

    fun isEmailVerified(): Boolean {
        return isEmailVerified
    }

}