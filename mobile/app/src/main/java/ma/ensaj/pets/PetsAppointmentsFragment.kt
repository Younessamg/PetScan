package ma.ensaj.pets

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.ensaj.pets.adapter.PetsAdapter
import ma.ensaj.pets.adapter.PetsVaccMedsAdapter
import ma.ensaj.pets.api.PetApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.Pet
import ma.ensaj.pets.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetsAppointmentsFragment : Fragment() {
    private lateinit var petsRecyclerView: RecyclerView
    private lateinit var addPetButton: Button
    private lateinit var petsVaccMedsAdapter: PetsVaccMedsAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate le layout pour ce fragment
        return inflater.inflate(R.layout.fragment_pets_appointments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Appeler setHasOptionsMenu pour indiquer que ce fragment a un menu
        setHasOptionsMenu(true)

        val backArrow = view.findViewById<ImageView>(R.id.imageView6)

        sessionManager = SessionManager(requireContext())
        val ownerId = sessionManager.fetchUserId()

        if (ownerId == -1L || sessionManager.fetchAuthToken() == null) {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
            return
        }

        Log.d("userid", "$ownerId")

        setupViews(view) // Passer 'view' ici
        loadPets(ownerId)

        backArrow.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupViews(view: View) {
        petsRecyclerView = view.findViewById(R.id.petsRecyclerView)

        petsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        petsVaccMedsAdapter = PetsVaccMedsAdapter(
            petsList = emptyList(),
            onPetClick = { pet -> navigateToAppointments(pet) },
            context = requireContext()
        )
        petsRecyclerView.adapter = petsVaccMedsAdapter

    }

    private fun loadPets(ownerId: Long) {
        val petApi = RetrofitClient.instance.create(PetApi::class.java)

        petApi.getPetsByOwnerId(ownerId).enqueue(object : Callback<List<Pet>> {
            override fun onResponse(call: Call<List<Pet>>, response: Response<List<Pet>>) {
                if (response.isSuccessful) {
                    val petsList = response.body()
                    if (petsList != null) {
                        petsVaccMedsAdapter.updatePetsList(petsList)
                    } else {
                        Toast.makeText(requireContext(), "No pets found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (response.code() == 401) {
                        sessionManager.clearSession()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireContext(), "Failed to load pets", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Pet>>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToEditPet(pet: Pet) {
        val intent = Intent(requireContext(), EditPetActivity::class.java)
        intent.putExtra("PET_ID", pet.id)
        startActivity(intent)
    }

    private fun navigateToAppointments(pet: Pet) {
        val intent = Intent(requireContext(), AppointmentsActivity::class.java)
        intent.putExtra("PET_ID", pet.id)
        startActivity(intent)
    }

    private fun confirmDeletePet(pet: Pet) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmation de suppression")
            .setMessage("Êtes-vous sûr de vouloir supprimer cet animal ?")
            .setPositiveButton("Oui") { _, _ -> deletePet(pet) }
            .setNegativeButton("Non", null)
            .show()
    }

    private fun deletePet(pet: Pet) {
        val petApi = RetrofitClient.instance.create(PetApi::class.java)

        pet.id?.let {
            petApi.deletePet(it).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Animal supprimé", Toast.LENGTH_SHORT).show()
                        loadPets(sessionManager.fetchUserId())
                    } else {
                        Toast.makeText(requireContext(), "Échec de la suppression", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "Erreur réseau : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        sessionManager.clearSession()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }
}