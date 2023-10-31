package com.wapazock.veliqolab.models

import org.json.JSONObject

/*
    This class hold information on a Authentication Uer
 */
open class AuthUser {
    private var email: String = ""
    private var password : String = ""

    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }

    constructor()

    public fun getEmail() : String {
        return this.email
    }

    public fun getPassword(): String {
        return  this.password
    }

    public fun setEmail(email: String){
        this.email = email
    }

    public fun setPassword(password: String){
        this.password = password
    }

    public fun asJSONObject(): JSONObject {
        val res = JSONObject()
        res.put("email",email)
        res.put("password",password)
        return res
    }
}

