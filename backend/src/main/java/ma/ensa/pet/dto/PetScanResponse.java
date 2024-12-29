package ma.ensa.pet.dto;

public record PetScanResponse(
        String name,
        String species,
        String breed,
        String color,
        String description,
        String ownerPhone,
        String ownerAddress
) {
}
