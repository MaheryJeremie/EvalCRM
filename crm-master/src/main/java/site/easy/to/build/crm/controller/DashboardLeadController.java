package site.easy.to.build.crm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
        import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.util.AuthorizationUtil;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/depenseLead")
public class DashboardLeadController {
    private DepenseService depenseService;
    private LeadService leadService;

    public DashboardLeadController(DepenseService depenseService, LeadService leadService) {
        this.depenseService = depenseService;
        this.leadService = leadService;
    }
    @GetMapping()
    public ResponseEntity<?>getAllValide(){
        try{
            List<Depense> budgets=depenseService.listeDepenseLeadValide();
            return ResponseEntity.ok(budgets);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/leads")
    public ResponseEntity<?>getAll(){
        try{
            List<Depense> budgets=depenseService.listeDepenseLead();
            return ResponseEntity.ok(budgets);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/total")
    public ResponseEntity<?> total(){
        try{
            double total=depenseService.sommeDepenseLead().orElse(0.0);
            return ResponseEntity.ok(total);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepense(@PathVariable int id,@RequestBody Map<String, Object> request) {
        try{
            double valeur = Double.parseDouble(request.get("montant").toString());
            Depense depense= depenseService.getDepenseById(id);
            depense.setMontant(valeur);
            Depense updatedDepense = depenseService.updateDepense(depense);
            if (updatedDepense != null) {
                return ResponseEntity.ok(updatedDepense);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Depense not found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the depense: " + e.getMessage());
        }
    }
    @DeleteMapping("/lead/{id}")
    public ResponseEntity<?> deleteLead(@PathVariable("id") int id) {
        try{
            Lead lead = leadService.findByLeadId(id);
            leadService.delete(lead);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
}
