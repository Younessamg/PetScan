package ma.ensaj.pets

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ma.ensaj.pets.api.VaccinationApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.Vaccination
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditVaccinationActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var dateAdministeredEditText: EditText
    private lateinit var nextDueDateEditText: EditText
    private lateinit var veterinarianEditText: EditText
    private lateinit var notesEditText: EditText
    private lateinit var descriptionEditText: EditText

    private var petId: Long = 0
    private var vaccinationId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_vaccination)

        val backArrow = findViewById<ImageView>(R.id.imageView6)

        // Récupérer l'ID de l'animal et l'ID de la vaccination à modifier
        petId = intent.getLongExtra("PET_ID", 0)
        vaccinationId = intent.getLongExtra("VACCINATION_ID", 0)

        // Initialiser les vues
        nameEditText = findViewById(R.id.etVaccinationName)
        dateAdministeredEditText = findViewById(R.id.etDateAdministered)
        nextDueDateEditText = findViewById(R.id.etNextDueDate)
        veterinarianEditText = findViewById(R.id.etVeterinarian)
        notesEditText = findViewById(R.id.etNotes)
        descriptionEditText = findViewById(R.id.etDescription)

        // Charger les informations existantes de la vaccination
        loadVaccinationDetails()

        findViewById<Button>(R.id.btnSaveVaccination).setOnClickListener {
            updateVaccination()
        }

        backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadVaccinationDetails() {
        val vaccinationApi = RetrofitClient.instance.create(VaccinationApi::class.java)
        vaccinationApi.getVaccinationById(petId, vaccinationId).enqueue(object :
            Callback<Vaccination> {
            override fun onResponse(call: Call<Vaccination>, response: Response<Vaccination>) {
                if (response.isSuccessful) {
                    val vaccination = response.body()
                    vaccination?.let {
                        nameEditText.setText(it.name)
                        dateAdministeredEditText.setText(it.dateAdministered)
                        nextDueDateEditText.setText(it.nextDueDate)
                        veterinarianEditText.setText(it.veterinarian)
                        notesEditText.setText(it.notes)
                        descriptionEditText.setText(it.description)
                    }
                } else {
                    Toast.makeText(this@EditVaccinationActivity, "Failed to load vaccination details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Vaccination>, t: Throwable) {
                Toast.makeText(this@EditVaccinationActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateVaccination() {
        val name = nameEditText.text.toString()
        val dateAdministered = dateAdministeredEditText.text.toString()
        val nextDueDate = nextDueDateEditText.text.toString()
        val veterinarian = veterinarianEditText.text.toString()
        val notes = notesEditText.text.toString()
        val description = descriptionEditText.text.toString()

        // Créer un objet Vaccination avec les nouvelles informations
        val updatedVaccination = Vaccination(
            id = vaccinationId, // Garder le même ID de vaccination
            name = name,
            dateAdministered = dateAdministered,
            nextDueDate = nextDueDate,
            veterinarian = veterinarian,
            notes = notes,
            description = description
        )

        // Appeler l'API pour mettre à jour la vaccination
        val vaccinationApi = RetrofitClient.instance.create(VaccinationApi::class.java)
        vaccinationApi.updateVaccination(petId, vaccinationId, updatedVaccination).enqueue(object : Callback<Vaccination> {
            override fun onResponse(call: Call<Vaccination>, response: Response<Vaccination>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditVaccinationActivity, "Vaccination updated successfully", Toast.LENGTH_SHORT).show()
                    finish() // Retourner à l'écran précédent
                } else {
                    Toast.makeText(this@EditVaccinationActivity, "Failed to update vaccination", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Vaccination>, t: Throwable) {
                Toast.makeText(this@EditVaccinationActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
