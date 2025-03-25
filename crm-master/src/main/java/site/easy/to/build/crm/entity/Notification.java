package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import site.easy.to.build.crm.entity.Customer;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name="message")
    private String message;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column(name="date_notif")
    private LocalDateTime dateNotif;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getDateNotif() {
        return dateNotif;
    }

    public void setDateNotif(LocalDateTime dateNotif) {
        this.dateNotif = dateNotif;
    }
}
