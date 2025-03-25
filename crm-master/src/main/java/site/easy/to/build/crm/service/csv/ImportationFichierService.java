package site.easy.to.build.crm.service.csv;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.notification.NotificationService;
import site.easy.to.build.crm.service.seuil.SeuilLimiteService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
@Service
public class ImportationFichierService {
    private final CustomerService customerService;
    private final LeadService leadService;
    private final TicketService ticketService;
    private final UserService userService;
    private final DepenseService depenseService;
    private final BudgetService budgetService;
    private final NotificationService notifService;
    private final SeuilLimiteService seuilLimiteService;

    public ImportationFichierService(CustomerService customerService, LeadService leadService, TicketService ticketService, UserService userService, DepenseService depenseService, BudgetService budgetService, NotificationService notifService,SeuilLimiteService seuilLimiteService) {
        this.customerService = customerService;
        this.leadService = leadService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.depenseService = depenseService;
        this.budgetService = budgetService;
        this.notifService = notifService;
        this.seuilLimiteService = seuilLimiteService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void importationFeuille1(MultipartFile file, char separator, List<Customer> customerList,List<Budget> budgets)throws Exception{
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             @SuppressWarnings("deprecation")
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withDelimiter(separator))) {
            for (CSVRecord csvRecord : csvParser) {
                String customer_email = csvRecord.get("customer_email").trim();
                if(customer_email.isEmpty()) throw new Exception("email inexistant a la ligne "+csvRecord.getRecordNumber());
//                Customer customer = customerService.findByEmail(customer_email);
//                if(customer == null) throw new Exception("customer inexistant");
                Customer customer = null;
                boolean verification = false;
                for (int i = 0; i < customerList.size(); i++) {
                    if(customerList.get(i).getEmail().equalsIgnoreCase(customer_email)){
                        verification = true;
                        customer = customerList.get(i);
                        break;
                    }
                }
                if(!verification) throw new Exception("customer inexistant a la ligne "+csvRecord.getRecordNumber());
                if(customer == null) throw new Exception("customer inexistant pour l'email a l ligne "+csvRecord.getRecordNumber());
                String expense = csvRecord.get("expense").trim();
                if(expense.isEmpty()) throw new Exception("expense inexistant a l ligne "+csvRecord.getRecordNumber());
                int indexVirgule = expense.indexOf(',');
                if(indexVirgule!=-1){
                    StringBuilder sb = new StringBuilder(expense);
                    sb.setCharAt(indexVirgule, '.');
                    expense = sb.toString();
                }
                double depense = Double.parseDouble(expense);
                if(depense<0){
                    throw new Exception("montant negatif a la ligne "+csvRecord.getRecordNumber());
                }
                String subject_or_name = csvRecord.get("subject_or_name").trim();
                if(subject_or_name.isEmpty()) throw new Exception("subject_or_name inexistant a l ligne "+csvRecord.getRecordNumber());
                String status = csvRecord.get("status").trim();
                if(status.isEmpty()) throw new Exception("status inexistant a l ligne "+csvRecord.getRecordNumber());
                String type = csvRecord.get("type").trim();
                if(type.isEmpty()) throw new Exception("type inexistant a l ligne "+csvRecord.getRecordNumber());
                List<User> manager = userService.getUsersByRoleId(1);
                List<User> employe = userService.getUsersByRoleId(2);
                int maxUser = 2;
                int minUser = 1;
                Random random = new Random();
                int randomUser = random.nextInt(maxUser - minUser + 1) + minUser;
                if(status == null) throw new Exception("Status invalid a la ligne "+csvRecord.getRecordNumber());
                else{
                    if(type.equalsIgnoreCase("lead")){
                        if(!(status.equalsIgnoreCase("assign-to-sales") || status.equalsIgnoreCase("success") ||status.equalsIgnoreCase("archived") || status.equalsIgnoreCase("scheduled")  || status.equalsIgnoreCase("meeting-to-schedule") )){
                            throw new Exception("Status invalid a la ligne "+csvRecord.getRecordNumber());
                        }else{
                            int min = 0;
                            Lead lead= new Lead();
                            int maxManager =manager.size()-1;
                            int randomManager = random.nextInt(maxManager - min + 1) + min;
                            User managerLead = manager.get(randomManager);
                            if(randomUser ==1){
                                lead.setName(subject_or_name);
                                lead.setCustomer(customer);
                                lead.setManager(managerLead);
                                lead.setPhone("0300000000");
                                lead.setEmployee(managerLead);
                                lead.setStatus(status);
                                lead.setGoogleDrive(false);
                                lead.setCreatedAt(LocalDateTime.now());
                                lead= leadService.save(lead);
                                Depense depense1 = new Depense();
                                depense1.setMontant(depense);
                                depense1.setLead(lead);
                                depense1.setDateDepense(LocalDateTime.now());
                                SeuilLimite seuilLimite=seuilLimiteService.getAllSorted().get(0);
                                double pourcentage=seuilLimite.getPourcentage();
                                boolean depasseBudget = depenseService.depasseBudget(customer.getCustomerId(), depense1.getMontant());
                                if (depasseBudget){
                                    Notification notification = new Notification();
                                    notification.setMessage("Il y a une nouvelle depense en attente de confirmation car elle dépasse le budget");
                                    notification.setCustomer(customerService.findByCustomerId(customer.getCustomerId()));
                                    notification.setDateNotif(depense1.getDateDepense());
                                    notifService.save(notification);
                                }
                                depense1 =  depenseService.saveDepense(depense1);
                                if (!depasseBudget){
                                    boolean isAtteint = depenseService.checkSeuilAtteint(customer.getCustomerId(), pourcentage);
                                    if (isAtteint) {
                                        Notification notification = new Notification();
                                        notification.setMessage(pourcentage + "% of the budget has been used.");
                                        notification.setCustomer(customerService.findByCustomerId(customer.getCustomerId()));
                                        notification.setDateNotif(depense1.getDateDepense());
                                        notifService.save(notification);
                                    }
                                }
                            }else{
                                int max = employe.size()-1;
                                lead.setName(subject_or_name);
                                lead.setCustomer(customer);
                                int randomEmploye = random.nextInt(max - min + 1) + min;
                                User employeLead = employe.get(randomEmploye);
                                lead.setManager(managerLead);
                                lead.setPhone("0300000000");
                                lead.setEmployee(employeLead);
                                lead.setStatus(status);
                                lead.setGoogleDrive(false);
                                lead.setCreatedAt(LocalDateTime.now());
                                lead= leadService.save(lead);
                                Depense depense1 = new Depense();
                                depense1.setMontant(depense);
                                depense1.setLead(lead);
                                depense1.setDateDepense(LocalDateTime.now());
                                SeuilLimite seuilLimite=seuilLimiteService.getAllSorted().get(0);
                                double pourcentage=seuilLimite.getPourcentage();
                                boolean depasseBudget = depenseService.depasseBudget(customer.getCustomerId(), depense1.getMontant());
                                if (depasseBudget){
                                    Notification notification = new Notification();
                                    notification.setMessage("Il y a une nouvelle depense en attente de confirmation car elle dépasse le budget");
                                    notification.setCustomer(customerService.findByCustomerId(customer.getCustomerId()));
                                    notification.setDateNotif(depense1.getDateDepense());
                                    notifService.save(notification);
                                }
                                depense1 =  depenseService.saveDepense(depense1);
                                if (!depasseBudget){
                                    boolean isAtteint = depenseService.checkSeuilAtteint(customer.getCustomerId(), pourcentage);
                                    if (isAtteint) {
                                        Notification notification = new Notification();
                                        notification.setMessage(pourcentage + "% of the budget has been used.");
                                        notification.setCustomer(customerService.findByCustomerId(customer.getCustomerId()));
                                        notification.setDateNotif(depense1.getDateDepense());
                                        notifService.save(notification);
                                    }
                                }
                            }
                        }
                    } else if (type.equalsIgnoreCase("ticket")) {
                        if(!( status.equalsIgnoreCase("archived") || status.equalsIgnoreCase("escalated") ||status.equalsIgnoreCase("pending-customer-response") ||status.equalsIgnoreCase("reopened") ||status.equalsIgnoreCase("closed") || status.equalsIgnoreCase("open") || status.equalsIgnoreCase("assigned") ||status.equalsIgnoreCase("on-hold") || status.equalsIgnoreCase("in-progress")  || status.equalsIgnoreCase("resolved") )){
                            throw new Exception("Status invalid a la ligne "+csvRecord.getRecordNumber());
                        }else{
                            int min = 0;
                            Ticket ticket = new Ticket();
                            int maxManager =manager.size()-1;
                            int randomManager = random.nextInt(maxManager - min + 1) + min;
                            User managerLead = manager.get(randomManager);
                            if(randomUser ==1){
                                ticket.setSubject(subject_or_name);
                                ticket.setDescription("csv");
                                ticket.setStatus(status);
                                ticket.setPriority("low");
                                ticket.setCustomer(customer);
                                ticket.setManager(managerLead);
                                ticket.setEmployee(managerLead);
                                ticket.setCreatedAt(LocalDateTime.now());
                                ticket = ticketService.save(ticket);
                                Depense depense1 = new Depense();
                                depense1.setMontant(depense);
                                depense1.setTicket(ticket);
                                depense1.setDateDepense(LocalDateTime.now());
                                SeuilLimite seuilLimite=seuilLimiteService.getAllSorted().get(0);
                                double pourcentage=seuilLimite.getPourcentage();
                                boolean depasseBudget = depenseService.depasseBudget(customer.getCustomerId(), depense1.getMontant());
                                if (depasseBudget){
                                    Notification notification = new Notification();
                                    notification.setMessage("Il y a une nouvelle depense en attente de confirmation car elle dépasse le budget");
                                    notification.setCustomer(customerService.findByCustomerId(customer.getCustomerId()));
                                    notification.setDateNotif(depense1.getDateDepense());
                                    notifService.save(notification);
                                }
                                depense1 =  depenseService.saveDepense(depense1);
                                if (!depasseBudget){
                                    boolean isAtteint = depenseService.checkSeuilAtteint(customer.getCustomerId(), pourcentage);
                                    if (isAtteint) {
                                        Notification notification = new Notification();
                                        notification.setMessage(pourcentage + "% of the budget has been used.");
                                        notification.setCustomer(customerService.findByCustomerId(customer.getCustomerId()));
                                        notification.setDateNotif(depense1.getDateDepense());
                                        notifService.save(notification);
                                    }
                                }

                            }else{
                                int max = employe.size()-1;
                                int randomEmploye = random.nextInt(max - min + 1) + min;
                                User employeLead = employe.get(randomEmploye);
                                ticket.setSubject(subject_or_name);
                                ticket.setDescription("csv");
                                ticket.setStatus(status);
                                ticket.setPriority("low");
                                ticket.setCustomer(customer);
                                ticket.setManager(managerLead);
                                ticket.setEmployee(employeLead);
                                ticket.setCreatedAt(LocalDateTime.now());
                                ticket = ticketService.save(ticket);
                                Depense depense1 = new Depense();
                                depense1.setMontant(depense);
                                depense1.setTicket(ticket);
                                depense1.setDateDepense(LocalDateTime.now());
                                SeuilLimite seuilLimite=seuilLimiteService.getAllSorted().get(0);
                                double pourcentage=seuilLimite.getPourcentage();
                                boolean depasseBudget = depenseService.depasseBudget(customer.getCustomerId(), depense1.getMontant());
                                if (depasseBudget){
                                    Notification notification = new Notification();
                                    notification.setMessage("Il y a une nouvelle depense en attente de confirmation car elle dépasse le budget");
                                    notification.setCustomer(customerService.findByCustomerId(customer.getCustomerId()));
                                    notification.setDateNotif(depense1.getDateDepense());
                                    notifService.save(notification);
                                }
                                depense1 =  depenseService.saveDepense(depense1);
                                if (!depasseBudget){
                                    boolean isAtteint = depenseService.checkSeuilAtteint(customer.getCustomerId(), pourcentage);
                                    if (isAtteint) {
                                        Notification notification = new Notification();
                                        notification.setMessage(pourcentage + "% of the budget has been used.");
                                        notification.setCustomer(customerService.findByCustomerId(customer.getCustomerId()));
                                        notification.setDateNotif(depense1.getDateDepense());
                                        notifService.save(notification);
                                    }
                                }
                            }
                        }
                    }else{
                        throw new Exception("Type invalid a la ligne "+csvRecord.getRecordNumber());
                    }
                }

            }
        }catch (Exception e){
            throw e;
        }
    }
}
