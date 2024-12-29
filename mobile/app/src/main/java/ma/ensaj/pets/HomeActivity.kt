package ma.ensaj.pets

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_container)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Afficher le fragment d'accueil par défaut
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        // Gestion des clics sur la barre de navigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            onNavigationItemSelected(item)
        }
    }

    // Gestion des éléments de navigation
    fun onNavigationItemSelected(item: MenuItem): Boolean {
        val selectedFragment: Fragment? = when (item.itemId) {
            R.id.nav_home -> HomeFragment()
            R.id.nav_appointments -> PetsAppointmentsFragment()
            R.id.nav_vet_map -> MapsFragment()
            R.id.nav_shop -> ProductFragment()
            R.id.nav_profile -> UserProfileFragment()
            else -> null
        }

        selectedFragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, it)
                .commit()
        }

        return selectedFragment != null // Renvoie true si un fragment a été sélectionné
    }
}