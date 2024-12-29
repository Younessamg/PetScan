package ma.ensaj.pets

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ma.ensaj.pets.api.MedicationApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.Medication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMedicationActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var startDateEditText: EditText
    private lateinit var endDateEditText: EditText
    private lateinit var dosageEditText: EditText
    private lateinit var frequencyEditText: EditText
    private lateinit var descriptionEditText: EditText
    private var petId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medication)

        petId = intent.getLongExtra("PET_ID", 0)

        val backArrow = findViewById<ImageView>(R.id.imageView6)
        nameEditText = findViewById(R.id.etMedicationName)
        startDateEditText = findViewById(R.id.etStartDate)
        endDateEditText = findViewById(R.id.etEndDate)
        dosageEditText = findViewById(R.id.etDosage)
        frequencyEditText = findViewById(R.id.etFrequency)
        descriptionEditText = findViewById(R.id.etDescription)

        findViewById<Button>(R.id.btnSaveMedication).setOnClickListener {
            saveMedication()
        }

        backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun saveMedication() {
        val name = nameEditText.text.toString()
        val startDate = startDateEditText.text.toString()
        val endDate = endDateEditText.text.toString()
        val dosage = dosageEditText.text.toString()
        val frequency = frequencyEditText.text.toString()
        val description = descriptionEditText.text.toString()

        val medication = Medication(
            name = name,
            startDate = startDate,
            endDate = endDate,
            dosage = dosage,
            frequency = frequency,
            description = description
        )

        val medicationApi = RetrofitClient.instance.create(MedicationApi::class.java)
        medicationApi.addMedication(petId, medication).enqueue(object : Callback<Medication> {
            override fun onResponse(call: Call<Medication>, response: Response<Medication>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddMedicationActivity, "Medication added", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddMedicationActivity, "Failed to add medication", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Medication>, t: Throwable) {
                Toast.makeText(this@AddMedicationActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
