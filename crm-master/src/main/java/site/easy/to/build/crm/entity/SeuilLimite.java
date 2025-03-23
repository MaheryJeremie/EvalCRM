package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seuil_limite")
public class SeuilLimite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "pourcentage", nullable = false)
    private double pourcentage;

    @Column(name = "date_modif", nullable = false)
    private LocalDateTime dateModif;

    public SeuilLimite() {
    }

    public SeuilLimite(int id, double pourcentage, LocalDateTime dateModif) {
        this.id = id;
        this.pourcentage = pourcentage;
        this.dateModif = dateModif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public LocalDateTime getDateModif() {
        return dateModif;
    }

    public void setDateModif(LocalDateTime dateModif) {
        this.dateModif = dateModif;
    }
}
