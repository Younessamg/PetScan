package ma.ensaj.pets

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ma.ensaj.pets.api.AppointmentApi
import ma.ensaj.pets.api.PetApi
import ma.ensaj.pets.api.VeterinarianApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.Appointment
import ma.ensaj.pets.dto.AppointmentStatus
import ma.ensaj.pets.dto.Pet
import ma.ensaj.pets.dto.Veterinarian
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class AddAppointmentActivity : AppCompatActivity() {

    private lateinit var vetIdInput: EditText
    private lateinit var dateTimeInput: EditText
    private lateinit var reasonInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var saveButton: Button
    private var petId: Long = 0
    private var selectedDateTime: LocalDateTime? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_appointment)

        val backArrow = findViewById<ImageView>(R.id.imageView6)

        petId = intent.getLongExtra("PET_ID", 0)
        initializeViews()
        setupDateTimePicker()

        saveButton.setOnClickListener {
            saveAppointment()
        }

        backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initializeViews() {
        vetIdInput = findViewById(R.id.vetIdInput)
        dateTimeInput = findViewById(R.id.dateTimeInput)
        reasonInput = findViewById(R.id.reasonInput)
        notesInput = findViewById(R.id.notesInput)
        saveButton = findViewById(R.id.saveAppointmentButton)
    }

    private fun setupDateTimePicker() {
        // Rendre le champ date/heure non éditable mais cliquable
        dateTimeInput.isFocusable = false
        dateTimeInput.isClickable = true

        dateTimeInput.setOnClickListener {
            showDateTimePicker()
        }
    }

    private fun showDateTimePicker() {
        val currentDate = LocalDateTime.now()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        selectedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute, 0)
                        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                        dateTimeInput.setText(selectedDateTime?.format(formatter))
                    },
                    currentDate.hour,
                    currentDate.minute,
                    true // 24h format
                ).show()
            },
            currentDate.year,
            currentDate.monthValue - 1,
            currentDate.dayOfMonth
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() // Empêcher les dates passées
        datePickerDialog.show()
    }

    private fun saveAppointment() {
        val vetId = vetIdInput.text.toString().toLongOrNull()
        val reason = reasonInput.text.toString()
        val notes = notesInput.text.toString()

        // Validation des champs
        if (vetId == null) {
            Toast.makeText(this, "Please enter a valid veterinarian ID", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedDateTime == null) {
            Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show()
            return
        }
        if (reason.isEmpty()) {
            Toast.makeText(this, "Please enter a reason", Toast.LENGTH_SHORT).show()
            return
        }

        val petApi = RetrofitClient.instance.create(PetApi::class.java)

        // Commencer à créer l'appointment
        petApi.getPetById(petId).enqueue(object : Callback<Pet> {
            override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                if (response.isSuccessful && response.body() != null) {
                    val pet = response.body()!!
                    getVeterinarianAndCreateAppointment(vetId, pet, reason, notes)
                } else {
                    Toast.makeText(this@AddAppointmentActivity,
                        "Failed to get pet details: ${response.code()}",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Pet>, t: Throwable) {
                Toast.makeText(this@AddAppointmentActivity,
                    "Error getting pet: ${t.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getVeterinarianAndCreateAppointment(vetId: Long, pet: Pet, reason: String, notes: String) {
        val vetApi = RetrofitClient.instance.create(VeterinarianApi::class.java)

        vetApi.getVeterinarian(vetId).enqueue(object : Callback<Veterinarian> {
            override fun onResponse(call: Call<Veterinarian>, response: Response<Veterinarian>) {
                if (response.isSuccessful && response.body() != null) {
                    val veterinarian = response.body()!!
                    createAppointment(pet, veterinarian, reason, notes)
                } else {
                    Toast.makeText(this@AddAppointmentActivity,
                        "Failed to get veterinarian details: ${response.code()}",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Veterinarian>, t: Throwable) {
                Toast.makeText(this@AddAppointmentActivity,
                    "Error getting veterinarian: ${t.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createAppointment(pet: Pet, veterinarian: Veterinarian, reason: String, notes: String) {
        val appointment = Appointment(
            pet = pet,
            veterinarian = veterinarian,
            appointmentDateTime = selectedDateTime!!,
            reason = reason,
            notes = notes,
            status = AppointmentStatus.SCHEDULED
        )

        val appointmentApi = RetrofitClient.instance.create(AppointmentApi::class.java)
        appointmentApi.createAppointment(appointment).enqueue(object : Callback<Appointment> {
            override fun onResponse(call: Call<Appointment>, response: Response<Appointment>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddAppointmentActivity,
                        "Appointment added successfully",
                        Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddAppointmentActivity,
                        "Failed to add appointment: ${response.code()}, ${response.body()}",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Appointment>, t: Throwable) {
                Toast.makeText(this@AddAppointmentActivity,
                    "Error creating appointment: ${t.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}