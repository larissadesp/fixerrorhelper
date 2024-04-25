package com.plugin.fixerrorhelper.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class PluginMessageManager {

	private String valueCause = "";
	private String valueError = "";
	private String valuePossibleSolutions = "";
	
	public boolean validateExpectedJson(JSONObject apiResponseInJson) {
		JSONObject pluginResponseInJson = new JSONObject(extractContentJson(apiResponseInJson));

		try {
			if (!pluginResponseInJson.has(GPTMessageManager.KEY_CAUSE) || !pluginResponseInJson.has(GPTMessageManager.KEY_ERROR)
					|| !pluginResponseInJson.has(GPTMessageManager.KEY_POSSIBLE_SOLUTIONS)) {
				return false;
			}

			valueCause = pluginResponseInJson.optString(GPTMessageManager.KEY_CAUSE);
			valueError = pluginResponseInJson.optString(GPTMessageManager.KEY_ERROR);
			valuePossibleSolutions = pluginResponseInJson.optString(GPTMessageManager.KEY_POSSIBLE_SOLUTIONS);

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
			JSONArray choicesArray = jsonObject.getJSONArray("choices");
			JSONObject choiceObject = choicesArray.getJSONObject(0);
			JSONObject messageObject = choiceObject.getJSONObject("message");
			String content = messageObject.optString("content");

			return content;
		} catch (Exception e) {
			return null;
		}
	}

	public String formatResponseForPlugin(JSONObject jsonContentMessageForPlugin) {
		StringBuilder formattedResponse = new StringBuilder();

		try {
			formattedResponse.append("Cause: ").append(jsonContentMessageForPlugin.getString(GPTMessageManager.KEY_CAUSE)).append("\n\n");
			formattedResponse.append("Error: ").append(jsonContentMessageForPlugin.getString(GPTMessageManager.KEY_ERROR)).append("\n\n");
			formattedResponse.append("Possible solution(s): ")
					.append(jsonContentMessageForPlugin.getString(GPTMessageManager.KEY_POSSIBLE_SOLUTIONS)).append("\n\n");
		} catch (Exception e) {
			formattedResponse.append("Error formatting response: ").append(e.getMessage());
		}

		return formattedResponse.toString();
	}

	public boolean checkIfCompleteAnswer(String responseChatGPT) {
		String regexCause = "(Cause:)[\\s\\S]*?";
		String regexError = "(Error:)[\\s\\S]*?";
		String regexSolutions = "(Possible solution\\(s\\):)[\\s\\S]*?";

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
