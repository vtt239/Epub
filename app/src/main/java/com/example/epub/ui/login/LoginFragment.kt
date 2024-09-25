package com.example.epub.ui.login

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.epub.R
import com.example.epub.api.ApiClient
import com.example.epub.api.ErrorResponse
import com.example.epub.api.LoginRequest
import com.example.epub.api.LoginResponse
import com.example.epub.databinding.FragmentLoginBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isPasswordVisible = false

        binding.btnLogin.setOnClickListener {
            val phone = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()

            if (phone.isNotEmpty() && password.isNotEmpty()) {
                loginUser(phone, password)
            } else {
                Toast.makeText(
                    context,
                    "Bạn cần điền đủ thông tin",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.register.setOnClickListener {
            Log.d("OnClick register", "register: ")
            findNavController().navigate(R.id.navigation_register)
        }

        binding.btnShowPass.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            // Đặt con trỏ (cursor) về cuối của văn bản
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }
    }

    private fun loginUser(phone: String, password: String) {
        val request = LoginRequest(phone, password)

        ApiClient.authService.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.access_token
                    Toast.makeText(context, "Login successful! Token: $token", Toast.LENGTH_SHORT)
                        .show()
                    // Chuyển đến màn hình chính hoặc lưu token
                } else {
                    response.errorBody()?.let { errorBody ->
                        val gson = Gson()
                        val errorResponse =
                            gson.fromJson(errorBody.charStream(), ErrorResponse::class.java)
                        Toast.makeText(
                            context,
                            "${errorResponse.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context, "API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}