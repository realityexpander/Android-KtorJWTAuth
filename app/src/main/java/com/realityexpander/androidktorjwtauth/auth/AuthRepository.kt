package com.realityexpander.androidktorjwtauth.auth

interface AuthRepository {
    suspend fun signUp(username: String, password: String, email: String): AuthResult<String>
    suspend fun signIn(username: String, password: String, email: String): AuthResult<String>
    suspend fun authenticate(): AuthResult<String>
}