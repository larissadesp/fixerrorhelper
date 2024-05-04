package com.plugin.fixerrorhelper.core.gpt;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import com.plugin.fixerrorhelper.util.ConsoleMessageManager;

public class GPTService {
	
	private static final String INSUFFICIENT_QUOTA_MESSAGE = "You exceeded your current quota, please check your plan and billing details.";
	private static final String ERROR_FORMATTING_MESSAGE = "An error occurred while formatting the response.";
	private static final String ERROR_PROCESSING_MESSAGE = "An error occurred while processing the message return. Try again.";
	private static final String MESSAGE_NOT_JAVA = "The message is not related to Java.";
	private static final String EMPTY_CONSOLE_MESSAGE = "There is no message in the Console.";

	public static String makeFriendlyConsole(String consoleText) {
		if (StringUtils.isBlank(consoleText)) {
			return EMPTY_CONSOLE_MESSAGE;
		}

		if (!ConsoleMessageManager.isJavaErrorOrException(consoleText)) {
			return MESSAGE_NOT_JAVA;
		}

		var result = GPTCaller.init(consoleText).call();

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
