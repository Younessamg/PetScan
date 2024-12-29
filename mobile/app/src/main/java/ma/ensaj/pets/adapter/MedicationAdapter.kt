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
import ma.ensaj.pets.dto.Medication

class MedicationAdapter(
    private val context: Context,
    private val medications: List<Medication>,
    private val onEditClick: (Medication) -> Unit,
    private val onDeleteClick: (Medication) -> Unit
) : RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medication, parent, false)
        return MedicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        val medication = medications[position]
        holder.bind(medication)
    }

    override fun getItemCount() = medications.size

    inner class MedicationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image : ImageView = itemView.findViewById(R.id.circle)
        private val medicationName: TextView = itemView.findViewById(R.id.medicationName)
        private val medicationStartDate: TextView = itemView.findViewById(R.id.medicationStartDate)
        private val medicationEndDate: TextView = itemView.findViewById(R.id.medicationEndDate)
        private val medicationDosage: TextView = itemView.findViewById(R.id.medicationDosage)
        private val medicationFrequency: TextView = itemView.findViewById(R.id.medicationFrequency)
        private val btnEditMedication: Button = itemView.findViewById(R.id.btnEditMedication)
        private val btnDeleteMedication: Button = itemView.findViewById(R.id.btnDeleteMedication)

        fun bind(medication: Medication) {
            medicationName.text = medication.name
            medicationStartDate.text = "${medication.startDate}"
            medicationEndDate.text = "${medication.endDate}"
            medicationDosage.text = "${medication.dosage}"
            medicationFrequency.text = "${medication.frequency}"

            Glide.with(context)
                .load(R.drawable.drugs)
                .centerCrop()
                .into(image)

            btnEditMedication.setOnClickListener { onEditClick(medication) }
            btnDeleteMedication.setOnClickListener { onDeleteClick(medication) }
        }
    }
}
