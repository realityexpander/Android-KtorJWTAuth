package com.realityexpander.androidktorjwtauth.auth

import org.json.JSONObject
import retrofit2.HttpException

fun getErrorMessage(e: HttpException) =
    e.response()?.errorBody()?.string()?.let { errStr ->
        try {
            JSONObject(errStr)
                .getJSONObject("error").getString("message")
        } catch (e: Exception) {
            errStr
        }
    } ?: ""