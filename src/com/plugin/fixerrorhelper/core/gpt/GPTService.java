package com.plugin.fixerrorhelper.core.gpt;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import com.plugin.fixerrorhelper.constants.ApiKeyConstants;
import com.plugin.fixerrorhelper.core.gpt.model.Language;
import com.plugin.fixerrorhelper.messages.Messages;
import com.plugin.fixerrorhelper.util.ConsoleMessageManager;

public class GPTService {

	public static String makeFriendlyConsole(String consoleText, Language language, String apiKey) {
		if (StringUtils.isBlank(consoleText)) {
			return Messages.emptyConsoleMessage;
		}

		if (StringUtils.isBlank(apiKey)) {
			return Messages.apiKeyInvalidMessage;
		}

		if (!(ConsoleMessageManager.isStackTrace(consoleText)
				&& ConsoleMessageManager.isJavaErrorException(consoleText))) {
			return Messages.notJavaMessage;
		}
		
		if (!ConsoleMessageManager.checkThrowable(consoleText)) {
			return Messages.notJavaMessage;
		}

		var result = GPTCaller.init(consoleText)
							  .withLanguage(language)
							  .withApiKey(apiKey)
							  .call();

		if (result.isErrorOfType() != "") {
			var errorType = result.isErrorOfType();
			if (errorType == ApiKeyConstants.INSUFFICIENT_QUOTA) {
				return Messages.insufficientQuotaMessage;
			}
			
			if (errorType == ApiKeyConstants.CONTEXT_LENGTH_EXCEEDED) {
				return Messages.contextLengthExceeded;
			}
		}

		if (result.hasParseError()) {
			return Messages.errorProcessingMessage;
		}

		try {
			if (!result.isComplete()) {
				return Messages.errorFormattingMessage;
			}

			return result.formattedMessage();
		} catch (JSONException e) {
			return Messages.errorFormattingMessage;
		}
	}

}
