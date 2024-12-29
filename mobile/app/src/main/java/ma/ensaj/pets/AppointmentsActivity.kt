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
import ma.ensaj.pets.adapter.AppointmentAdapter
import ma.ensaj.pets.api.AppointmentApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.Appointment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var appointmentAdapter: AppointmentAdapter
    private var petId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        val backArrow = findViewById<ImageView>(R.id.imageView6)

        petId = intent.getLongExtra("PET_ID", 0)

        recyclerView = findViewById(R.id.recyclerViewAppointments)
        val addAppointmentButton: Button = findViewById(R.id.btnAddAppointment)

        recyclerView.layoutManager = LinearLayoutManager(this)
        appointmentAdapter = AppointmentAdapter(this, emptyList(), ::onEditAppointment, ::onDeleteAppointment)
        recyclerView.adapter = appointmentAdapter

        addAppointmentButton.setOnClickListener {
            val intent = Intent(this, AddAppointmentActivity::class.java)
            intent.putExtra("PET_ID", petId)
            startActivity(intent)
        }

        loadAppointments()

        backArrow.setOnClickListener {
            onBackPressed()
        }

    }

    private fun loadAppointments() {
        val appointmentApi = RetrofitClient.instance.create(AppointmentApi::class.java)
        appointmentApi.getPetAppointments(petId).enqueue(object : Callback<List<Appointment>> {
            override fun onResponse(call: Call<List<Appointment>>, response: Response<List<Appointment>>) {
                if (response.isSuccessful) {
                    appointmentAdapter = AppointmentAdapter(this@AppointmentsActivity,response.body() ?: emptyList(), ::onEditAppointment, ::onDeleteAppointment)
                    recyclerView.adapter = appointmentAdapter
                }
            }

            override fun onFailure(call: Call<List<Appointment>>, t: Throwable) {
                Toast.makeText(this@AppointmentsActivity, "Failed to load appointments", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onEditAppointment(appointment: Appointment) {
        val intent = Intent(this, EditAppointmentActivity::class.java)
        intent.putExtra("PET_ID", petId)
        intent.putExtra("APPOINTMENT_ID", appointment.id)
        startActivity(intent)
    }

    private fun onDeleteAppointment(appointment: Appointment) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete this appointment?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            val appointmentApi = RetrofitClient.instance.create(AppointmentApi::class.java)
            appointmentApi.cancelAppointment(appointment.id!!).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        loadAppointments()
                        Toast.makeText(this@AppointmentsActivity, "Appointment deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AppointmentsActivity, "Failed to delete appointment", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@AppointmentsActivity, "Failed to delete appointment", Toast.LENGTH_SHORT).show()
                }
            })
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }

        builder.show()
    }
}
