package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import model.ReportFile;
import model.Report01;
//import service.ReportService;

@RestController
@RequestMapping("/generate")
public class ReportController {
	
    //@Autowired
    //ReportService rs;	
    
    @CrossOrigin()
    @RequestMapping("/test")
    public void getAll () {
        System.out.println("******************************");;
    }	
	
    @CrossOrigin()
    @RequestMapping(value = "/report01",method = RequestMethod.POST)
    @ResponseBody
    public void updateEmployeeData(@RequestBody @Valid final Report01 report01) {
    	    	
	    try {
	    	
	    	int tenantId = report01.getTenant_id();
	    	String startDate = report01.getStart_date();
	    	String endDate = report01.getEnd_date();	 
	    		    	
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("pan.bat -file=\"KettleAutoReporting.ktr\" " + startDate + " " + endDate +" " + tenantId + " -Level=Logging > output.log");
            
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            
            String line=null;

            while((line=input.readLine()) != null) {
                System.out.println(line);
            }

            int exitVal = pr.waitFor();
            
	    } catch (Exception ex) {
	    	System.out.println("Error getting report parameters: " + ex.toString());  
	    }
    } 
    
    @CrossOrigin()
    @RequestMapping("/list/{name}")
    public List<ReportFile> getListOfReportFiles (@PathVariable("name") String name) {
    	
    	File folder = new File("c:\\tmp\\Reporting\\Reports\\Reporting\\output\\");
    	List<ReportFile> fList = listFilesForFolder(folder);
    	    	
        return fList;
    } 
    
    private List<ReportFile> listFilesForFolder(File folder) {
    	
    	List<ReportFile> fList = new ArrayList<ReportFile>();    	
    	
    	try {    	
    		
	        for (final File fileEntry : folder.listFiles()) {
	            if (fileEntry.isDirectory()) {
	                listFilesForFolder(fileEntry);
	            } else {
	            	ReportFile file = new ReportFile();
	            	file.setName(fileEntry.getName());
	            	file.setLocation(fileEntry.getPath());
	            	
					BasicFileAttributes attr = Files.readAttributes(Paths.get(fileEntry.getAbsolutePath()), BasicFileAttributes.class);
					
	            	file.setCreated(attr.creationTime().toString());
	            	file.setSize(Long.toString(attr.size()));
							            	
	            	fList.add(file);
	            }
	        }
        
		} catch (IOException e) {
			e.printStackTrace();
		}        
        
        return fList;
    }
}
