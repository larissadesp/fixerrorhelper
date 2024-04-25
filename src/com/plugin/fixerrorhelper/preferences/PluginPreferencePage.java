package com.plugin.fixerrorhelper.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.plugin.fixerrorhelper.Activator;

//TODO
public class PluginPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	private static final String FIELD_API_KEY = "apiKey"; 
	private static final String LABEL_API_KEY = "Your API OpenAi key:"; 
	
	private static final String TITLE = "FixErrorHelper Preferences";
	private static final String DESCRIPTION = "Configure your preferences for FixErrorHelper.";
	private static final String ERROR_MESSAGE = "API Key must not be empty.";
	private static final String HELP_MESSAGE = "Enter your OpenAI API Key.";
	
	private StringFieldEditor apiKeyFieldEditor;
	private String apiKey;

	public PluginPreferencePage() {
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setTitle(TITLE);
		setDescription(DESCRIPTION);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		apiKeyFieldEditor = new StringFieldEditor(FIELD_API_KEY, LABEL_API_KEY, getFieldEditorParent());
		apiKeyFieldEditor.setEmptyStringAllowed(false);
		apiKeyFieldEditor.setErrorMessage(ERROR_MESSAGE);
		apiKeyFieldEditor.getTextControl(getFieldEditorParent()).setToolTipText(HELP_MESSAGE);
		addField(apiKeyFieldEditor);
	}

	@Override
	public boolean performOk() {
		boolean result = super.performOk();
        if (result) {
            apiKey = apiKeyFieldEditor.getStringValue(); 
            System.out.println("API Key: " + apiKey);
        }
		return result;
	}
	
	public String getAPIKey() {
        return apiKey;
    }

}
