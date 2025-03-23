package site.easy.to.build.crm.service.depense;

import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.repository.BudgetRepository;
import site.easy.to.build.crm.repository.DepenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.repository.SeuilLimiteRepository;
import site.easy.to.build.crm.service.budget.BudgetService;

import java.util.List;
import java.util.Optional;

@Service
public class DepenseServiceImpl implements DepenseService {

    private final DepenseRepository depenseRepository;
    private final BudgetService budgetService;


    @Autowired
    public DepenseServiceImpl(DepenseRepository depenseRepository, BudgetService budgetService) {
        this.depenseRepository = depenseRepository;
        this.budgetService = budgetService;
    }

    @Override
    public List<Depense> getAllDepenses() {
        return depenseRepository.findAll();
    }

    @Override
    public Depense getDepenseById(int id) {
        return depenseRepository.findById(id).orElse(null);
    }

    @Override
    public Depense saveDepense(Depense depense) {
        return depenseRepository.save(depense);
    }

    @Override
    public Depense updateDepense(int id, Depense depenseDetails) {
        Depense existingDepense = depenseRepository.findById(id).orElse(null);
        if (existingDepense != null) {
            existingDepense.setMontant(depenseDetails.getMontant());
            existingDepense.setDateDepense(depenseDetails.getDateDepense());
            existingDepense.setTicket(depenseDetails.getTicket());
            existingDepense.setLead(depenseDetails.getLead());
            return depenseRepository.save(existingDepense);
        }
        return null; // Retourne null si la dépense n'existe pas
    }

    @Override
    public void deleteDepense(int id) {
        depenseRepository.deleteById(id);
    }

    @Override
    public Depense getDepensesByTicketId(int ticketId) {
        return depenseRepository.findByTicketTicketId(ticketId);
    }

    @Override
    public Depense getDepensesByLeadId(int leadId) {
        return depenseRepository.findByLeadLeadId(leadId);
    }
    public Optional<Double> getTotalDepensesByCustomerId(int customerId) {
        return depenseRepository.findTotalDepensesByCustomerId(customerId);
    }
    public boolean checkSeuilAtteint(int customer_id,double pourcentage){
        double sommeBudget=budgetService.getTotalBudgetByCustomerId(customer_id);
        double totalDepense=getTotalDepensesByCustomerId(customer_id).orElse(0.0);
        double pourcentageMontant=(pourcentage*sommeBudget)/100;
        return totalDepense>=pourcentageMontant;
    }
}