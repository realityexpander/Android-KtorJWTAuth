package com.realityexpander.androidktorjwtauth.auth

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("signup")
    suspend fun signUp(
        @Body request: AuthRequest
    ): ResponseBody  // returns message from server
    // String

    @POST("signin")
    suspend fun signIn(
        @Body request: AuthRequest
    ): TokenResponse  // returns token from server

    @GET("authenticate")
    suspend fun authenticate(
        @Header ("Authorization") token: String
    ): ResponseBody  // returns message from server
    // String
}