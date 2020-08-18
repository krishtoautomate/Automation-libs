package com.DataManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonFileReader {


	String filePath;

	public JsonFileReader(String jsonfile) {
		this.setFilePath(jsonfile);
	}

	public synchronized String getJsonValue(int index, String key) throws FileNotFoundException, IOException, ParseException {
		
		Object obj = new JSONParser().parse(new FileReader(new File(filePath)));
		JSONArray jsonArray = (JSONArray) obj;
		
		String jsonValue = ((JSONObject)jsonArray.get(index)).get(key).toString();
		
		return jsonValue;
	}

	public synchronized int getObjIndex(String key, String value) throws IOException, ParseException {
		
		int objIndex = 0;
		
		FileReader fileReader = new FileReader(filePath);
		
		JSONArray jArray = (JSONArray) new JSONParser().parse(fileReader);
		
		for(int i = 0;i<jArray.size();i++) {
			if(((JSONObject)jArray.get(i)).get(key).toString().equals(value)) {
				objIndex = i;
				break;
			}
		}
		return objIndex;
	}
	
	public synchronized String getFilePath() {
		return filePath;
	}
	
	public synchronized void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
