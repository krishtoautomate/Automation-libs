package com.base;
/**
* Created by Krish on 21.07.2018.
**/
 
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
* Created by Krish on 21.01.2019.
*/
public class CapabilitiesManager {
	
	private DesiredCapabilities capabilities = new DesiredCapabilities();
//	private static final Logger log = Logger.getLogger(CapabilitiesManager.class);
	
	@SuppressWarnings("unchecked")
	public synchronized DesiredCapabilities loadJSONCapabilities(String capabilitiesJson, String capabilitiesName) {
		
		File buildCapabilities = new File(Constants.CAPABILITIES);
		if(buildCapabilities.exists()) 
			capabilitiesJson = Constants.CAPABILITIES;
	
		try {
			//read the json file
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(capabilitiesJson));

			//get an array from the JSON object
			JSONArray jsonArray= (JSONArray) jsonObject.get(capabilitiesName);
		
			JSONObject innerObj = (JSONObject) jsonArray.get(0);
			
			innerObj.keySet().forEach(key->{
//				log.info(key.toString() + ":" + innerObj.get(key));
				capabilities.setCapability(key.toString(), innerObj.get(key));
			});
				
		} catch (IOException | ParseException | NullPointerException ex) {
			ex.printStackTrace();
		}

		return capabilities;
	}
}
