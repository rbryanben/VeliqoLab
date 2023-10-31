package com.wapazock.veliqolab.utils.Share

import java.util.Dictionary

// This class is used to share data across the application
// It implements a singleton repository for all
class Share {
    // Companion
    companion object {

        // Shared instance
        private lateinit var instance : Share;

        //Get Instance
        fun getInstance() : Share {
            if (instance == null){
                instance = Share()
            }
            return instance
        }
    }

    // Key Value Store
    private var store : HashMap<String,Object> = HashMap()

    // Set Value
    fun set(key : String, value : Object){
        store[key] = value
    }

    // Get Value
    fun get(key : String) : Object{
        return store[key]!!
    }
}