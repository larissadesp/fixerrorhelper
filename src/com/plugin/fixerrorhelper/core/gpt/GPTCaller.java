package com.plugin.fixerrorhelper.core.gpt;

import org.json.JSONArray;
import org.json.JSONObject;

import com.plugin.fixerrorhelper.core.gpt.client.GPTHttpClient;
import com.plugin.fixerrorhelper.core.gpt.client.GPTSandboxClient;
import com.plugin.fixerrorhelper.core.gpt.model.GPTResult;
import com.plugin.fixerrorhelper.core.gpt.model.Language;
import com.plugin.fixerrorhelper.util.EnvironmentUtils;

public class GPTCaller {
	
	private static final String SYSTEM_CONTENT = 
			"""
				You are a Java programming wizard and must only generate responses in language %s and in JSON-style format:
				{
					"cause": describe the cause of the error here, "
					"error": describe the error here,
					"possible_solutions": describe possible solutions here (with line breaks if there is numbering)
				}
			""";

	private static final String USER_CONTENT = "Review the following error message and provide the requested information. Message: ";

	private Boolean sandbox;
	private String normalizedInput;
	private Language language;
	private String apiKey;

	private GPTCaller(Boolean sandbox, String normalizedInput) {
		this.sandbox = sandbox;
		this.normalizedInput = normalizeInput(normalizedInput);
	}

	public static GPTCaller init(String rawInput) {
		var sandboxFlag = EnvironmentUtils.getBoolean("FEH_SANDBOX");
		return new GPTCaller(sandboxFlag.orElse(Boolean.FALSE), normalizeInput(rawInput));
	}
	
	private static String normalizeInput(String text) {
		return text.replaceAll("\\r\\n|\\s+", " ");
	}
	
	public GPTCaller withLanguage(Language language) {
		this.language = language;
		return this;
	}
	
	public GPTCaller withApiKey(String apiKey) {
		this.apiKey = apiKey;
		return this;
	}

	public GPTResult call() {
		GPTClient client = sandbox ? new GPTSandboxClient() : new GPTHttpClient(apiKey);
		
		var instructions = assembleApiInstruction();
		var gptResult = client.call(instructions);

		return GPTResult.fromJson(gptResult);
	}

	public JSONArray assembleApiInstruction() {
		JSONArray messages = new JSONArray();
		
		var system_content = SYSTEM_CONTENT.formatted(language.getDescription());
		
		messages.put(new JSONObject().put("role", "system").put("content", system_content));
		messages.put(new JSONObject().put("role", "user").put("content", USER_CONTENT + normalizedInput));

		return messages;
	}
	
}
