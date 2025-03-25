package site.easy.to.build.crm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.service.budget.BudgetService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/budgets")
public class DashboardBudgetController {
    private final BudgetService budgetService;

    public DashboardBudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        try{
            List<Object[]>budgets=budgetService.getTotalByClient();
            return ResponseEntity.ok(budgets);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id){
        try{
            Budget budget=budgetService.getBudgetById(id);
            return ResponseEntity.ok(budget);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Map<String, Object> request, @PathVariable("id") int id) {
        try {
            double valeur = Double.parseDouble(request.get("valeur").toString());
            Budget budget = budgetService.getBudgetById(id);
            budget.setValeur(valeur);
            budgetService.saveBudget(budget);
            return ResponseEntity.ok(budget);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?>delete(@PathVariable("id") int id){
        try{
            budgetService.deleteBudget(id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/total")
    public ResponseEntity<?> total(){
        try{
            Double total=budgetService.getTotal();
            return ResponseEntity.ok(total);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
