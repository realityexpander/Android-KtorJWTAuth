package com.realityexpander.androidktorjwtauth.auth

import android.content.SharedPreferences
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val prefs: SharedPreferences
) : AuthRepository {
    override suspend fun signUp(username: String, password: String): AuthResult<Unit> {
        return try {
            // Attempt to sign up
            val response = authApi.signUp(
                AuthRequest(username,password)
            )
            println(response) // debugging

            // Attempt to get signIn & get token for new user
            signIn(username, password)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.Error("${e.localizedMessage}: ${e.code()}", e.code())
            }
        } catch (e: Exception) {
            AuthResult.Error("Unexpected error: ${e.localizedMessage}", -1)
        }
    }

    override suspend fun signIn(username: String, password: String): AuthResult<Unit> {
        return try {
            // Attempt to sign in
            val response = authApi.signIn(
                AuthRequest(username,password)
            )
            println(response) // debugging

            // Save token for existing user to shared prefs
            prefs.edit()
                .putString("jwt", response.token)
                .apply()

            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.Error("${e.localizedMessage}: ${e.code()}", e.code())
            }
        } catch (e: Exception) {
            AuthResult.Error("Unexpected error: ${e.localizedMessage}", -1)
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            // Attempt to get token for existing user from shared prefs
            val token = prefs.getString("jwt", null)
                ?: return AuthResult.Unauthorized()

            authApi.authenticate("Bearer $token")
            AuthResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.Error("${e.localizedMessage}: ${e.code()}", e.code())
            }
        } catch (e: Exception) {
            AuthResult.Error("Unexpected error: ${e.localizedMessage}", -1)
        }
    }
}