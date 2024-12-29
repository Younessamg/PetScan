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
import com.google.android.material.bottomnavigation.BottomNavigationView
import ma.ensaj.pets.api.UserApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.User
import ma.ensaj.pets.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var name : TextView
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate le layout pour ce fragment

        return inflater.inflate(R.layout.fragment_home, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisation des vues avec findViewById
        name = view.findViewById(R.id.UserName)
        button1 = view.findViewById(R.id.button)
        button2 = view.findViewById(R.id.button2)
        button3 = view.findViewById(R.id.button3)
        button4 = view.findViewById(R.id.button4)
        bottomNavigationView = view.findViewById(R.id.bottomNavigationView)

        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.fetchUserId()

        loadUserProfile(userId)
        // Gestion des clics sur les boutons
        button1.setOnClickListener { openActivity(PetsAppointmentsActivity::class.java) }
        button2.setOnClickListener { openActivity(AddPetActivity::class.java) }
        button3.setOnClickListener { openActivity(PetsVaccinationsActivity::class.java) }
        button4.setOnClickListener { openActivity(PetsMedicationsActivity::class.java) }

        // Gestion des clics sur la barre de navigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            (activity as HomeActivity).onNavigationItemSelected(item)
        }
    }


    // Méthode pour ouvrir une activité
    private fun openActivity(activityClass: Class<*>) {
        val intent = Intent(activity, activityClass)
        startActivity(intent)
    }

    private fun loadUserProfile(userId: Long) {
        val userApi = RetrofitClient.instance.create(UserApi::class.java)
        userApi.getUserById(userId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        name.text = user.lastName
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
}