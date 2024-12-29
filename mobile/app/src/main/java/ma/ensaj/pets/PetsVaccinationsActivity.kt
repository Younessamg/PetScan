package ma.ensaj.pets

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.ensaj.pets.adapter.PetsAdapter
import ma.ensaj.pets.adapter.PetsVaccMedsAdapter
import ma.ensaj.pets.api.PetApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.Pet
import ma.ensaj.pets.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetsVaccinationsActivity : AppCompatActivity() {
    private lateinit var petsRecyclerView: RecyclerView
    private lateinit var petsVaccMedsAdapter: PetsVaccMedsAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets_vacc_meds)

        val backArrow = findViewById<ImageView>(R.id.imageView6)

        sessionManager = SessionManager(this)
        val ownerId = sessionManager.fetchUserId()

        if (ownerId == -1L || sessionManager.fetchAuthToken() == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        Log.d("userid", "$ownerId")

        setupViews()
        loadPets(ownerId)

        backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupViews() {
        petsRecyclerView = findViewById(R.id.petsRecyclerView)

        petsRecyclerView.layoutManager = LinearLayoutManager(this)
        petsVaccMedsAdapter = PetsVaccMedsAdapter(
            petsList = emptyList(),
            onPetClick = { pet -> navigateToVaccinations(pet) },
            context = this// Ajoutez cette ligne pour gérer le clic sur un animal
        )
        petsRecyclerView.adapter = petsVaccMedsAdapter

    }

    private fun loadPets(ownerId: Long) {
        val petApi = RetrofitClient.instance.create(PetApi::class.java)

        petApi.getPetsByOwnerId(ownerId).enqueue(object : Callback<List<Pet>> {
            override fun onResponse(call: Call<List<Pet>>, response: Response<List<Pet>>) {
                if (response.isSuccessful) {
                    val petsList = response.body()
                    if (petsList != null) {
                        petsVaccMedsAdapter.updatePetsList(petsList)
                    } else {
                        Toast.makeText(this@PetsVaccinationsActivity, "No pets found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (response.code() == 401) {
                        sessionManager.clearSession()
                        startActivity(Intent(this@PetsVaccinationsActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@PetsVaccinationsActivity, "Failed to load pets", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Pet>>, t: Throwable) {
                Toast.makeText(this@PetsVaccinationsActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToEditPet(pet: Pet) {
        val intent = Intent(this, EditPetActivity::class.java)
        intent.putExtra("PET_ID", pet.id)
        startActivity(intent)
    }

    private fun navigateToVaccinations(pet: Pet) {
        val intent = Intent(this, VaccinationsActivity::class.java)
        intent.putExtra("PET_ID", pet.id) // Passez l'ID de l'animal à VaccinationsActivity
        startActivity(intent)
    }

    private fun navigateToMedications(pet: Pet) {
        val intent = Intent(this, MedicationsActivity::class.java)
        intent.putExtra("PET_ID", pet.id) // Passez l'ID de l'animal à VaccinationsActivity
        startActivity(intent)
    }

    private fun navigateToAppointments(pet: Pet) {
        val intent = Intent(this, AppointmentsActivity::class.java)
        intent.putExtra("PET_ID", pet.id) // Passez l'ID de l'animal à VaccinationsActivity
        startActivity(intent)
    }

    private fun confirmDeletePet(pet: Pet) {
        AlertDialog.Builder(this)
            .setTitle("Confirmation de suppression")
            .setMessage("Êtes-vous sûr de vouloir supprimer cet animal ?")
            .setPositiveButton("Oui") { _, _ -> deletePet(pet) }
            .setNegativeButton("Non", null)
            .show()
    }

    private fun deletePet(pet: Pet) {
        val petApi = RetrofitClient.instance.create(PetApi::class.java)

        pet.id?.let {
            petApi.deletePet(it).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PetsVaccinationsActivity, "Animal supprimé", Toast.LENGTH_SHORT).show()
                        loadPets(sessionManager.fetchUserId())
                    } else {
                        Toast.makeText(this@PetsVaccinationsActivity, "Échec de la suppression", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@PetsVaccinationsActivity, "Erreur réseau : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        sessionManager.clearSession()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
