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
import com.plugin.fixerrorhelper.util.PluginMessageManager;

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
	private PluginMessageManager pluginMessageManager;
	private ChatGPT chatGPT;
	private PluginMessageHandler pluginMessageHandler;
	private static JSONObject apiResponseInJson;

	public static String responseChatGPT = "";

	public Activator() {
		consoleMessageManager = new ConsoleMessageManager();
		gptMessageManager = new GPTMessageManager();
		pluginMessageManager = new PluginMessageManager();
		chatGPT = new ChatGPT();
		pluginMessageHandler = new PluginMessageHandler();
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
		
		if (!consoleMessageManager.isJavaErrorOrException(consoleOutput)) {
	        responseChatGPT = MESSAGE_NOT_JAVA;
	        return;
	    }
		
		String normalizedConsoleOutput = gptMessageManager.normalizeConsoleText(consoleOutput);
		JSONArray instructionGPT = gptMessageManager.assembleApiInstruction(normalizedConsoleOutput);

		apiResponseInJson = chatGPT.chatGPT(instructionGPT);

		if (gptMessageManager.isInsufficientQuota(apiResponseInJson)) {
			responseChatGPT = INSUFFICIENT_QUOTA_MESSAGE;
			return;
		}

		if (!pluginMessageManager.validateExpectedJson(apiResponseInJson)) {
			responseChatGPT = ERROR_PROCESSING_MESSAGE;
			return;
		}

		try {
			JSONObject jsonContentMessageForPlugin = new JSONObject(pluginMessageManager.extractContentJson(apiResponseInJson));
			responseChatGPT = pluginMessageManager.formatResponseForPlugin(jsonContentMessageForPlugin);

			if (!pluginMessageManager.checkIfCompleteAnswer(responseChatGPT)) {
				responseChatGPT = ERROR_FORMATTING_MESSAGE;
				return;
			}

			pluginMessageHandler.responseOk = true;
		} catch (JSONException e) {
			responseChatGPT = ERROR_FORMATTING_MESSAGE;
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
