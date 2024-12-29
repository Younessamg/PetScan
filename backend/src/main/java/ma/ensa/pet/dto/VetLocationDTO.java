package ma.ensa.pet.dto;

import lombok.Data;

@Data
public class VetLocationDTO {
    private Long id;
    private String fullName;
    private String clinicAddress;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
    private Double distance; // Distance in kilometers
    private String workingHours;
    private boolean emergencyService;
}