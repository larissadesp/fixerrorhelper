package com.plugin.fixerrorhelper.core.gpt.client;

import org.json.JSONArray;
import org.json.JSONObject;

import com.plugin.fixerrorhelper.core.gpt.GPTClient;

public class GPTSandboxClient implements GPTClient {
	private static final String CONTENT = "{\\\"cause\\\": \\\"describe the cause of the error here\\\", \\\"error\\\": \\\"describe the error here\\\", \\\"possible solution(s)\\\": \\\"describe possible solutions here\\\"}" ;
	
	private static final String EXPECTED_JSON = """
				{
				    "choices": [
				        {
				            "message": {
				                "content": "%s"
				            }
				        }
				    ]
				}
			""";
	
	@Override
	public JSONObject call(JSONArray instructions) {
		return new JSONObject(EXPECTED_JSON.formatted(CONTENT));
	}

}
