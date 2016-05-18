package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.TenantsDao;
import model.ReportFile;
import model.Tenants;
import model.Report01;
import service.ReportService;


@RestController
@RequestMapping("/generate")
public class ReportController {
	    	
    @Autowired
    ReportService rs;

    @Autowired
    TenantsDao eDao;   	
	
    @CrossOrigin()
    @RequestMapping("/getTenantList")
    public Iterable<Tenants> getAll () {
        return eDao.findAll();
    }    
    
    @CrossOrigin()
    @RequestMapping(value = "/Report01",method = RequestMethod.POST)
    @ResponseBody
    public boolean updateEmployeeData(@RequestBody @Valid final Report01 report01) {
    	    	
	    try {
	    	
	    	int tenantId = report01.getTenant_id();
	    	String startDate = report01.getStart_date();
	    	String endDate = report01.getEnd_date();	 
	    		    	
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("c:\\Program Files\\Kettle6.0\\pan.bat /rep:\"Repo\" /trans:\"ReportingTest\" \"" + startDate + "\" \"" + endDate + "\" " + tenantId + " /user:admin /pass:admin /dir:\"/Jednokratni/\" /level:Basic > log.txt");
            
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            
            String line=null;

            while((line=input.readLine()) != null) {
                System.out.println(line);
            }

            int exitVal = pr.waitFor();
                                    
	    } catch (Exception ex) {
	    	System.out.println("Error getting report parameters: " + ex.toString());  
	    }
	    
	    return true;
    } 
    
    @CrossOrigin()
    @RequestMapping("/list/{name}")
    public List<ReportFile> getListOfReportFiles (@PathVariable("name") String name) {
    	
    	String reportLocation = "c:\\Programiranje\\Reporting\\" + name + "\\output\\";     	
    	File folder = new File(reportLocation);
    	List<ReportFile> fList = listFilesForFolder(folder);
    	    	
        return fList;
    }
    
    @CrossOrigin()
    @RequestMapping(value = "/get/{name}/file/{filename}", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> getReportBlob (@PathVariable("name") String name, @PathVariable("filename") String filename) {
    	
        FileInputStream fileStream;
        try {
        	
        	String fileAbsolutePath = "c:\\Programiranje\\Reporting\\" + name + "\\output\\" + filename + ".pdf";
        	
            fileStream = new FileInputStream(new File(fileAbsolutePath));
            
            byte[] contents = IOUtils.toByteArray(fileStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.setContentDispositionFormData(filename, filename);
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
            return response;
        } catch (FileNotFoundException e) {
           System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
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
