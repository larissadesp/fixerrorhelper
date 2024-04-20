package com.plugin.fixerrorhelper;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;

import com.plugin.fixerrorhelper.api.ChatGPT;
import com.plugin.fixerrorhelper.handlers.PluginMessageHandler;
import com.plugin.fixerrorhelper.util.ConsoleMessageManager;
import com.plugin.fixerrorhelper.util.GPTMessageManager;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.plugin.fixerrorhelper"; //$NON-NLS-1$

	private static final String INSUFFICIENT_QUOTA_MESSAGE = "You exceeded your current quota, please check your plan and billing details.";
	private static final String ERROR_FORMATTING_MESSAGE = "An error occurred while formatting the response.";
	private static final String ERROR_PROCESSING_MESSAGE = "An error occurred while processing the message return. Try again.";
	private static final String MESSAGE_NOT_JAVA = "The message is not related to Java.";
	private static final String EMPTY_CONSOLE_MESSAGE = "There is no message in the Console.";

	private static Activator plugin;
	private ConsoleMessageManager consoleMessageManager;
	private GPTMessageManager gptMessageManager;
	private ChatGPT chatGPT;
	public static JSONObject apiResponseInJson;

	public static String responseChatGPT = "";

	public Activator() {
		consoleMessageManager = new ConsoleMessageManager();
		gptMessageManager = new GPTMessageManager();
		chatGPT = new ChatGPT();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		String consoleOutput = consoleMessageManager.getProcessConsoleOutput();

		if (StringUtils.isBlank(consoleOutput)) {
			responseChatGPT = EMPTY_CONSOLE_MESSAGE;
			return;
		}

		if (consoleMessageManager.hasThrowableHierarchy(consoleOutput)) {
			String normalizedConsoleOutput = gptMessageManager.normalizeConsoleText(consoleOutput);
			JSONArray instructionGPT = gptMessageManager.assembleApiInstruction(normalizedConsoleOutput);

			apiResponseInJson = chatGPT.chatGPT(instructionGPT);

			if (gptMessageManager.isInsufficientQuota(apiResponseInJson)) {
				responseChatGPT = INSUFFICIENT_QUOTA_MESSAGE;
				return;
			}

			if (gptMessageManager.validateExpectedJson(apiResponseInJson)) {
				try {
					JSONObject jsonContentMessageForPlugin = new JSONObject(gptMessageManager.extractContentJson(apiResponseInJson));
					responseChatGPT = gptMessageManager.formatResponseForPlugin(jsonContentMessageForPlugin);

					if (gptMessageManager.checkIfCompleteAnswer(responseChatGPT)) {
						PluginMessageHandler.responseOk = true;
					} else {
						responseChatGPT = ERROR_FORMATTING_MESSAGE;
					}
				} catch (JSONException e) {
					responseChatGPT = ERROR_FORMATTING_MESSAGE;
				}
			} else {
				responseChatGPT = ERROR_PROCESSING_MESSAGE;
			}
		} else {
			responseChatGPT = MESSAGE_NOT_JAVA;
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

}
