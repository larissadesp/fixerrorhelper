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

	public static String isErrorOfType(JSONObject jsonObject) {
	    try {
	        if (!jsonObject.has(SectionHeadersKeyConstants.KEY_ERROR)) {
	            return "";
	        }

	        Object errorObject = jsonObject.get(SectionHeadersKeyConstants.KEY_ERROR);

	        if (errorObject instanceof JSONObject) {
	            JSONObject errorJSONObject = (JSONObject) errorObject;

	            if (errorJSONObject.has(ApiKeyConstants.KEY_TYPE)) {
	                String errorType = errorJSONObject.getString(ApiKeyConstants.KEY_TYPE);

	                if (errorType.equals(ApiKeyConstants.INSUFFICIENT_QUOTA)) {
	                    return ApiKeyConstants.INSUFFICIENT_QUOTA;
	                }
	                
	                if (errorObject instanceof String) {
						if (errorObject.equals(ApiKeyConstants.INSUFFICIENT_QUOTA)) {
							return ApiKeyConstants.INSUFFICIENT_QUOTA;
						}
					}

	                if (errorJSONObject.has(ApiKeyConstants.KEY_CODE)) {
	                    String errorCode = errorJSONObject.getString(ApiKeyConstants.KEY_CODE);

	                    if (errorType.equals(ApiKeyConstants.INVALID_REQUEST_ERROR) && 
	                        errorCode.equals(ApiKeyConstants.CONTEXT_LENGTH_EXCEEDED)) {
	                        return ApiKeyConstants.CONTEXT_LENGTH_EXCEEDED;
	                    }
	                }
	            }
	        } 
	    } catch (JSONException e) {
	    	return "";
	    }

	    return "";
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
