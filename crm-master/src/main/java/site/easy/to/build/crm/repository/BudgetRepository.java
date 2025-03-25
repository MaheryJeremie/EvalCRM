package site.easy.to.build.crm.repository;

import site.easy.to.build.crm.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    List<Budget> findByCustomerCustomerId(int customerId);
    @Query("SELECT SUM(b.valeur) FROM Budget b WHERE b.customer.customerId = :customerId")
    Double getTotalBudgetByCustomerId(@Param("customerId") int customerId);

    @Query("SELECT SUM(b.valeur) FROM Budget b")
    Double getTotalBudget();
    @Query("SELECT b.customer.name, SUM(b.valeur) FROM Budget b GROUP BY b.customer.customerId, b.customer.name")
    List<Object[]> findTotalBudgetByClient();
}