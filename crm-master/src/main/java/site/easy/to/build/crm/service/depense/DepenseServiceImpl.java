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
    public Depense updateDepense(Depense depenseDetails) {
            return depenseRepository.save(depenseDetails);
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
    public boolean depasseBudget(int customer_id,double montantSupp){
        double sommeBudget=budgetService.getTotalBudgetByCustomerId(customer_id);
        double totalDepense=getTotalDepensesByCustomerId(customer_id).orElse(0.0);
        double newTotalDepense=totalDepense+montantSupp;
        return  newTotalDepense>sommeBudget;
    }

    @Override
    public double totalDepense() {
        return depenseRepository.findTotalDepenses();
    }

    @Override
    public List<Depense> listeDepenseLead() {
        return depenseRepository.findListeDepensesLead();
    }
    @Override
    public List<Depense> listeDepenseLeadValide() {
        return depenseRepository.findListeDepensesLeadValide();
    }

    @Override
    public List<Depense> listeDepenseTicket() {
        return depenseRepository.findListeDepensesTicket();
    }
    @Override
    public List<Depense> listeDepenseTicketValide() {
        return depenseRepository.findListeDepensesTicketValide();
    }

    @Override
    public Optional<Double> sommeDepenseLead() {
        return depenseRepository.findSommeDepensesLead();
    }

    @Override
    public Optional<Double> sommeDepenseTicket() {
        return depenseRepository.findSommeDepensesTicket();
    }

    @Override
    public List<Depense> findByCustomerByEtat(int id) {
        return depenseRepository.findByEtatAndCustomerId(id);
    }
}