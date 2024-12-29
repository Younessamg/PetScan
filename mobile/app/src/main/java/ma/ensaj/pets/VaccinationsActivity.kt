package ma.ensaj.pets

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.ensaj.pets.adapter.VaccinationAdapter
import ma.ensaj.pets.api.VaccinationApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.Vaccination
import ma.ensaj.pets.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VaccinationsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var vaccinationAdapter: VaccinationAdapter
    private var petId: Long = 0 // L'ID de l'animal sélectionné

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccinations)

        val backArrow = findViewById<ImageView>(R.id.imageView6)

        petId = intent.getLongExtra("PET_ID", 0) // Récupérer l'ID de l'animal passé via Intent

        recyclerView = findViewById(R.id.recyclerViewVaccinations)
        val addVaccinationButton: Button = findViewById(R.id.btnAddVaccination)

        recyclerView.layoutManager = LinearLayoutManager(this)
        vaccinationAdapter = VaccinationAdapter(this,emptyList(), ::onEditVaccination, ::onDeleteVaccination)
        recyclerView.adapter = vaccinationAdapter

        addVaccinationButton.setOnClickListener {
            val intent = Intent(this, AddVaccinationActivity::class.java)
            intent.putExtra("PET_ID", petId)
            startActivity(intent)
        }

        loadVaccinations()

        backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadVaccinations() {
        val vaccinationApi = RetrofitClient.instance.create(VaccinationApi::class.java)
        vaccinationApi.getVaccinations(petId).enqueue(object : Callback<List<Vaccination>> {
            override fun onResponse(call: Call<List<Vaccination>>, response: Response<List<Vaccination>>) {
                if (response.isSuccessful) {
                    vaccinationAdapter = VaccinationAdapter(this@VaccinationsActivity, response.body() ?: emptyList(), ::onEditVaccination, ::onDeleteVaccination)
                    recyclerView.adapter = vaccinationAdapter
                }
            }

            override fun onFailure(call: Call<List<Vaccination>>, t: Throwable) {
                Toast.makeText(this@VaccinationsActivity, "Failed to load vaccinations", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onEditVaccination(vaccination: Vaccination) {
        val intent = Intent(this, EditVaccinationActivity::class.java)
        intent.putExtra("PET_ID", petId)
        intent.putExtra("VACCINATION_ID", vaccination.id)
        startActivity(intent)
    }


    private fun onDeleteVaccination(vaccination: Vaccination) {
        // Créer l'alerte de confirmation
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmer la suppression")
        builder.setMessage("Êtes-vous sûr de vouloir supprimer cette vaccination ?")

        // Si l'utilisateur clique sur "Oui", procéder à la suppression
        builder.setPositiveButton("Oui") { dialog, _ ->
            val vaccinationApi = RetrofitClient.instance.create(VaccinationApi::class.java)
            vaccinationApi.deleteVaccination(petId, vaccination.id!!).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        loadVaccinations()
                        Toast.makeText(this@VaccinationsActivity, "Vaccination supprimée avec succès", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@VaccinationsActivity, "Échec de la suppression de la vaccination", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@VaccinationsActivity, "Échec de la suppression de la vaccination", Toast.LENGTH_SHORT).show()
                }
            })
            dialog.dismiss() // Fermer la boîte de dialogue après la suppression
        }

        // Si l'utilisateur clique sur "Non", simplement fermer la boîte de dialogue
        builder.setNegativeButton("Non") { dialog, _ ->
            dialog.dismiss() // Fermer la boîte de dialogue
        }

        // Afficher la boîte de dialogue
        builder.show()
    }
}
