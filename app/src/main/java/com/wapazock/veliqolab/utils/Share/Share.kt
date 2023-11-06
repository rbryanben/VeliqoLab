package com.wapazock.veliqolab.utils.Share

// This class is used to share data across the application
// It implements a singleton repository for all
class Share {
    // Companion
    companion object {

        // Shared instance
        private var instance : Share?=null;


        //Get Instance
        fun getInstance() : Share {
            if (instance == null){
                instance = Share()
            }
            return instance as Share
        }
    }

    // Key Value Store
    private var store : HashMap<String,String> = HashMap()

    // Set Value
    fun set(key: String, value: String){
        store[key] = value
    }

    // Get Value
    fun get(key : String) : String? {
        if (!store.containsKey(key)){
            return null
        }
        return store[key]!!
    }
}