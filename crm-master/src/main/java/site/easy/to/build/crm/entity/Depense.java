package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "depenses")
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "depense_id")
    private int id;

    @Column(name = "montant", nullable = false)
    private double montant;

    @Column(name = "date_depense", nullable = false)
    private LocalDateTime dateDepense;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDateTime getDateDepense() {
        return dateDepense;
    }

    public void setDateDepense(LocalDateTime dateDepense) {
        this.dateDepense = dateDepense;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }
}