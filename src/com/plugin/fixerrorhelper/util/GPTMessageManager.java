package com.plugin.fixerrorhelper.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GPTMessageManager {

	private static final String SYSTEM_CONTENT = 
			"You are a Java programming wizard and must only generate responses in JSON-style format: "
			+ "{cause: describe the cause of the error here, "
			+ "error: describe the error here, "
			+ "possible solution(s): describe possible solutions here}";
	private static final String USER_CONTENT = 
			"Review the following error message and provide the requested information. "
			+ "Message: ";
	
	private static final String INSUFFICIENT_QUOTA = "insufficient_quota";
	
	public static final String KEY_CAUSE = "cause";
	public static final String KEY_ERROR = "error";
	public static final String KEY_POSSIBLE_SOLUTIONS = "possible solution(s)";
	public static final String KEY_TYPE = "type";

	public String normalizeConsoleText(String textConsole) {
		// Replaces line breaks and whitespace with a single space
	    String normalizedText = textConsole.replaceAll("\\r\\n|\\s+", " ");
	    return normalizedText;
	}

	public JSONArray assembleApiInstruction(String formattedConsoleMessage) {
		JSONArray messages = new JSONArray();

		messages.put(new JSONObject()
				.put("role", "system")
				.put("content", SYSTEM_CONTENT));

		messages.put(new JSONObject()
				.put("role", "user")
				.put("content", USER_CONTENT + formattedConsoleMessage));

		return messages;
	}

	public boolean isInsufficientQuota(JSONObject jsonObject) {
		try {
			if (jsonObject.has(KEY_ERROR)) {
				Object errorObject = jsonObject.get(KEY_ERROR);

				if (errorObject instanceof JSONObject) {
					JSONObject errorJSONObject = (JSONObject) errorObject;
					
					if (errorJSONObject.has(KEY_TYPE) && errorJSONObject.getString(KEY_TYPE).equals(INSUFFICIENT_QUOTA)) {
						return true;
					}
				} else if (errorObject instanceof String) {
					if (errorObject.equals(INSUFFICIENT_QUOTA)) {
						return true;
					}
				}
			}
		} catch (JSONException e) {
			return false;
		}
		
		return false; 
	}

}
