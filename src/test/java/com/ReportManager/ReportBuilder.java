package com.ReportManager;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ReportBuilder {
	
	public static List<Result> details;
	public final String resultPlaceholder = "<!-- INSERT_RESULTS -->";
	public final String templatePath = System.getProperty("user.dir")+ "/" + "src/main/resources" + "/" +"ReportTemplate"+ ".html";
			
	public ReportBuilder() {
	}
	
	public void initialize() {
		details = new ArrayList<Result>();
	}
	
	public void report(String TestCase, String browser, String status, String reason) {
			Result r = new Result(TestCase, browser, status, reason);
			details.add(r);
	}
	
	public void writeResults(String Path) {
		try {
			String reportIn = new String(Files.readAllBytes(Paths.get(templatePath)));
			for (int i = 0; i < details.size();i++) {
				
				String htmlStyle = "<td>";
				if(details.get(i).getStatus().contains("FAIL")){
					htmlStyle = "<td style=\"color:#FF0000\";>";
				
				
					reportIn = reportIn.replaceFirst(resultPlaceholder,
						
								"<tr><td>" + Integer.toString(i+1) 
									+ "</td><td>" + details.get(i).getTestCase() 
									+ "</td><td>" + details.get(i).getBrowser() + "</td>"
											+ htmlStyle + details.get(i).getStatus()  
							  + "</td></tr>" + resultPlaceholder);
				}
			}
		
			Files.write(Paths.get(Path),reportIn.getBytes(),StandardOpenOption.CREATE);
			
		} catch (Exception e) {
			System.out.println("Error when writing report file:\n" + e.toString());
		}
	}
}