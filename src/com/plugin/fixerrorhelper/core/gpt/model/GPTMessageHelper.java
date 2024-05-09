package com.plugin.fixerrorhelper.core.gpt.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.plugin.fixerrorhelper.constants.TextSectionHeadersConstants;

class GPTMessageHelper {

	public static boolean isInsufficientQuota(JSONObject jsonObject) {
		try {
			if (!jsonObject.has(TextSectionHeadersConstants.KEY_ERROR)) {
				return false;
			}

			Object errorObject = jsonObject.get(TextSectionHeadersConstants.KEY_ERROR);

			if (errorObject instanceof JSONObject) {
				JSONObject errorJSONObject = (JSONObject) errorObject;

				if (errorJSONObject.has(TextSectionHeadersConstants.KEY_TYPE)
						&& errorJSONObject.getString(TextSectionHeadersConstants.KEY_TYPE)
								.equals(TextSectionHeadersConstants.INSUFFICIENT_QUOTA)) {
					return true;
				}
			}

			if (errorObject instanceof String) {
				if (errorObject.equals(TextSectionHeadersConstants.INSUFFICIENT_QUOTA)) {
					return true;
				}
			}
		} catch (JSONException e) {
			return false;
		}

		return false;
	}

	public static boolean checkIfCompleteAnswer(String responseChatGPT) {
		String regexCause = "(" + TextSectionHeadersConstants.CAUSE + ")[\\s\\S]*?";
		String regexError = "(" + TextSectionHeadersConstants.ERROR + ")[\\s\\S]*?";
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
