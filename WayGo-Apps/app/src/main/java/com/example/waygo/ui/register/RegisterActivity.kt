package com.example.waygo.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.waygo.ui.login.LoginActivity
import com.example.waygo.ui.welcome.WelcomeActivity
import com.example.waygo.databinding.ActivityRegisterBinding
import com.example.waygo.helper.Result
import com.example.waygo.helper.VMFactory
import com.example.waygo.ui.customView.ButtonRegister


class RegisterActivity : AppCompatActivity() {

    private val viewModel by viewModels<RegisterViewModel> {
        VMFactory.getInstance(this)
    }
    private lateinit var buttonRegister: ButtonRegister
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            buttonRegister = btRegister
            buttonRegister.isEnabled = false
            edUsername.addTextChangedListener(textWatcher)
            edEmailRegister.addTextChangedListener(textWatcher)
            edPasswordRegister.addTextChangedListener(textWatcher)
            edUlangPassword.addTextChangedListener(textWatcher)
        }

        binding.btMasukRegister.setOnClickListener { onClickLogin() }
        binding.btRegister.setOnClickListener { onClickRegister() }
    }

    private fun onClickLogin() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }

    private fun onClickRegister() {
        val username = binding.edUsername.text.toString()
        val email = binding.edEmailRegister.text.toString()
        val password = binding.edPasswordRegister.text.toString()
        val ulangPassword = binding.edUlangPassword.text.toString()

        if (ulangPassword == password) {
            observeRegistrationResult(username, email, password)
        } else {
            Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
        }
    }


    private fun observeRegistrationResult(username: String, email: String, password: String) {
        viewModel.registerUser(username, email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, WelcomeActivity::class.java)
                    intent.putExtra(WelcomeActivity.EXTRA_EMAIL, email)
                    intent.putExtra(WelcomeActivity.EXTRA_PASSWORD, password)
                    startActivity(intent)
                    finish()
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    Log.i("Regis", "observeRegistrationResult: ${result.error}")
                }
            }
        }
    }


    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Do nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            checkFieldsForEmptyValues()
        }

        override fun afterTextChanged(s: Editable?) {
            // Do nothing
        }
    }

    private fun checkFieldsForEmptyValues() {
        binding.apply {
            val username = edUsername.text.toString()
            val email = edEmailRegister.text.toString()
            val password = edPasswordRegister.text.toString()
            val ulangPassword = edUlangPassword.text.toString()

            val isUsernameValid = username.length >= 5

            buttonRegister.isEnabled =
                isUsernameValid && email.isNotEmpty() &&
                        password.isNotEmpty() && ulangPassword.isNotEmpty()

        }
    }

    private fun showLoading(isLoading: Boolean) {
        runOnUiThread {
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}
