package com.plugin.fixerrorhelper.core.gpt.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.plugin.fixerrorhelper.constants.ApiKeyConstants;
import com.plugin.fixerrorhelper.constants.PreferenceConstants;
import com.plugin.fixerrorhelper.constants.SectionHeadersKeyConstants;
import com.plugin.fixerrorhelper.messages.Messages;

class GPTMessageHelper {

	public static boolean isInsufficientQuota(JSONObject jsonObject) {
		try {
			if (!jsonObject.has(SectionHeadersKeyConstants.KEY_ERROR)) {
				return false;
			}

			Object errorObject = jsonObject.get(SectionHeadersKeyConstants.KEY_ERROR);

			if (errorObject instanceof JSONObject) {
				JSONObject errorJSONObject = (JSONObject) errorObject;

				if (errorJSONObject.has(ApiKeyConstants.KEY_TYPE)
						&& errorJSONObject.getString(ApiKeyConstants.KEY_TYPE)
								.equals(ApiKeyConstants.INSUFFICIENT_QUOTA)) {
					return true;
				}
			}

			if (errorObject instanceof String) {
				if (errorObject.equals(ApiKeyConstants.INSUFFICIENT_QUOTA)) {
					return true;
				}
			}
		} catch (JSONException e) {
			return false;
		}

		return false;
	}

	public static boolean isContextLengthExceeded(JSONObject jsonObject) {
		Object errorObject = jsonObject.get(SectionHeadersKeyConstants.KEY_ERROR);

		if (errorObject instanceof JSONObject) {
			JSONObject errorJSONObject = (JSONObject) errorObject;

			if (errorJSONObject.has(ApiKeyConstants.KEY_TYPE)
					&& errorJSONObject.has(ApiKeyConstants.KEY_CODE)) {
				
				if (errorJSONObject.getString(ApiKeyConstants.KEY_TYPE)
						.equals(ApiKeyConstants.INVALID_REQUEST_ERROR)
						&& errorJSONObject.getString(ApiKeyConstants.KEY_CODE)
								.equals(ApiKeyConstants.CONTEXT_LENGTH_EXCEEDED))
					return true;
			}
		}

		return false;
	}

	public static boolean checkIfCompleteAnswer(String responseChatGPT) {
		String regexCause = "(" + Messages.cause + ")[\\s\\S]*?";
		String regexError = "(" + Messages.error + ")[\\s\\S]*?";
		String regexSolutionsIn = "(Possible solution\\(s\\):)[\\s\\S]*?";
		String regexSolutionsPt = "(Possível\\(is\\) solução\\(ões\\):)[\\s\\S]*?";
		String regexSolutions = "";

		String languageSelected = Messages.languageSelected;

		switch (languageSelected) {
		case PreferenceConstants.LANGUAGE_EN:
			regexSolutions = regexSolutionsIn;
			break;
		case PreferenceConstants.LANGUAGE_PT_BR:
			regexSolutions = regexSolutionsPt;
			break;
		default:
			regexSolutions = regexSolutionsIn;
			break;
		}

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
