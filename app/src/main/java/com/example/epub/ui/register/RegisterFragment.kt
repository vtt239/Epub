package com.example.epub.ui.register

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
import com.example.epub.api.RegisterRequest
import com.example.epub.api.RegisterResponse
import com.example.epub.databinding.FragmentRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.epub.api.ErrorResponse
import com.google.gson.Gson

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()
            val rePassword = binding.etRepass.text.toString()

            if (phone.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty() && rePassword.isNotEmpty()) {
                if (password == rePassword) {
                    registerUser(phone, password, email)
                } else {
                    Toast.makeText(context, "Xác nhận lại mật khẩu!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Bạn cần điền đủ thông tin!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLoginFromRes.setOnClickListener {
            findNavController().navigate(R.id.navigation_login)
        }
    }

    private fun registerUser(phone: String, password: String, email: String) {
        val request = RegisterRequest(phone, password, email)

        ApiClient.authService.registerUser(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>, response: Response<RegisterResponse>
            ) {
                Log.d("<<<<<onResponse>>>>>", "onResponse: " + response.body())
                if (response.body()?.status == 200) {
                    Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.navigation_login)
                } else {
                    response.errorBody()?.let { errorBody ->
                        val gson = Gson()
                        // Chuyển đổi JSON error body thành đối tượng ErrorResponse
                        val errorResponse =
                            gson.fromJson(errorBody.charStream(), ErrorResponse::class.java)
                        Toast.makeText(context, "${errorResponse.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(context, "API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}