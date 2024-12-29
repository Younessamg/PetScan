package ma.ensaj.pets

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ma.ensaj.pets.api.AppointmentApi
import ma.ensaj.pets.api.VeterinarianApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.Appointment
import ma.ensaj.pets.dto.AppointmentStatus
import ma.ensaj.pets.dto.Veterinarian
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAppointmentActivity : AppCompatActivity() {

    private lateinit var vetIdInput: EditText
    private lateinit var dateTimeInput: EditText
    private lateinit var reasonInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var saveButton: Button
    private var currentAppointment: Appointment? = null
    private var selectedDateTime: LocalDateTime? = null

    private var appointmentId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_appointment)

        vetIdInput = findViewById(R.id.vetIdInput)
        dateTimeInput = findViewById(R.id.dateTimeInput)
        reasonInput = findViewById(R.id.reasonInput)
        notesInput = findViewById(R.id.notesInput)
        saveButton = findViewById(R.id.saveAppointmentButton)

        appointmentId = intent.getLongExtra("APPOINTMENT_ID", -1)

        if (appointmentId != -1L) {
            loadAppointmentDetails(appointmentId)
        } else {
            Toast.makeText(this, "Invalid appointment ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        setupDateTimePicker() // Configurer le sélecteur de date/heure
        saveButton.setOnClickListener {
            updateAppointment()
        }
    }

    private fun setupDateTimePicker() {
        dateTimeInput.isFocusable = false
        dateTimeInput.isClickable = true

        dateTimeInput.setOnClickListener {
            showDateTimePicker()
        }
    }

    private fun showDateTimePicker() {
        val currentDateTime = selectedDateTime ?: LocalDateTime.now()

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
                    currentDateTime.hour,
                    currentDateTime.minute,
                    true // Format 24h
                ).show()
            },
            currentDateTime.year,
            currentDateTime.monthValue - 1,
            currentDateTime.dayOfMonth
        )

        // Désactiver la sélection des dates passées
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun loadAppointmentDetails(appointmentId: Long) {
        val appointmentApi = RetrofitClient.instance.create(AppointmentApi::class.java)
        appointmentApi.getAppointmentById(appointmentId).enqueue(object : Callback<Appointment> {
            override fun onResponse(call: Call<Appointment>, response: Response<Appointment>) {
                if (response.isSuccessful) {
                    val appointment = response.body()
                    if (appointment != null) {
                        currentAppointment = appointment // Sauvegarder l'appointment courant
                        populateFields(appointment)
                    } else {
                        Toast.makeText(
                            this@EditAppointmentActivity,
                            "Appointment not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<Appointment>, t: Throwable) {
                Toast.makeText(
                    this@EditAppointmentActivity,
                    "Failed to load appointment",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun populateFields(appointment: Appointment) {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        vetIdInput.setText(appointment.veterinarian?.id?.toString() ?: "")
        dateTimeInput.setText(appointment.appointmentDateTime.format(formatter)) // Formater la date
        reasonInput.setText(appointment.reason ?: "")
        notesInput.setText(appointment.notes ?: "")

        // Initialiser selectedDateTime avec la date de l'appointment actuel
        selectedDateTime = appointment.appointmentDateTime
    }

    private fun updateAppointment() {
        val vetId = vetIdInput.text.toString().toLongOrNull()
        val dateTime = selectedDateTime
        val reason = reasonInput.text.toString()
        val notes = notesInput.text.toString()

        if (vetId == null || dateTime == null) {
            Toast.makeText(this, "Invalid input data", Toast.LENGTH_SHORT).show()
            return
        }

        // Récupérer d'abord le Pet actuel depuis l'appointment existant
        val vetApi = RetrofitClient.instance.create(VeterinarianApi::class.java)

        currentAppointment?.pet?.let { currentPet ->
            vetApi.getVeterinarian(vetId).enqueue(object : Callback<Veterinarian> {
                override fun onResponse(call: Call<Veterinarian>, response: Response<Veterinarian>) {
                    if (response.isSuccessful && response.body() != null) {
                        val veterinarian = response.body()!!

                        val updatedAppointment = Appointment(
                            id = appointmentId,
                            pet = currentPet,
                            veterinarian = veterinarian,
                            appointmentDateTime = dateTime,
                            reason = reason,
                            notes = notes,
                            status = currentAppointment?.status ?: AppointmentStatus.SCHEDULED
                        )

                        val appointmentApi = RetrofitClient.instance.create(AppointmentApi::class.java)
                        appointmentApi.updateAppointment(appointmentId, updatedAppointment)
                            .enqueue(object : Callback<Appointment> {
                                override fun onResponse(
                                    call: Call<Appointment>,
                                    response: Response<Appointment>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            this@EditAppointmentActivity,
                                            "Appointment updated successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@EditAppointmentActivity,
                                            "Failed to update appointment",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(call: Call<Appointment>, t: Throwable) {
                                    Toast.makeText(
                                        this@EditAppointmentActivity,
                                        "Error: ${t.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    } else {
                        Toast.makeText(
                            this@EditAppointmentActivity,
                            "Failed to get veterinarian details",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Veterinarian>, t: Throwable) {
                    Toast.makeText(
                        this@EditAppointmentActivity,
                        "Error getting veterinarian: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } ?: run {
            Toast.makeText(this, "Current appointment data is invalid", Toast.LENGTH_SHORT).show()
        }
    }
}
