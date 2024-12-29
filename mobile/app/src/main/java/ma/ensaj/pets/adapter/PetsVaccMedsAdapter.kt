package ma.ensaj.pets.adapter

import android.content.Context
import ma.ensaj.pets.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ma.ensaj.pets.dto.Pet

class PetsVaccMedsAdapter(
    private val context: Context,
    private var petsList: List<Pet>,
    private val onPetClick: (Pet) -> Unit
) : RecyclerView.Adapter<PetsVaccMedsAdapter.PetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet_vacc_meds, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = petsList[position]
        holder.bind(pet)
    }

    override fun getItemCount(): Int = petsList.size

    fun updatePetsList(newPetsList: List<Pet>) {
        petsList = newPetsList
        notifyDataSetChanged()
    }

    inner class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name)
        private val typeTextView: TextView = itemView.findViewById(R.id.type)
        private val breedtextview : TextView = itemView.findViewById(R.id.breed)
        private val image : ImageView = itemView.findViewById(R.id.circle)

        fun bind(pet: Pet) {
            nameTextView.text = pet.name
            typeTextView.text = pet.species
            breedtextview.text = pet.breed

            if(pet.species.lowercase() == "cat") {
                Glide.with(context)
                    .load(R.drawable.kitty)
                    .centerCrop()
                    .into(image)
            }

            if(pet.species.lowercase() == "dog") {
                Glide.with(context)
                    .load(R.drawable.happy)
                    .centerCrop()
                    .into(image)
            }

            if(pet.species.lowercase() == "bird") {
                Glide.with(context)
                    .load(R.drawable.eagle)
                    .centerCrop()
                    .into(image)
            }


            itemView.setOnClickListener {
                onPetClick(pet)
            }


        }
    }
}
