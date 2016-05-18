package service;
 
import java.util.Hashtable;
import org.springframework.stereotype.Service;
import model.Tenants;
 
@Service
public class ReportService {

    Hashtable <String, Tenants> tenants = new Hashtable<String, Tenants>(); 
     
    public ReportService() {
 
    }
     
    public Tenants getEmployee(String id) {
        if(tenants.containsKey(id)) {
            return tenants.get(id);
        } else {
            return null;
        }
    }
     
    public Hashtable<String, Tenants> getAll () {
        return tenants;
    }
}