package site.easy.to.build.crm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.util.AuthorizationUtil;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/depenseTicket")
public class DashboardTicketController {
    private DepenseService depenseService;
    private TicketService ticketService;

    public DashboardTicketController(DepenseService depenseService, TicketService ticketService) {
        this.depenseService = depenseService;
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<?>getAllValide(){
        try{
            List<Depense> budgets=depenseService.listeDepenseTicketValide();
            return ResponseEntity.ok(budgets);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/tickets")
    public ResponseEntity<?>getAll(){
        try{
            List<Depense> budgets=depenseService.listeDepenseTicket();
            return ResponseEntity.ok(budgets);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/total")
    public ResponseEntity<?> total(){
        try{
            double total=depenseService.sommeDepenseTicket().orElse(0.0);
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
    @DeleteMapping("/ticket/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable("id") int id, Authentication authentication) {
        try{
            Ticket lead = ticketService.findByTicketId(id);
            ticketService.delete(lead);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
}
