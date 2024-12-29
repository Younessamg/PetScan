package ma.ensaj.pets

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ma.ensaj.pets.api.AuthApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.AuthResponse
import ma.ensaj.pets.dto.LoginRequest
import ma.ensaj.pets.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var sessionManager: SessionManager
    private lateinit var register : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        // Si l'utilisateur est déjà connecté, redirigez directement vers PetsActivity
        if (sessionManager.fetchAuthToken() != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        register = findViewById(R.id.registerRedirectText)

        register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        loginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val loginRequest = LoginRequest(email, password)
        val authApi = RetrofitClient.instance.create(AuthApi::class.java)

        authApi.login(loginRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    Log.d("LoginActivity", "Response code: ${response.code()}")
                    Log.d("LoginActivity", "Response headers: ${response.headers()}")
                    // Sauvegarder les informations de session
                    sessionManager.saveAuthToken(authResponse.token)
                    sessionManager.saveUserId(authResponse.userId)

                    // Configurer le client Retrofit avec le token
                    RetrofitClient.updateToken(authResponse.token)

                    //val intent = Intent(this@LoginActivity, PetsActivity::class.java)
                    //val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
                    //val intent = Intent(this@LoginActivity, ProductActivity::class.java)
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}