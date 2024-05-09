package com.plugin.fixerrorhelper.messages;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.jface.preference.IPreferenceStore;

import com.plugin.fixerrorhelper.Activator;
import com.plugin.fixerrorhelper.constants.PreferenceConstants;

public class Messages {

	private static final String MESSAGES_EN_US = "messages_en_US.properties";
	private static final String MESSAGES_PT_BR = "messages_pt_BR.properties";

	private static Properties properties;
	private static String languageSelected;
	private static String propertiesFile;

	public Messages() {
		properties = new Properties();
	}

	// core.gpt > client > GPTHttpClient
	public static String emptyReplyMessage;
	public static String comunicationErrorMessage;

	// core.gpt > GPTService
	public static String insufficientQuotaMessage;
	public static String errorFormattingMessage;
	public static String errorProcessingMessage;
	public static String notJavaMessage;
	public static String emptyConsoleMessage;
	public static String apiKeyInvalidMessage;

	// preferences > PluginPreferencePage
	public static String descriptionLabel;
	public static String apiSettingsLabel;
	public static String apiKeyLabel;
	public static String apiErrorMessage;
	public static String apiHelpMessage;
	public static String directLinkApiMessage;
	public static String languageLabel;

	private static void getLanguageSelected() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		languageSelected = store.getString(PreferenceConstants.PREFERENCE_LANGUAGE);
	}

	public static void loadProperties() {
		getLanguageSelected();

		switch (languageSelected) {
		case PreferenceConstants.LANGUAGE_EN:
			propertiesFile = MESSAGES_EN_US;
			break;
		case PreferenceConstants.LANGUAGE_PT_BR:
			propertiesFile = MESSAGES_PT_BR;
			break;
		default:
			propertiesFile = MESSAGES_EN_US;
			break;
		}

		InputStream file = new Messages().getClass().getResourceAsStream(propertiesFile);

		try {
			properties.load(file);

			emptyReplyMessage = properties.getProperty("EmptyReply_Message");
			comunicationErrorMessage = properties.getProperty("ComunicationError_Message");
			insufficientQuotaMessage = properties.getProperty("InsufficientQuota_Message");
			errorFormattingMessage = properties.getProperty("ErrorFormatting_Message");
			errorProcessingMessage = properties.getProperty("ErrorProcessing_Message");
			notJavaMessage = properties.getProperty("NotJava_Message");
			emptyConsoleMessage = properties.getProperty("EmptyConsole_Message");
			apiKeyInvalidMessage = properties.getProperty("ApiKeyInvalid_Message");
			descriptionLabel = properties.getProperty("Description_Label");
			apiSettingsLabel = properties.getProperty("ApiSettings_Label");
			apiKeyLabel = properties.getProperty("ApiKey_Label");
			apiErrorMessage = properties.getProperty("ApiError_Message");
			apiHelpMessage = properties.getProperty("ApiHelp_Message");
			directLinkApiMessage = properties.getProperty("DirectLinkApi_Message");
			languageLabel = properties.getProperty("Language_Label");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
