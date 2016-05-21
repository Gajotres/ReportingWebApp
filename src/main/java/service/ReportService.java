package service;
 
import java.io.File;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import controller.ReportController;
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
    
    public String getRelativeJarPath() {
    	
    	String jarPath = null;
    	
        Pattern prefix = Pattern.compile("jar:file:\\/");
        Pattern prefixSTS = Pattern.compile("file:\\/");
        Pattern suffix = Pattern.compile("[\\w|.|\\-|\\_]{1,}.jar!/$");
    	
		jarPath = ReportController.class.getProtectionDomain().getCodeSource().getLocation().toString();
		jarPath = prefix.matcher(jarPath).replaceAll("");
		jarPath = prefixSTS.matcher(jarPath).replaceAll("");
		jarPath = suffix.matcher(jarPath).replaceAll(""); 
    	
    	return jarPath;
	}    
}