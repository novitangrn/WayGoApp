package com.example.waygo.view.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.waygo.R
import com.example.waygo.custview.SignupButton
import com.example.waygo.data.model.SignupUser
import com.example.waygo.databinding.ActivitySignupBinding
import com.example.waygo.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var btnSignup: SignupButton
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signupViewModel = ViewModelProvider(this)[SignupViewModel::class.java]
        signupViewModel.enableButton.observe(this) {
            enableButton(it)
        }
        signupViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        signupViewModel.isRegisterUnsuccessful.observe(this) {
            intoLogin(it)
        }
        signupViewModel.isToast.observe(this) { toast ->
            if (toast) {
                signupViewModel.responseMessage.observe(this) { msg ->
                    showToast(true, msg.toString())
                }
            }
        }
        btnSignup = binding.btnSignup
        val emailEditText = binding.etEmailSignup
        val passwordEditText = binding.etPasswrdSignup
        val nameEditText = binding.etNameSignup
        val loginNow = binding.tvLogin

        loginNow.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        })

        binding.btnSignup.setOnClickListener(View.OnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val name = nameEditText.text.toString().trim()

            when {
                email.isEmpty() -> {
                    emailEditText.error = getString(R.string.check_field)
                }

                password.isEmpty() -> {
                    passwordEditText.error = getString(R.string.check_field)
                }

                name.isEmpty() -> {
                    nameEditText.error = getString(R.string.check_field)
                }

                password.length < 8 -> {
                    passwordEditText.error = getString(R.string.mincharacter)
                }

                else -> {
                    signupViewModel.signupUser(SignupUser(email, password, name))
                }
            }

        })
    }

    private fun intoLogin(failed: Boolean) {
        if (!failed) {
            val i = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun showToast(show: Boolean, msg: String) {
        if (show) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.progBar1.visibility = View.VISIBLE
        } else {
            binding.progBar1.visibility = View.GONE
        }
    }

    private fun enableButton(enabled: Boolean) {
        binding.btnSignup.isEnabled = enabled
    }
}