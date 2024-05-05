package com.plugin.fixerrorhelper.core.gpt;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import com.plugin.fixerrorhelper.core.gpt.model.Language;
import com.plugin.fixerrorhelper.util.ConsoleMessageManager;

public class GPTService {
	
	private static final String INSUFFICIENT_QUOTA_MESSAGE = "You exceeded your current quota, please check your plan and billing details.";
	private static final String ERROR_FORMATTING_MESSAGE = "An error occurred while formatting the response.";
	private static final String ERROR_PROCESSING_MESSAGE = "An error occurred while processing the message return. Try again.";
	private static final String MESSAGE_NOT_JAVA = "The message is not related to Java.";
	private static final String EMPTY_CONSOLE_MESSAGE = "There is no message in the Console.";
	private static final String API_KEY_INVALID = "Invalid or missing API Key info";

	public static String makeFriendlyConsole(String consoleText, Language language, String apiKey) {
		if (StringUtils.isBlank(consoleText)) {
			return EMPTY_CONSOLE_MESSAGE;
		}
		
		if (StringUtils.isBlank(apiKey)) {
			return API_KEY_INVALID;
		}

		if (!(ConsoleMessageManager.isStackTrace(consoleText) && ConsoleMessageManager.isJavaLangException(consoleText))) {
			return MESSAGE_NOT_JAVA;
		}

		var result = GPTCaller
				.init(consoleText)
				.withLanguage(language)
				.withApiKey(apiKey)
				.call();

		if (result.isInsufficientQuota()) {
			return INSUFFICIENT_QUOTA_MESSAGE;
		}

		if (result.hasParseError()) {
			return ERROR_PROCESSING_MESSAGE;
		}

		try {
			if (!result.isComplete()) {
				return ERROR_FORMATTING_MESSAGE;
			}

			return result.formattedMessage();
		} catch (JSONException e) {
			return ERROR_FORMATTING_MESSAGE;
		}
	}
	
}
