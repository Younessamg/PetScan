package ma.ensa.pet.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "scan_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime scanDateTime;

    private String location;

    private String scannerIp;

    @Column(length = 500)
    private String additionalInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "qr_code_id", nullable = false)
    private QrCode qrCode;

    @PrePersist
    protected void onCreate() {
        scanDateTime = LocalDateTime.now();
    }
}