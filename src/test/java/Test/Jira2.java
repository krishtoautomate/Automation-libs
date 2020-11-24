package Test;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Jira2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Date date = new Date();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm.ssXXX");
		sdf.setTimeZone(TimeZone.getTimeZone("EST"));
		
		String dateANDtime = sdf.format(date.getTime());
		
		// Create new JSON Object
		JSONObject info = new JSONObject();
        
        // Create Inner JSON Object 
		JSONObject testPlan = new JSONObject();
		testPlan.put("summary", "BETA RELEASE PLAN");
		testPlan.put("description", "BETA MAR 15.0");
		testPlan.put("revision", "MAR 15.0");
		testPlan.put("startDate", dateANDtime);
		testPlan.put("finishDate", dateANDtime);
        info.put("info", testPlan);
        
        // Create JSON Array 
        JSONArray tests = new JSONArray();
        
        JSONObject test = new JSONObject();
        test.put("testKey", "MAEAUTO-1051");
        test.put("start", dateANDtime);
        test.put("finish", dateANDtime);
        test.put("comment", "ios tests");
        test.put("status", "PASS");
        
        tests.add(test);//add tests to array
        
//        System.out.println(tests);
        
        
        
        info.put("tests", tests);
        System.out.println(info.toJSONString());
        
        
      //Write JSON file
//        try (FileWriter file = new FileWriter("test-output/jira-result.json")) {
// 
//            file.write(info.toJSONString());
//            file.flush();
// 
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

	}

}
