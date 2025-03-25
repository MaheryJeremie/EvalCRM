package site.easy.to.build.crm.service.budget;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;

    @Autowired
    public BudgetServiceImpl(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    @Override
    public Budget getBudgetById(int id) {
        return budgetRepository.findById(id).orElse(null);
    }

    @Override
    public Budget saveBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public void deleteBudget(int id) {
        budgetRepository.deleteById(id);
    }

    @Override
    public List<Budget> getBudgetsByCustomerId(int customerId) {
        return budgetRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public double getTotalBudgetByCustomerId(int customerId) {
        Double total = budgetRepository.getTotalBudgetByCustomerId(customerId);
        return total != null ? total : 0.0;
    }

    @Override
    public double getTotal() {
        Double total = budgetRepository.getTotalBudget();
        return total != null ? total : 0.0;
    }

    @Override
    public List<Object[]> getTotalByClient() {
        return budgetRepository.findTotalBudgetByClient();
    }
}