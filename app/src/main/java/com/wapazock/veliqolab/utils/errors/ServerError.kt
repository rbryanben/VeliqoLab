package com.wapazock.veliqolab.utils.errors

/*
    Definitions for any network/server related issues
 */
enum class ServerError {
    SERVER_UNREACHABLE,
    CONNECTION_TIMEOUT,
    INVALID_CREDENTIALS,
    SOMETHING_WENT_WRONG,
    EMAIL_ALREADY_EXISTS,
    EXPIRED_OTP,
    INVALID_OTP,
    INVALID_EMAIL
}