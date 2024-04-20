package com.plugin.fixerrorhelper.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GPTMessageManager {

	public static final String ROLE = "role";
	public static final String CONTENT = "content";
	public static final String MESSAGE = "message";
	public static final String CHOICES = "choices";
	
	public static final String SYSTEM = "system";
	public static final String USER = "user";
	
	public static final String SYSTEM_CONTENT = 
			"You are a Java programming wizard and must only generate responses in JSON-style format: "
			+ "{cause: describe the cause of the error here, "
			+ "error: describe the error here, "
			+ "possible solutions: describe possible solutions here}";
	public static final String USER_CONTENT = 
			"Review the following error message and provide the requested information. "
			+ "Message: ";
	
	public static final String INSUFFICIENT_QUOTA = "insufficient_quota";

	public static final String KEY_CAUSE = "cause";
	public static final String KEY_ERROR = "error";
	public static final String KEY_POSSIBLE_SOLUTIONS = "possible solutions";
	public static final String KEY_TYPE = "type";

	public static String valueCause = "";
	public static String valueError = "";
	public static String valuePossibleSolutions = "";

	public String normalizeConsoleText(String textConsole) {
		// Replaces line breaks and whitespace with a single space
	    String normalizedText = textConsole.replaceAll("\\r\\n|\\s+", " ");
	    return normalizedText;
	}

	public JSONArray assembleApiInstruction(String formattedConsoleMessage) {
		JSONArray messages = new JSONArray();

		messages.put(new JSONObject()
				.put(ROLE, SYSTEM)
				.put(CONTENT, SYSTEM_CONTENT));

		messages.put(new JSONObject()
				.put(ROLE, USER)
				.put(CONTENT, USER_CONTENT + formattedConsoleMessage));

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

	public boolean validateExpectedJson(JSONObject apiResponseInJson) {
		JSONObject pluginResponseInJson = new JSONObject(extractContentJson(apiResponseInJson));

		try {
			if (!pluginResponseInJson.has(KEY_CAUSE) || !pluginResponseInJson.has(KEY_ERROR)
					|| !pluginResponseInJson.has(KEY_POSSIBLE_SOLUTIONS)) {
				return false;
			}

			valueCause = pluginResponseInJson.optString(KEY_CAUSE);
			valueError = pluginResponseInJson.optString(KEY_ERROR);
			valuePossibleSolutions = pluginResponseInJson.optString(KEY_POSSIBLE_SOLUTIONS);

			if (valueCause.isEmpty() || valueError.isEmpty() || valuePossibleSolutions.isEmpty()) {
				return false;
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String extractContentJson(JSONObject jsonObject) {
		try {
			JSONArray choicesArray = jsonObject.getJSONArray(CHOICES);
			JSONObject choiceObject = choicesArray.getJSONObject(0);
			JSONObject messageObject = choiceObject.getJSONObject(MESSAGE);
			String content = messageObject.optString(CONTENT);

			return content;
		} catch (Exception e) {
			return null;
		}
	}

	public String formatResponseForPlugin(JSONObject jsonContentMessageForPlugin) {
		StringBuilder formattedResponse = new StringBuilder();

		try {
			formattedResponse.append("Cause: ").append(jsonContentMessageForPlugin.getString(KEY_CAUSE)).append("\n\n");
			formattedResponse.append("Error: ").append(jsonContentMessageForPlugin.getString(KEY_ERROR)).append("\n\n");
			formattedResponse.append("Possible solutions: ")
					.append(jsonContentMessageForPlugin.getString(KEY_POSSIBLE_SOLUTIONS)).append("\n\n");
		} catch (Exception e) {
			formattedResponse.append("Error formatting response: ").append(e.getMessage());
		}

		return formattedResponse.toString();
	}

	public boolean checkIfCompleteAnswer(String responseChatGPT) {
		String regexCause = "(Cause:)[\\s\\S]*?";
		String regexError = "(Error:)[\\s\\S]*?";
		String regexSolutions = "(Possible solutions:)[\\s\\S]*?";

		if (matchPattern(responseChatGPT, regexCause) && matchPattern(responseChatGPT, regexError)
				&& matchPattern(responseChatGPT, regexSolutions)) {
			return true;
		}

		return false;
	}

	public static boolean matchPattern(String input, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		
		return matcher.find();
	}

}
