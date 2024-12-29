package ma.ensaj.pets

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ma.ensaj.pets.R.id.petDescriptionEditText
import ma.ensaj.pets.api.PetApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.Pet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPetActivity : AppCompatActivity() {
    private lateinit var petNameEditText: EditText
    private lateinit var petSpeciesEditText: EditText
    private lateinit var petBreedEditText: EditText
    private lateinit var petDescriptionEditText: EditText
    private lateinit var petColorEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var pet: Pet
    private var petId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pet)

        val backArrow = findViewById<ImageView>(R.id.imageView6)

        petId = intent.getLongExtra("PET_ID", 0L)
        if (petId == 0L) {
            finish()
            return
        }

        petNameEditText = findViewById(R.id.petNameEditText)
        petSpeciesEditText = findViewById(R.id.petSpeciesEditText)
        petBreedEditText = findViewById(R.id.petBreedEditText)
        petDescriptionEditText = findViewById(R.id.petDescriptionEditText)
        petColorEditText = findViewById(R.id.petColorEditText)
        saveButton = findViewById(R.id.saveButton)

        loadPetDetails()

        saveButton.setOnClickListener {
            val updatedPet = Pet(
                id = pet.id,
                name = petNameEditText.text.toString(),
                species = petSpeciesEditText.text.toString(),
                breed = petBreedEditText.text.toString(),
                description = petDescriptionEditText.text.toString(),
                color = petColorEditText.text.toString()
            )
            updatePetDetails(updatedPet)
        }

        backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadPetDetails() {
        val petApi = RetrofitClient.instance.create(PetApi::class.java)

        petApi.getPetById(petId).enqueue(object : Callback<Pet> {
            override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                if (response.isSuccessful) {
                    pet = response.body()!!
                    petNameEditText.setText(pet.name)
                    petSpeciesEditText.setText(pet.species)
                    petBreedEditText.setText(pet.breed)
                    petDescriptionEditText.setText(pet.description)
                    petColorEditText.setText(pet.color)
                } else {
                    Toast.makeText(this@EditPetActivity, "Failed to load pet details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Pet>, t: Throwable) {
                Toast.makeText(this@EditPetActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updatePetDetails(updatedPet: Pet) {
        val petApi = RetrofitClient.instance.create(PetApi::class.java)

        updatedPet.id?.let {
            petApi.updatePet(it, updatedPet).enqueue(object : Callback<Pet> {
                override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditPetActivity, "Pet updated successfully", Toast.LENGTH_SHORT).show()
                        finish()  // Close the activity after successful update
                    } else {
                        Toast.makeText(this@EditPetActivity, "Failed to update pet", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Pet>, t: Throwable) {
                    Toast.makeText(this@EditPetActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
