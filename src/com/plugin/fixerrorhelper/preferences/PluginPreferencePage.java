package com.plugin.fixerrorhelper.preferences;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.service.prefs.BackingStoreException;

import com.plugin.fixerrorhelper.Activator;

//TODO
public class PluginPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PluginPreferencePage() {
		super(GRID);

		setTitle("FixErrorHelper Preferences");
		setDescription("Configure your preferences for FixErrorHelper.");
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {
		StringFieldEditor apiKeyFieldEditor = new StringFieldEditor("apiKey", "API Key:", getFieldEditorParent());
		apiKeyFieldEditor.setEmptyStringAllowed(false);
		apiKeyFieldEditor.setErrorMessage("API Key must not be empty.");
		apiKeyFieldEditor.getTextControl(getFieldEditorParent()).setToolTipText("Enter your OpenAI API Key");
		addField(apiKeyFieldEditor);

		String[][] languages = { { "Português", "pt" }, { "Inglês", "en" }, { "Espanhol", "es" } };
		ComboFieldEditor languageFieldEditor = new ComboFieldEditor("language", "Return language:", languages,
				getFieldEditorParent());
		addField(languageFieldEditor);
	}

	@Override
	public boolean performOk() {
		boolean result = super.performOk();
		if (result) {
			String apiKey = getPreferenceStore().getString("apiKey");
			String selectedLanguage = getPreferenceStore().getString("language");

			IEclipsePreferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
			preferences.put("API_KEY", apiKey);
			try {
				preferences.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}

			System.out.println("API Key: " + apiKey);
			System.out.println("Selected Language: " + selectedLanguage);

			System.setProperty("API_KEY", apiKey);
		}
		return result;
	}

}
