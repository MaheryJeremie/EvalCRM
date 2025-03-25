package site.easy.to.build.crm.service.depense;

import site.easy.to.build.crm.entity.Depense;

import java.util.List;
import java.util.Optional;

public interface DepenseService {
    List<Depense> getAllDepenses();

    Depense getDepenseById(int id);

    Depense saveDepense(Depense depense);

    Depense updateDepense(Depense depenseDetails);

    void deleteDepense(int id);

    Depense getDepensesByTicketId(int ticketId);

    Depense getDepensesByLeadId(int leadId);
    public Optional<Double> getTotalDepensesByCustomerId(int customerId);
    public boolean checkSeuilAtteint(int customer_id,double pourcentage);
    public boolean depasseBudget(int customer_id,double montantsupp);
    public double totalDepense();
    public Optional<Double> sommeDepenseTicket();
    public Optional<Double> sommeDepenseLead();
    public List<Depense> listeDepenseTicket();
    public List<Depense> listeDepenseTicketValide();
    public List<Depense>  listeDepenseLead();
    public List<Depense>  listeDepenseLeadValide();
    public List<Depense> findByCustomerByEtat(int id);

}