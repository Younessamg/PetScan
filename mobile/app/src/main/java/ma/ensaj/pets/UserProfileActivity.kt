package ma.ensaj.pets

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ma.ensaj.pets.api.UserApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.User
import ma.ensaj.pets.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileActivity : AppCompatActivity() {

    private lateinit var firstNameTextView: TextView
    private lateinit var lastNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var editProfileButton: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        initializeViews()

        // Supposons que l'ID de l'utilisateur connecté est stocké dans SharedPreferences
        sessionManager = SessionManager(this)
        val userId = sessionManager.fetchUserId()

        if (userId != null) {
            loadUserProfile(userId)
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
        }

        editProfileButton.setOnClickListener {
            val intent = Intent(this, EditUserProfileActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
    }

    private fun initializeViews() {
        firstNameTextView = findViewById(R.id.firstNameTextView)
        lastNameTextView = findViewById(R.id.lastNameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView)
        addressTextView = findViewById(R.id.addressTextView)
        editProfileButton = findViewById(R.id.editProfileButton)
    }

    private fun loadUserProfile(userId: Long) {
        val userApi = RetrofitClient.instance.create(UserApi::class.java)
        userApi.getUserById(userId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        displayUserProfile(user)
                    } else {
                        Toast.makeText(this@UserProfileActivity, "User not found", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@UserProfileActivity, "Failed to load user profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@UserProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayUserProfile(user: User) {
        firstNameTextView.text = user.firstName
        lastNameTextView.text = user.lastName
        emailTextView.text = user.email
        phoneNumberTextView.text = user.phoneNumber
        addressTextView.text = user.address
    }

    private fun getUserIdFromPreferences(): Long? {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getLong("USER_ID", -1).takeIf { it != -1L }
    }
}
