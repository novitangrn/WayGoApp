package com.example.waygo.view.login


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.waygo.R
import com.example.waygo.SessionPrefs
import com.example.waygo.data.SessionUser
import com.example.waygo.data.model.LoginUser
import com.example.waygo.databinding.ActivityLoginBinding
import com.example.waygo.view.main.MainActivity
import com.example.waygo.view.signup.SignupActivity


class LoginActivity :AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sessionPrefs: SessionPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionPrefs = SessionPrefs(this)
        apiCheck()
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.enableBtn.observe(this) {
            enableButton(it)
        }
        viewModel.isLoad.observe(this) {
            showLoading(it)
        }
        viewModel.isLoginFail.observe(this) {
            nextAct(it)
        }
        viewModel.isToast.observe(this) { toast ->
            if (toast) {
                viewModel.responseMsg.observe(this) { msg ->
                    showToast(true, msg.toString())
                }
            }
        }
        val emailEditTxt = binding.cvEmailLogin
        val passwordEditTxt = binding.cvPasswordLogin
        binding.cvBtnLogin.setOnClickListener(View.OnClickListener {
            val email = emailEditTxt.text.toString().trim()
            val password = passwordEditTxt.text.toString().trim()

            when {
                email.isEmpty() -> {
                    emailEditTxt.error = getString(R.string.check_field)
                }

                password.isEmpty() -> {
                    passwordEditTxt.error = getString(R.string.check_field)
                }
                password.length < 8 -> {
                    passwordEditTxt.error = getString(R.string.mincharacter)
                }

                else -> {
                    viewModel.login(LoginUser(email, password))
                }
            }

        })

        val signup = binding.tvSignup
        signup.setOnClickListener(View.OnClickListener
        {
            val i = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(i)
            finish()
        })
    }

    private fun apiCheck() {
        val token = sessionPrefs.getToken()
        val apikey = token.api_key

        if (apikey != null) {
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }

    }

    private fun nextAct(failed: Boolean) {
        if (!failed) {
            viewModel.apiKey.observe(this) {
                setKey(it)
            }
            intoMain()
        }
    }

    private fun showToast(showToast: Boolean, msg: String) {
        if (showToast) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.progBar2.visibility = View.VISIBLE
        } else {
            binding.progBar2.visibility = View.GONE
        }
    }

    private fun enableButton(enabled: Boolean) {
        binding.cvBtnLogin.isEnabled = enabled
    }

    private fun intoMain() {
        val i = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun setKey(key: String) {
        val sessionPrefs = SessionPrefs(this)
        sessionPrefs.setToken(SessionUser(key))
    }
}