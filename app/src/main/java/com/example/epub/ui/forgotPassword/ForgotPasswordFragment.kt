package com.example.epub.ui.forgotPassword

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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
import com.example.epub.api.SentOtpRequest
import com.example.epub.api.SentOtpResponse
import com.example.epub.databinding.FragmentForgotPasswordBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    var isViewOtp = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val otpDigits = listOf(
            binding.otpDigit1,
            binding.otpDigit2,
            binding.otpDigit3,
            binding.otpDigit4
        )

        binding.btnNext.setOnClickListener {
            val phone = binding.etPhone.text.toString()
            Log.d("isViewOtp", "onViewCreated: $isViewOtp")

            if (!isViewOtp) {
                getOtp(phone)
            } else {
                val otpCode = otpDigits.joinToString(separator = "") { it.text.toString() }
                Log.d("otpCode", "onViewCreated: $otpCode")
                if (otpCode.length == 4) {
                } else {
                }
                Log.d("isViewOtp", "onViewCreated: ")
            }
        }

        otpDigits.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        if (index < otpDigits.size - 1) {
                            otpDigits[index + 1].requestFocus()
                        } else {
//                            binding.btnVerifyOtp.isEnabled = true
                        }
                    } else {
                        if (index > 0) {
                            otpDigits[index - 1].requestFocus()
                        }
                    }
                }
            })
        }
    }

    private fun getOtp(phone: String) {
        val request = SentOtpRequest(phone)
        ApiClient.authService.getOtp(request).enqueue(object : Callback<SentOtpResponse> {
            override fun onResponse(
                call: Call<SentOtpResponse>,
                response: Response<SentOtpResponse>
            ) {
                if (response.isSuccessful) {
                    binding.llPhone.visibility = View.GONE
                    binding.llOtp.visibility = View.VISIBLE
                    isViewOtp = true
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

            override fun onFailure(call: Call<SentOtpResponse>, t: Throwable) {
                Toast.makeText(context, "API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}