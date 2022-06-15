package com.realityexpander.androidktorjwtauth.auth

import android.content.SharedPreferences
import org.apache.commons.codec.digest.DigestUtils
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val prefs: SharedPreferences
) : AuthRepository {
    override suspend fun signUp(username: String, password: String, email: String): AuthResult<String> {
        return try {
            // Attempt to sign up
            val response = authApi.signUp(
                AuthRequest(
                    username,
                    DigestUtils.sha256Hex(password), // don't send the password, just the hash
                    email)
            )
            println("AuthRepositoryImpl#signUp: ${response.string()}") // debugging

            // Attempt to get signIn & get token for new user
            signIn(username, password, email)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.Error("${e.localizedMessage}: ${getErrorMessage(e)}", e.code())
            }
        } catch (e: Exception) {
            AuthResult.Error("Unexpected error: ${e.localizedMessage}", -1)
        }
    }

    override suspend fun signIn(username: String, password: String, email: String): AuthResult<String> {
        return try {
            // Attempt to sign in
            val response = authApi.signIn(
                AuthRequest(
                    username,
                    DigestUtils.sha256Hex(password), // don't send the password, just the hash
                    email)
            )
            println("AuthRepositoryImpl#signIn: $response") // debugging

            // Save token for existing user to shared prefs
            prefs.edit()
                .putString("jwt", response.token)
                .apply()

            AuthResult.Authorized(data = response.username)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.Error("${e.localizedMessage}: ${getErrorMessage(e)}", e.code())
            }
        } catch (e: Exception) {
            AuthResult.Error("Unexpected error: ${e.localizedMessage}", -1)
        }
    }

    override suspend fun authenticate(): AuthResult<String> {
        return try {
            // Attempt to get token for existing user from shared prefs
            val token = prefs.getString("jwt", null)
                ?: return AuthResult.Unauthorized()

            val response = authApi.authenticate("Bearer $token")
            AuthResult.Authorized(data = response.string())
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.Error("${e.localizedMessage}: ${getErrorMessage(e)}", e.code())
            }
        } catch (e: Exception) {
            AuthResult.Error("Unexpected error: ${e.localizedMessage}", -1)
        }
    }
}