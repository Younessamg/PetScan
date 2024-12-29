package ma.ensaj.pets

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ma.ensaj.pets.api.VeterinarianApi
import ma.ensaj.pets.config.RetrofitClient
import ma.ensaj.pets.dto.VetLocationDTO
import ma.ensaj.pets.dto.Veterinarian
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var citySearch: AutoCompleteTextView
    private lateinit var btnNearbyVets: Button
    private lateinit var btnEmergencyVets: Button
    private lateinit var btnCityVets: Button
    private var userLocation: LatLng? = null
    private var vetApi: VeterinarianApi? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate le layout pour ce fragment
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        vetApi = RetrofitClient.instance.create(VeterinarianApi::class.java)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initializeViews(view)
        setupButtonListeners()
    }

    private fun initializeViews(view: View) {
        citySearch = view.findViewById(R.id.citySearch)
        btnNearbyVets = view.findViewById(R.id.btnNearbyVets)
        btnEmergencyVets = view.findViewById(R.id.btnEmergencyVets)
        btnCityVets = view.findViewById(R.id.btnCityVets)
    }

    private fun setupButtonListeners() {
        btnNearbyVets.setOnClickListener {
            showLoading("Recherche des vétérinaires à proximité...")
            loadNearbyVets()
        }
        btnEmergencyVets.setOnClickListener {
            showLoading("Recherche des vétérinaires d'urgence...")
            loadEmergencyVets()
        }
        btnCityVets.setOnClickListener {
            if (citySearch.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez entrer une ville", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showLoading("Recherche des vétérinaires dans ${citySearch.text}...")
            loadVetsByCity()
        }
    }

    private fun showLoading(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()
        requestLocationPermissionAndGetLocation()
    }

    private fun requestLocationPermissionAndGetLocation() {
        if (checkLocationPermission()) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getCurrentLocation() {
        if (!checkLocationPermission()) return

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                userLocation = LatLng(it.latitude, it.longitude)
                updateMapWithUserLocation()
            } ?: run {
                Toast.makeText(requireContext(), "Impossible d'obtenir votre position", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun updateMapWithUserLocation() {
        userLocation?.let { location ->
            mMap.clear()
            mMap.addMarker(MarkerOptions()
                .position(location)
                .title("Votre position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        }
    }

    private fun loadNearbyVets() {
        userLocation?.let { location ->
            vetApi?.getNearbyVeterinarians(location.latitude, location.longitude, 10.0)
                ?.enqueue(object : Callback<List<VetLocationDTO>> {
                    override fun onResponse(
                        call: Call<List<VetLocationDTO>>,
                        response: Response<List<VetLocationDTO>>
                    ) {
                        if (response.isSuccessful) {
                            updateMapWithVets(response.body() ?: emptyList(), false)
                        } else {
                            showError("Erreur lors du chargement des vétérinaires")
                        }
                    }

                    override fun onFailure(call: Call<List<VetLocationDTO>>, t: Throwable) {
                        showError("Erreur réseau: ${t.localizedMessage}")
                    }
                })
        } ?: showError("Position non disponible")
    }

    private fun loadEmergencyVets() {
        userLocation?.let { location ->
            vetApi?.getNearbyEmergencyVeterinarians(location.latitude, location.longitude, 10.0)
                ?.enqueue(object : Callback<List<VetLocationDTO>> {
                    override fun onResponse(
                        call: Call<List<VetLocationDTO>>,
                        response: Response<List<VetLocationDTO>>
                    ) {
                        if (response.isSuccessful) {
                            updateMapWithVets(response.body() ?: emptyList(), true)
                        } else {
                            showError("Erreur lors du chargement des vétérinaires d'urgence")
                        }
                    }

                    override fun onFailure(call: Call<List<VetLocationDTO>>, t: Throwable) {
                        showError("Erreur réseau: ${t.localizedMessage}")
                    }
                })
        } ?: showError("Position non disponible")
    }

    private fun loadVetsByCity() {
        val city = citySearch.text.toString()
        if (city.isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez entrer une ville", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading("Recherche des vétérinaires dans $city...")

        vetApi?.getVeterinariansByCity(city)?.enqueue(object : Callback<List<Veterinarian>> {
            override fun onResponse(
                call: Call<List<Veterinarian>>,
                response: Response<List<Veterinarian>>
            ) {
                if (response.isSuccessful) {
                    val veterinarians = response.body() ?: emptyList()

                    if (veterinarians.isEmpty()) {
                        showError("Aucun vétérinaire trouvé dans $city")
                    } else {
                        val vetLocations = veterinarians
                            .filter { vet -> vet.latitude != 0.0 && vet.longitude != 0.0 }
                            .map { vet ->
                                VetLocationDTO(
                                    id = vet.id,
                                    fullName = "${vet.firstName} ${vet.lastName}".trim(),
                                    clinicAddress = vet.clinicAddress,
                                    phoneNumber = vet.phoneNumber,
                                    latitude = vet.latitude,
                                    longitude = vet.longitude,
                                    distance = null,
                                    workingHours = vet.workingHours,
                                    emergencyService = vet.emergencyService
                                )
                            }

                        if (vetLocations.isEmpty()) {
                            showError("Aucun vétérinaire avec des coordonnées valides trouvé dans $city")
                        } else {
                            updateMapWithVets(vetLocations, false)
                        }
                    }
                } else {
                    showError("Erreur lors du chargement des vétérinaires: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Veterinarian>>, t: Throwable) {
                showError("Erreur réseau: ${t.localizedMessage}")
            }
        })
    }

    private fun updateMapWithVets(vets: List<VetLocationDTO>, isEmergency: Boolean) {
        requireActivity().runOnUiThread {
            if (vets.isEmpty()) {
                Toast.makeText(requireContext(), "Aucun vétérinaire trouvé", Toast.LENGTH_SHORT).show()
                return@runOnUiThread
            }

            updateMapWithUserLocation()

            val builder = com.google.android.gms.maps.model.LatLngBounds.Builder()
            userLocation?.let { builder.include(it) }

            vets.forEach { vet ->
                val markerColor = if (vet.emergencyService || isEmergency)
                    BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_GREEN

                val vetPosition = LatLng(vet.latitude, vet.longitude)
                builder.include(vetPosition)

                val snippet = buildString {
                    append("Adresse: ${vet.clinicAddress}")
                    vet.phoneNumber?.let { append("\nTél: $it") }
                    vet.workingHours?.let { append("\nHoraires: $it") }
                    vet.distance?.let { append("\nDistance: ${String.format("%.1f", it)} km") }
                    if (vet.emergencyService) append("\nService d'urgence disponible")
                }

                mMap.addMarker(
                    MarkerOptions()
                        .position(vetPosition)
                        .title(vet.fullName)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                )
            }

            try {
                val bounds = builder.build()
                val padding = 100 // padding en pixels
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                mMap.animateCamera(cameraUpdate)
            } catch (e: Exception) {
                Log.e("MapsFragment", "Erreur lors de l'ajustement de la caméra", e)
                val firstVet = vets.first()
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    LatLng(firstVet.latitude, firstVet.longitude), 12f))
            }
        }
    }

    private fun showError(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(requireContext(), "Permission de localisation refusée", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}