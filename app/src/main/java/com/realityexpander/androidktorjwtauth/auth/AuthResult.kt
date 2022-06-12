package com.realityexpander.androidktorjwtauth.auth


sealed class AuthResult<T>(val data: T? = null) {
    class Success<T>(data: T) : AuthResult<T>(data)
    class Authorized<T>(data: T? = null) : AuthResult<T>(data)
    class Unauthorized<T>() : AuthResult<T>()
    class Error<T>(val errorMessage: String = "Unknown Error", val errorCode: Int = 0) : AuthResult<T>()
}
