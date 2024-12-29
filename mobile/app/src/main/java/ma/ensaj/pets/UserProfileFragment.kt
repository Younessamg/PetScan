package ma.ensaj.pets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ma.ensaj.pets.api.UserApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.User
import ma.ensaj.pets.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileFragment : Fragment() {

    private lateinit var firstNameTextView: TextView
    private lateinit var lastNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var editProfileButton: Button
    private lateinit var sessionManager: SessionManager

    private lateinit var logOutButton: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate le layout pour ce fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)

        // Supposons que l'ID de l'utilisateur connecté est stocké dans SharedPreferences
        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.fetchUserId()


        if (userId != null) {
            loadUserProfile(userId)
        } else {
            Toast.makeText(requireContext(), "User  not logged in", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }

        editProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), EditUserProfileActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        logOutButton.setOnClickListener{
            logout()
        }
    }

    private fun initializeViews(view: View) {
        firstNameTextView = view.findViewById(R.id.firstNameTextView)
        lastNameTextView = view.findViewById(R.id.lastNameTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        phoneNumberTextView = view.findViewById(R.id.phoneNumberTextView)
        addressTextView = view.findViewById(R.id.addressTextView)
        editProfileButton = view.findViewById(R.id.editProfileButton)
        logOutButton = view.findViewById(R.id.logoutButton)
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
                        Toast.makeText(requireContext(), "User  not found", Toast.LENGTH_SHORT).show()
                        requireActivity().finish()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load user profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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

    private fun logout() {
        sessionManager.clearSession()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}