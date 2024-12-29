package ma.ensaj.pets

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ma.ensaj.pets.api.AuthApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var firstnameInput: EditText
    private lateinit var lastnameInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var phonenumberInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerButton: Button
    private lateinit var loginredirect : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firstnameInput = findViewById(R.id.firstnameInput)
        lastnameInput = findViewById(R.id.lastnameInput)
        addressInput = findViewById(R.id.addressInput)
        phonenumberInput = findViewById(R.id.phonenumberInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        registerButton = findViewById(R.id.registerButton)
        loginredirect = findViewById(R.id.loginRedirectText)

        loginredirect.setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        registerButton.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val firstname = firstnameInput.text.toString().trim()
        val lastname = lastnameInput.text.toString().trim()
        val address = addressInput.text.toString().trim()
        val phonenumber = phonenumberInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        // Vérifier si les champs sont vides
        if (firstname.isEmpty() || lastname.isEmpty() || address.isEmpty() || phonenumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Créer l'objet User pour l'enregistrement
        val user = User(firstName = firstname, lastName = lastname, address = address, phoneNumber = phonenumber, email = email, password = password)

        // Utiliser l'instance Retrofit déjà définie dans RetrofitClient
        val authApi = RetrofitClient.instance.create(AuthApi::class.java)

        // Appeler l'API Retrofit pour enregistrer l'utilisateur
        authApi.register(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
