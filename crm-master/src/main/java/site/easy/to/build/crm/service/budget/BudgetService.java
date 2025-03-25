package site.easy.to.build.crm.service.budget;

import site.easy.to.build.crm.entity.Budget;

import java.util.List;

public interface BudgetService {
    List<Budget> getAllBudgets();

    Budget getBudgetById(int id);

    Budget saveBudget(Budget budget);

    void deleteBudget(int id);

    List<Budget> getBudgetsByCustomerId(int customerId);

    double getTotalBudgetByCustomerId(int customerId);
    double getTotal();
    List<Object[]> getTotalByClient();
}