package ma.ensaj.pets.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ma.ensaj.pets.R
import ma.ensaj.pets.dto.Appointment
import ma.ensaj.pets.dto.Medication
import org.threeten.bp.format.DateTimeFormatter

class AppointmentAdapter(
    private val context : Context,
    private val appointments: List<Appointment>,
    private val onEditClick: (Appointment) -> Unit,
    private val onDeleteClick: (Appointment) -> Unit
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    // Définir le formateur pour le format dd/MM/yyyy HH:mm
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]

        holder.bind(appointment)
    }


    override fun getItemCount(): Int {
        return appointments.size
    }

    inner class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTimeTextView: TextView = itemView.findViewById(R.id.textViewAppointmentDate)
        val image : ImageView = itemView.findViewById(R.id.circle)
        val reasonTextView: TextView = itemView.findViewById(R.id.textViewAppointmentReason)
        val vetTextView : TextView = itemView.findViewById(R.id.textViewAppointmentVet)
        val editButton: Button = itemView.findViewById(R.id.btnEditAppointment)
        val deleteButton: Button = itemView.findViewById(R.id.btnDeleteAppointment)

        fun bind(appointment: Appointment) {
            dateTimeTextView.text = appointment.appointmentDateTime.format(dateFormatter)
            reasonTextView.text = appointment.reason
            vetTextView.text = appointment.veterinarian.lastName

            Glide.with(context)
                .load(R.drawable.reminder)
                .centerCrop()
                .into(image)

            editButton.setOnClickListener { onEditClick(appointment) }
            deleteButton.setOnClickListener { onDeleteClick(appointment) }
        }

    }
}
