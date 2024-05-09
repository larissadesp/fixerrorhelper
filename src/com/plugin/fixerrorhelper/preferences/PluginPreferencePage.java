package com.plugin.fixerrorhelper.preferences;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import com.plugin.fixerrorhelper.messages.Messages;

public class PluginPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final String API_KEY_LINK = "https://platform.openai.com/api-keys";

	private Composite mainComposite;

	public PluginPreferencePage() {
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setDescription(Messages.descriptionLabel);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		mainComposite = getFieldEditorParent();
		mainComposite.setLayout(new GridLayout());
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Group group = addGroup(Messages.apiSettingsLabel);

		addSpace();
		createApiFieldEditor(group);

		addSpace();
		createLanguageFieldEditor(group);
	}
	
	@Override
    public boolean performOk() {
        super.performOk();
        Messages.loadProperties();
        return true;
    }
	
	protected void createApiFieldEditor(Group group) {
		StringFieldEditor apiKeyFieldEditor = new StringFieldEditor(PreferenceConstants.PREFERENCE_API_KEY,
				Messages.apiKeyLabel, group);
		apiKeyFieldEditor.setEmptyStringAllowed(false);
		apiKeyFieldEditor.setErrorMessage(Messages.apiErrorMessage);
		apiKeyFieldEditor.getTextControl(group).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		apiKeyFieldEditor.getTextControl(group).setToolTipText(Messages.apiHelpMessage);

		addField(apiKeyFieldEditor);

		addLink(group, Messages.directLinkApiMessage, API_KEY_LINK);
	}

	protected void createLanguageFieldEditor(Group group) {
		String[][] values = Language.keyMap();

		ComboFieldEditor languageEditor = new ComboFieldEditor(PreferenceConstants.PREFERENCE_LANGUAGE,
				Messages.languageLabel, values, group);
		languageEditor.getLabelControl(group).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

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
