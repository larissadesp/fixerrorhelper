package com.plugin.fixerrorhelper.core.gpt;

import org.json.JSONArray;
import org.json.JSONObject;

import com.plugin.fixerrorhelper.core.gpt.client.GPTHttpClient;
import com.plugin.fixerrorhelper.core.gpt.client.GPTSandboxClient;
import com.plugin.fixerrorhelper.core.gpt.model.GPTResult;
import com.plugin.fixerrorhelper.util.EnvironmentUtils;

public class GPTCaller {
	
	private static final String SYSTEM_CONTENT = """
			You are a Java programming wizard and must only generate responses in JSON-style format:
			{
				"cause": describe the cause of the error here, "
				"error": describe the error here,
				"possible solution(s)": describe possible solutions here
			}
			""";

	private static final String USER_CONTENT = "Review the following error message and provide the requested information. Message: ";

	private GPTClient client;
	private String normalizedInput;

	private GPTCaller(GPTClient client, String normalizedInput) {
		this.client = client;
		this.normalizedInput = normalizeInput(normalizedInput);
	}

	public static GPTCaller init(String rawInput) {
		var sandboxFlag = EnvironmentUtils.getBoolean("FEH_SANDBOX");

		GPTClient client = sandboxFlag.isPresent() && sandboxFlag.get() ? new GPTSandboxClient() : new GPTHttpClient();

		return new GPTCaller(client, normalizeInput(rawInput));
	}
	
	private static String normalizeInput(String text) {
		return text.replaceAll("\\r\\n|\\s+", " ");
	}

	public GPTResult call() {
		var instructions = assembleApiInstruction();
		var gptResult = client.call(instructions);

		return GPTResult.fromJson(gptResult);
	}

	public JSONArray assembleApiInstruction() {
		JSONArray messages = new JSONArray();

		messages.put(new JSONObject().put("role", "system").put("content", SYSTEM_CONTENT));
		messages.put(new JSONObject().put("role", "user").put("content", USER_CONTENT + normalizedInput));

		return messages;
	}
	
}
