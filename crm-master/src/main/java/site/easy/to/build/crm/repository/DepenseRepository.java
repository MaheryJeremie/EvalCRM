package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.easy.to.build.crm.entity.Depense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Integer> {
    Depense findByTicketTicketId(int ticketId);
    Depense findByLeadLeadId(int leadId);
    @Query(value = "SELECT SUM(d.montant) AS totalDepenses " +
            "FROM depenses d " +
            "LEFT JOIN trigger_ticket t ON d.ticket_id = t.ticket_id " +
            "LEFT JOIN trigger_lead l ON d.lead_id = l.lead_id " +
            "WHERE t.customer_id = :customerId OR l.customer_id =:customerId", nativeQuery = true)
    Optional<Double> findTotalDepensesByCustomerId(@Param("customerId") int customerId);
}

