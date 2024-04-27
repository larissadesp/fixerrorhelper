package com.plugin.fixerrorhelper.core.gpt.client;

import org.json.JSONArray;
import org.json.JSONObject;

import com.plugin.fixerrorhelper.core.gpt.GPTClient;

public class GPTSandboxClient implements GPTClient{
	private static final String EXPECTED_JSON = """
				{
					"cause": "describe the cause of the error here",
					"error": "describe the error here",
					"possible solution(s)": "describe possible solutions here"
				}
			""";

	@Override
	public JSONObject call(JSONArray instructions) {
		return new JSONObject(EXPECTED_JSON);
	}

}
