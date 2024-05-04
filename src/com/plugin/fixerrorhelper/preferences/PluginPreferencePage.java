package com.plugin.fixerrorhelper.preferences;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
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
        
		addSpace();
		createApiFieldEditor();
		addSpace();
		createLanguageFieldEditor();
	}

	@Override
	public boolean performOk() {
		//TODO
		boolean result = super.performOk();
		return result;
	}

	protected void createApiFieldEditor() {
		Group group = addGroup("API Settings");

		Label apiKeyLabel = new Label(group, SWT.NONE);
		apiKeyLabel.setText(PreferenceConstants.LABEL_API_KEY);
		apiKeyLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		StringFieldEditor apiKeyFieldEditor = new StringFieldEditor(PreferenceConstants.FIELD_API_KEY, "", group);
		apiKeyFieldEditor.setEmptyStringAllowed(false);
		apiKeyFieldEditor.setErrorMessage(PreferenceConstants.ERROR_MESSAGE);
		apiKeyFieldEditor.getTextControl(group).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		apiKeyFieldEditor.getTextControl(group).setToolTipText(PreferenceConstants.HELP_MESSAGE);
		addField(apiKeyFieldEditor);

		addLink(group, "Click here to get API key", "https://platform.openai.com/api-keys");
	}

	protected void createLanguageFieldEditor() {
		Group group = addGroup("Language");

		Label languageLabel = new Label(group, SWT.NONE);
		languageLabel.setText(PreferenceConstants.LABEL_LANGUAGE);
		languageLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		Combo languageCombo = new Combo(group, SWT.READ_ONLY);
		languageCombo.add("English");
		languageCombo.add("Portuguese");
		languageCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		languageCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectedIndex = languageCombo.getSelectionIndex();
				String selectedValue = languageCombo.getItem(selectedIndex);
				//TODO
			}
		});
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
