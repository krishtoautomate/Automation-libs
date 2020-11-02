package Test;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JiraTest {

	public static void main(String[] args) {
		
		
		
		
		// TODO Auto-generated method stub
		
		Date date = new Date();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		sdf.setTimeZone(TimeZone.getTimeZone("EST"));
		
		String dateANDtime = sdf.format(date.getTime());
		
		// Create new JSON Object
        JsonObject info = new JsonObject();
        
        // Create Inner JSON Object 
        JsonObject textExecution = new JsonObject();
        textExecution.addProperty("summary", "BETA RELEASE PLAN");
        textExecution.addProperty("description", "BETA MAR 15.0");
        textExecution.addProperty("revision", "MAR 15.0");
        textExecution.addProperty("startDate", dateANDtime);
        textExecution.addProperty("finishDate", dateANDtime);
        info.add("info", textExecution);
        
        // Create JSON Array 
        JsonArray tests = new JsonArray();
        
        JsonObject test = new JsonObject();
        test.addProperty("testKey", "MAEAUTO-1051");
        test.addProperty("start", dateANDtime);
        test.addProperty("finish", dateANDtime);
        test.addProperty("comment", "ios tests");
        test.addProperty("status", "PASS");
        
        tests.add(test);//add tests to array
        
//        System.out.println(tests);
        
        info.add("tests", tests);
        System.out.println(info.toString());
        
        
      //Write JSON file
        try (FileWriter file = new FileWriter("test-output/jira-result.json")) {
 
            file.write(info.toString());
            file.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

}
