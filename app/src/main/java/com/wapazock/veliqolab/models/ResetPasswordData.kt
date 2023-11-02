package com.wapazock.veliqolab.models

import org.json.JSONObject

class ResetPasswordData {
    // Variables
    private var newPassword: String = ""
    private var otp: String = ""

    // Constructors
    constructor(email: String, otp: String) {
        this.newPassword = email
        this.otp = otp
    }
    constructor(){}

    // Getters
    fun getNewPassword(): String {
        return newPassword
    }

    fun getOtp(): String {
        return otp
    }

    // Setters
    fun setNewPassword(password: String) {
        this.newPassword = password
    }

    fun setOtp(otp: String) {
        this.otp = otp
    }

    fun toJSONObject(): JSONObject {
        val jsonObject = JSONObject().apply {
            put("otp",otp)
            put("password",newPassword)
        }
        return jsonObject
    }
}