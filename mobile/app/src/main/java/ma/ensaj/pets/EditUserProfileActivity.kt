package ma.ensaj.pets

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ma.ensaj.pets.api.UserApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditUserProfileActivity : AppCompatActivity() {

    private lateinit var firstNameInput: EditText
    private lateinit var lastNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneNumberInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var saveButton: Button
    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_profile)

        initializeViews()

        val backArrow = findViewById<ImageView>(R.id.imageView6)

        userId = intent.getLongExtra("USER_ID", -1)
        if (userId != -1L) {
            loadUserDetails(userId)
        } else {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        saveButton.setOnClickListener {
            updateUserProfile()
        }

        backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initializeViews() {
        firstNameInput = findViewById(R.id.editFirstNameInput)
        lastNameInput = findViewById(R.id.editLastNameInput)
        emailInput = findViewById(R.id.editEmailInput)
        phoneNumberInput = findViewById(R.id.editPhoneNumberInput)
        addressInput = findViewById(R.id.editAddressInput)
        saveButton = findViewById(R.id.saveUserProfileButton)
    }

    private fun loadUserDetails(userId: Long) {
        val userApi = RetrofitClient.instance.create(UserApi::class.java)
        userApi.getUserById(userId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        populateFields(user)
                    } else {
                        Toast.makeText(this@EditUserProfileActivity, "User not found", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@EditUserProfileActivity, "Failed to load user details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@EditUserProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun populateFields(user: User) {
        firstNameInput.setText(user.firstName)
        lastNameInput.setText(user.lastName)
        emailInput.setText(user.email)
        phoneNumberInput.setText(user.phoneNumber)
        addressInput.setText(user.address)
    }

    private fun updateUserProfile() {
        val updatedUser = User(
            id = userId.toString(),
            firstName = firstNameInput.text.toString(),
            lastName = lastNameInput.text.toString(),
            email = emailInput.text.toString(),
            phoneNumber = phoneNumberInput.text.toString(),
            address = addressInput.text.toString(),
            password = "" // Assurez-vous de gérer le mot de passe ailleurs ou de ne pas l'écraser
        )

        val userApi = RetrofitClient.instance.create(UserApi::class.java)
        userApi.updateUser(userId, updatedUser).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditUserProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditUserProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@EditUserProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
