package ma.ensaj.pets

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ma.ensaj.pets.api.PetApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.Pet
import ma.ensaj.pets.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPetActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var speciesInput: EditText
    private lateinit var breedInput: EditText
    private lateinit var colorInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var addPetButton: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        val backArrow = findViewById<ImageView>(R.id.imageView6)

        nameInput = findViewById(R.id.nameInput)
        speciesInput = findViewById(R.id.speciesInput)
        breedInput = findViewById(R.id.breedInput)
        colorInput = findViewById(R.id.colorInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        addPetButton = findViewById(R.id.addPetButton)

        addPetButton.setOnClickListener {
            addPet()
        }
        backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun addPet() {
        val name = nameInput.text.toString().trim()
        val species = speciesInput.text.toString().trim()
        val breed = breedInput.text.toString().trim()
        val color = colorInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()

        // Vérifier si les champs sont vides
        if (name.isEmpty() || species.isEmpty() || breed.isEmpty() || color.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Créer l'objet Pet
        val pet = Pet(
            name = name,
            species = species,
            breed = breed,
            color = color,
            description = description
        )

        // Utiliser l'instance Retrofit pour appeler l'API
        val petApi = RetrofitClient.instance.create(PetApi::class.java)

        // Appeler l'API pour ajouter un nouvel animal
        sessionManager = SessionManager(this)
        val ownerId = sessionManager.fetchUserId() // Remplace par l'ID de l'utilisateur connecté (à récupérer depuis la session ou les préférences)
        petApi.createPet(ownerId, pet).enqueue(object : Callback<Pet> {
            override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddPetActivity, "Pet added successfully", Toast.LENGTH_SHORT).show()
                    finish() // Fermer l'activité après l'ajout
                } else {
                    Toast.makeText(this@AddPetActivity, "Failed to add pet", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Pet>, t: Throwable) {
                Toast.makeText(this@AddPetActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
