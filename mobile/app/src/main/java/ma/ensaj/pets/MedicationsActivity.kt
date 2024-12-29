package ma.ensaj.pets

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.ensaj.pets.adapter.MedicationAdapter
import ma.ensaj.pets.api.MedicationApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.Medication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicationsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var medicationAdapter: MedicationAdapter
    private var petId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medications)

        petId = intent.getLongExtra("PET_ID", 0)

        recyclerView = findViewById(R.id.recyclerViewMedications)
        val addMedicationButton: Button = findViewById(R.id.btnAddVaccination)

        recyclerView.layoutManager = LinearLayoutManager(this)
        medicationAdapter = MedicationAdapter(this, emptyList(), ::onEditMedication, ::onDeleteMedication)
        recyclerView.adapter = medicationAdapter

        addMedicationButton.setOnClickListener {
            val intent = Intent(this, AddMedicationActivity::class.java)
            intent.putExtra("PET_ID", petId)
            startActivity(intent)
        }

        loadMedications()
    }

    private fun loadMedications() {
        val medicationApi = RetrofitClient.instance.create(MedicationApi::class.java)
        medicationApi.getMedications(petId).enqueue(object : Callback<List<Medication>> {
            override fun onResponse(call: Call<List<Medication>>, response: Response<List<Medication>>) {
                if (response.isSuccessful) {
                    medicationAdapter = MedicationAdapter(this@MedicationsActivity,response.body() ?: emptyList(), ::onEditMedication, ::onDeleteMedication)
                    recyclerView.adapter = medicationAdapter
                }
            }

            override fun onFailure(call: Call<List<Medication>>, t: Throwable) {
                Toast.makeText(this@MedicationsActivity, "Failed to load medications", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onEditMedication(medication: Medication) {
        val intent = Intent(this, EditMedicationActivity::class.java)
        intent.putExtra("PET_ID", petId)
        intent.putExtra("MEDICATION_ID", medication.id)
        startActivity(intent)
    }

    private fun onDeleteMedication(medication: Medication) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete this medication?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            val medicationApi = RetrofitClient.instance.create(MedicationApi::class.java)
            medicationApi.deleteMedication(petId, medication.id!!).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        loadMedications()
                        Toast.makeText(this@MedicationsActivity, "Medication deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MedicationsActivity, "Failed to delete medication", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@MedicationsActivity, "Failed to delete medication", Toast.LENGTH_SHORT).show()
                }
            })
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }
}
