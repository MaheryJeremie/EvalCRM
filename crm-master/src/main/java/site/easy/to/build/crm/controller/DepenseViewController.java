package site.easy.to.build.crm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.lead.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import site.easy.to.build.crm.service.ticket.TicketService;

@Controller
@RequestMapping("/depenses")
public class DepenseViewController {

    private final LeadService leadService;
    private final TicketService ticketService;

    @Autowired
    public DepenseViewController(LeadService leadService,TicketService ticketService) {
        this.leadService = leadService;
        this.ticketService = ticketService;
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
}