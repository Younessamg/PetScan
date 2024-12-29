package ma.ensaj.pets

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ma.ensaj.pets.api.VaccinationApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.Vaccination
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddVaccinationActivity : AppCompatActivity() {
    private lateinit var petSpinner: Spinner
    private lateinit var nameEditText: EditText
    private lateinit var dateAdministeredEditText: EditText
    private lateinit var nextDueDateEditText: EditText
    private lateinit var veterinarianEditText: EditText
    private lateinit var notesEditText: EditText
    private lateinit var descriptionEditText: EditText
    private var petId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vaccination)

        val backArrow = findViewById<ImageView>(R.id.imageView6)

        petId = intent.getLongExtra("PET_ID", 0)

        petSpinner = findViewById(R.id.spinnerSelectPet)
        nameEditText = findViewById(R.id.etVaccinationName)
        dateAdministeredEditText = findViewById(R.id.etDateAdministered)
        nextDueDateEditText = findViewById(R.id.etNextDueDate)
        veterinarianEditText = findViewById(R.id.etVeterinarian)
        notesEditText = findViewById(R.id.etNotes)
        descriptionEditText = findViewById(R.id.etDescription)

        findViewById<Button>(R.id.btnSaveVaccination).setOnClickListener {
            saveVaccination()
        }

        backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun saveVaccination() {
        val name = nameEditText.text.toString()
        val dateAdministered = dateAdministeredEditText.text.toString()
        val nextDueDate = nextDueDateEditText.text.toString()
        val veterinarian = veterinarianEditText.text.toString()
        val notes = notesEditText.text.toString()
        val description = descriptionEditText.text.toString()

        val vaccination = Vaccination(
            name = name,
            dateAdministered = dateAdministered,
            nextDueDate = nextDueDate,
            veterinarian = veterinarian,
            notes = notes,
            description = description
        )

        val vaccinationApi = RetrofitClient.instance.create(VaccinationApi::class.java)
        vaccinationApi.addVaccination(petId, vaccination).enqueue(object : Callback<Vaccination> {
            override fun onResponse(call: Call<Vaccination>, response: Response<Vaccination>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddVaccinationActivity, "Vaccination added", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddVaccinationActivity, "Failed to add vaccination", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Vaccination>, t: Throwable) {
                Toast.makeText(this@AddVaccinationActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
