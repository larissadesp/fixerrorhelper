package com.plugin.fixerrorhelper.preferences;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.plugin.fixerrorhelper.Activator;
import com.plugin.fixerrorhelper.constants.PreferenceConstants;
import com.plugin.fixerrorhelper.core.gpt.model.Language;

public class PluginPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private Composite mainComposite;

	public PluginPreferencePage() {
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setDescription(PreferenceConstants.DESCRIPTION);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		mainComposite = getFieldEditorParent();
		mainComposite.setLayout(new GridLayout());
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
		Group group = addGroup("API Settings");
		
		addSpace();
		createApiFieldEditor(group);
		addSpace();
		createLanguageFieldEditor(group);
	}

	protected void createApiFieldEditor(Group group) {
		StringFieldEditor apiKeyFieldEditor = new StringFieldEditor(PreferenceConstants.PREFERENCE_API_KEY, "Your API Key", group);
		apiKeyFieldEditor.setEmptyStringAllowed(false);
		apiKeyFieldEditor.setErrorMessage(PreferenceConstants.ERROR_MESSAGE);
		apiKeyFieldEditor.getTextControl(group).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		apiKeyFieldEditor.getTextControl(group).setToolTipText(PreferenceConstants.HELP_MESSAGE);
		addField(apiKeyFieldEditor);

		addLink(group, "Click here to get API key", "https://platform.openai.com/api-keys");
	}

	protected void createLanguageFieldEditor(Group group) {
		String[][] values = Language.keyMap();
		ComboFieldEditor languageEditor = new ComboFieldEditor(PreferenceConstants.PREFERENCE_LANGUAGE, PreferenceConstants.LABEL_LANGUAGE, values, group);
	
		languageEditor.getLabelControl(group).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		languageEditor.getLabelControl(group).setToolTipText(PreferenceConstants.HELP_MESSAGE);
		
		addField(languageEditor);
	}

	protected void addLink(Group group, String text, String url) {
		Link link = new Link(group, SWT.NONE);
		link.setText("<a href=\"" + url + "\">" + text + "</a>");

		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.horizontalSpan = 2;
		link.setLayoutData(gridData);

		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openUrl(url);
			}
		});
	}

	protected void openUrl(String url) {
		try {
			PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(url));
		} catch (PartInitException | MalformedURLException e) {
			e.printStackTrace();
		}
	}

	protected void addSpace() {
		Label space = new Label(mainComposite, SWT.NONE);
		space.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 0));
		space.setText("");
	}

	protected Group addGroup(String text) {
		Group group = new Group(mainComposite, SWT.NONE);
		group.setText(text);
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
		data.verticalIndent = 5;
		group.setLayoutData(data);

		return group;
	}

}
