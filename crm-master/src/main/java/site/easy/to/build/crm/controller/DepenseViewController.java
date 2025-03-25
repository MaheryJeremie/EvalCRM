package site.easy.to.build.crm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.util.AuthenticationUtils;

import java.util.List;

@Controller
@RequestMapping("/depenses")
public class DepenseViewController {

    private final LeadService leadService;
    private final TicketService ticketService;
    private final DepenseService depenseService;
    private final AuthenticationUtils authenticationUtils;

    @Autowired
    public DepenseViewController(LeadService leadService, TicketService ticketService, DepenseService depenseService, AuthenticationUtils authenticationUtils) {
        this.leadService = leadService;
        this.ticketService = ticketService;
        this.depenseService = depenseService;
        this.authenticationUtils = authenticationUtils;
    }




    @GetMapping("/create/lead/{leadId}")
    public String showCreateExpenseFormLead(@PathVariable int leadId, Model model) {
        Lead lead = leadService.findByLeadId(leadId);
        if (lead == null) {
            return "redirect:/leads";
        }
        model.addAttribute("depense",new Depense());
        model.addAttribute("lead", lead);
        return "depense/create-depense-lead";
    }
    @GetMapping("/create/ticket/{ticketId}")
    public String showCreateExpenseFormTicket(@PathVariable int ticketId, Model model) {
        Ticket ticket = ticketService.findByTicketId(ticketId);
        if (ticket == null) {
            return "redirect:/leads";
        }
        model.addAttribute("depense",new Depense());
        model.addAttribute("ticket", ticket);
        return "depense/create-depense-ticket";
    }
    @GetMapping("/attente")
    public String showMyNotif(Authentication authentication,Model model){
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        List<Depense> listes=depenseService.findByCustomerByEtat(userId);
        model.addAttribute("listes",listes);
        return "depense/depense-en-attente";
    }
    @GetMapping("/update/{id}")
    public String updateEtat(@PathVariable("id")int id,@RequestParam("etat")int etat){
        Depense depense=depenseService.getDepenseById(id);
        depense.setEtat(etat);
        depenseService.saveDepense(depense);
        return "redirect:/depenses/attente";

    }
}