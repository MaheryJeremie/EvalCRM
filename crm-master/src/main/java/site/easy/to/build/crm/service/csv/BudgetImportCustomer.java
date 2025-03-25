package site.easy.to.build.crm.service.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.service.budget.BudgetService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetImportCustomer {
    private final BudgetService budgetService;
    public BudgetImportCustomer(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    public List<Budget> importBudgetCsv(MultipartFile file, char separator, List<Customer> customerList) throws Exception {
        List<Budget> budgetList = new ArrayList<>();
        List<Budget> budgets = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withDelimiter(separator)
                     .withFirstRecordAsHeader()
                     .withIgnoreEmptyLines(true)
                     .withTrim())) {

            for (CSVRecord csvRecord : csvParser) {
                String customer_email = csvRecord.get("customer_email").trim();
                if(customer_email.isEmpty()) throw new Exception("email inexistant a la ligne "+csvRecord.getRecordNumber());

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

                String budget = csvRecord.get("Budget").trim();

                if(budget.isEmpty()) throw new Exception("budget inexistant a l ligne "+csvRecord.getRecordNumber());

                int indexVirgule = budget.indexOf(',');

                if(indexVirgule!=-1){
                    StringBuilder sb = new StringBuilder(budget);
                    sb.setCharAt(indexVirgule, '.');
                    budget = sb.toString();
                }

                double budget1 = Double.parseDouble(budget);

                if(budget1<0){
                    throw new Exception("montant negatif a la ligne "+csvRecord.getRecordNumber());
                }

                Budget budgetObject = new Budget();
                budgetObject.setCustomer(customer);
                budgetObject.setValeur(budget1);
                budgetObject.setDate(LocalDateTime.now());

                budgetList.add(budgetObject);
            }

            for (Budget budget : budgetList){
                Budget budget1 = budgetService.saveBudget(budget);
                budgets.add(budget1);
            }

        } catch (IOException e) {
            throw new Exception("Erreur de lecture du fichier CSV.", e);
        } catch (Exception exception) {
            throw new Exception("Erreur générale lors de l'importation du fichier CSV.", exception);
        }
        return budgets;
    }
}
