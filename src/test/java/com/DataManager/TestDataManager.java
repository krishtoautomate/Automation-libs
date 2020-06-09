package com.DataManager;
import java.io.File;
/**
* Created by Krish on 21.07.2018.
**/
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class TestDataManager {
	

	String filePath;
	String jsonValue;
	
	public TestDataManager(String PathtoJSONfile) {
		this.setFilePath(PathtoJSONfile);
	}
	
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public synchronized String getJsonValue(int index, String key) throws FileNotFoundException, IOException, ParseException {
		
		Object obj = new JSONParser().parse(new FileReader(new File(filePath)));
		JSONArray jsonArray = (JSONArray) obj;
		
		String jsonValue = ((JSONObject)jsonArray.get(index)).get(key).toString();
		
		return jsonValue;
	}
	
}
