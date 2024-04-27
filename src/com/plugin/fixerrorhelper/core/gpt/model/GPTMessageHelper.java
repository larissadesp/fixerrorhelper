package com.plugin.fixerrorhelper.core.gpt.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

class GPTMessageHelper {
	public static final String KEY_CAUSE = "cause";
	public static final String KEY_ERROR = "error";
	public static final String KEY_TYPE = "type";
	public static final String KEY_POSSIBLE_SOLUTIONS = "possible solution(s)";
	private static final String INSUFFICIENT_QUOTA = "insufficient_quota";

	public static boolean isInsufficientQuota(JSONObject jsonObject) {
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
	
	public static boolean checkIfCompleteAnswer(String responseChatGPT) {
		String regexCause = "(Cause:)[\\s\\S]*?";
		String regexError = "(Error:)[\\s\\S]*?";
		String regexSolutions = "(Possible solution\\(s\\):)[\\s\\S]*?";

		if (matchPattern(responseChatGPT, regexCause) && matchPattern(responseChatGPT, regexError)
				&& matchPattern(responseChatGPT, regexSolutions)) {
			return true;
		}

		return false;
	}
	
	private static boolean matchPattern(String input, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		
		return matcher.find();
	}
}
