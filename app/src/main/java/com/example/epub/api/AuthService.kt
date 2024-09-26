package com.example.epub.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterData(val token: String)

data class ErrorResponse(val status: Int, val message: String)

data class RegisterRequest(val phone: String, val password: String, val email: String)

data class RegisterResponse(val status: Int, val data: RegisterData, val message: String)

data class LoginRequest(val phone: String, val password: String)

data class LoginResponse(val message: String, val access_token: String, val status: Int)

data class SentOtpRequest( val phone: String)

data class SentOtpResponse( val status: Int, val message: String, val data: Boolean)

data class VerifyOtpRequest( val phone: String, val otpCode: String)

data class VerifyOtpResponse( val status: Int, val message: String, val data: String)

interface AuthService {
    @POST("/elib/auth/register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/elib/auth/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/elib/auth/send-otp")
    fun getOtp(@Body request: SentOtpRequest): Call<SentOtpResponse>

    @POST("/elib/auth/verify")
    fun verifyOtp(@Body request: VerifyOtpRequest): Call<VerifyOtpResponse>
}