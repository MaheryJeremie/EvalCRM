package site.easy.to.build.crm.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.RedirectView;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.service.notification.NotificationService;
import site.easy.to.build.crm.service.seuil.SeuilLimiteService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/depenses")
public class DepenseController {

    private final DepenseService depenseService;
    private final LeadService leadService;
    private final TicketService ticketService;
    private final SeuilLimiteService seuilLimiteService;
    private final CustomerService customerService;
    private final NotificationService notificationService;

    @Autowired
    public DepenseController(DepenseService depenseService, LeadService leadService, TicketService ticketService, SeuilLimiteService seuilLimiteService, CustomerService customerService, NotificationService notificationService) {
        this.depenseService = depenseService;
        this.leadService = leadService;
        this.ticketService = ticketService;
        this.seuilLimiteService = seuilLimiteService;
        this.customerService = customerService;
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<?> createDepense(
            @ModelAttribute("depense") Depense depense,
            @RequestParam(value = "leadId", required = false) Integer leadId,
            @RequestParam(value = "ticketId", required = false) Integer ticketId,
            BindingResult result
    ) {
        boolean isLead = false;
        boolean isAtteint = false;
        boolean depasseBudget = false;
        String avertissement = null;
        String redirectUrl = "someRedirectUrl";

        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid data provided for depense."));
            }

            List<SeuilLimite> seuilLimites = seuilLimiteService.getAllSorted();
            if (seuilLimites.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "SeuilLimite not configured."));
            }

            double pourcentage = seuilLimites.get(0).getPourcentage();

            // Traitement Lead ou Ticket
            if (leadId != null) {
                Lead lead = leadService.findByLeadId(leadId);
                depense.setLead(lead);
                depense.setDateDepense(lead.getCreatedAt());
                isLead = true;
            } else if (ticketId != null) {
                Ticket ticket = ticketService.findByTicketId(ticketId);
                if (ticket == null) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Ticket not found"));
                }
                depense.setTicket(ticket);
                depense.setDateDepense(ticket.getCreatedAt());
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Neither Lead nor Ticket Id was provided"));
            }

            int customerId = isLead ? depense.getLead().getCustomer().getCustomerId() : depense.getTicket().getCustomer().getCustomerId();

            depasseBudget = depenseService.depasseBudget(customerId, depense.getMontant());

            if (depasseBudget) {
                avertissement = "La depense est en attente de confirmation";
                depense.setEtat(0);
                Notification notification = new Notification();
                notification.setMessage("Il y a une nouvelle depense en attente de confirmation car elle dépasse le budget");
                notification.setCustomer(customerService.findByCustomerId(customerId));
                notification.setDateNotif(depense.getDateDepense());
                notificationService.save(notification);

            }
            depenseService.saveDepense(depense);

                if (!depasseBudget){
                    isAtteint = depenseService.checkSeuilAtteint(customerId, pourcentage);
                    if (isAtteint) {
                        Notification notification = new Notification();
                        notification.setMessage(pourcentage + "% of the budget has been used.");
                        notification.setCustomer(customerService.findByCustomerId(customerId));
                        notification.setDateNotif(depense.getDateDepense());
                        notification.setDateNotif(depense.getDateDepense());
                        notificationService.save(notification);
                    }
                }



            redirectUrl = isLead ? "/employee/lead/show/" + leadId : "/employee/ticket/show-ticket/" + ticketId;
            return ResponseEntity.ok(Map.of(
                    "redirectUrl", redirectUrl,
                    "avertissement", avertissement != null ? avertissement : ""
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "An error occurred: " + e.getMessage()));
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

    @GetMapping
    public ResponseEntity<?> getAllDepenses() {
        try {
            List<Depense> depenses = depenseService.getAllDepenses();
            return ResponseEntity.ok(depenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving depenses: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepenseById(@PathVariable int id) {
        try {
            Depense depense = depenseService.getDepenseById(id);
            if (depense != null) {
                return ResponseEntity.ok(depense);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Depense not found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the depense: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepense(@PathVariable int id) {
        try {
            Depense depense = depenseService.getDepenseById(id);
            if (depense != null) {
                depenseService.deleteDepense(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Depense not found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the depense: " + e.getMessage());
        }
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<?> getDepensesByTicketId(@PathVariable int ticketId) {
        try {
            Depense depense = depenseService.getDepensesByTicketId(ticketId);
            if (depense != null) {
                return ResponseEntity.ok(depense);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Depense not found for ticket ID: " + ticketId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the depense: " + e.getMessage());
        }
    }

    @GetMapping("/lead/{leadId}")
    public ResponseEntity<?> getDepensesByLeadId(@PathVariable int leadId) {
        try {
            Depense depense = depenseService.getDepensesByLeadId(leadId);
            if (depense != null) {
                return ResponseEntity.ok(depense);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Depense not found for lead ID: " + leadId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the depense: " + e.getMessage());
        }
    }
    @GetMapping("/total")
    public ResponseEntity<?> getTotal(){
        try{
            double total=depenseService.totalDepense();
            return ResponseEntity.ok(total);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}