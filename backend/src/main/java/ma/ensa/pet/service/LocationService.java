package ma.ensa.pet.service;

import ma.ensa.pet.dto.VetLocationDTO;
import ma.ensa.pet.model.Veterinarian;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    private static final double EARTH_RADIUS = 6371; // Earth's radius in kilometers

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    public VetLocationDTO convertToLocationDTO(Veterinarian vet, double userLat, double userLon) {
        VetLocationDTO dto = new VetLocationDTO();
        dto.setId(vet.getId());
        dto.setFullName(vet.getFirstName() + " " + vet.getLastName());
        dto.setClinicAddress(vet.getClinicAddress());
        dto.setPhoneNumber(vet.getPhoneNumber());
        dto.setLatitude(vet.getLatitude());
        dto.setLongitude(vet.getLongitude());
        dto.setWorkingHours(vet.getWorkingHours());
        dto.setEmergencyService(vet.isEmergencyService());

        // Calculate distance from user
        double distance = calculateDistance(userLat, userLon, vet.getLatitude(), vet.getLongitude());
        dto.setDistance(Math.round(distance * 100.0) / 100.0); // Round to 2 decimal places

        return dto;
    }
}