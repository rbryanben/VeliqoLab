package com.wapazock.veliqolab.utils.validators

class EmailValidator {
    companion object {
        fun isValid(email: String): Boolean{
            val regex = Regex("[a-zA-Z0-9+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+")

            return regex.matches(email)
        }
    }
}