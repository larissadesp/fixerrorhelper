package com.plugin.fixerrorhelper.core.gpt;

import org.json.JSONArray;
import org.json.JSONObject;

public interface GPTClient {
	
	public JSONObject call(JSONArray instructions);
	
}
