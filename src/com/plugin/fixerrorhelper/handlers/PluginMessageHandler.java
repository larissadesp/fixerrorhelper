package com.plugin.fixerrorhelper.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.plugin.fixerrorhelper.Activator;
import com.plugin.fixerrorhelper.preferences.PluginPreferencePage;

public class PluginMessageHandler extends AbstractHandler {

	private static final String TITLE = "FixErrorHelper";
	private static final String[] TEXT_SECTION_HEADERS = { "Cause:", "Error:", "Possible solution(s):" };

	private PluginPreferencePage preferencePage;
	
	public boolean responseOk = false;
	
	private String apiKey;
	private String pluginReturnMessage = "";
	private Color background;
	private Display display;
	private Shell shell;
	private StyledText styledText;

	public PluginMessageHandler() {
		preferencePage = new PluginPreferencePage();
		pluginReturnMessage = Activator.responseChatGPT;
	}

	@Override
	public Object execute(ExecutionEvent event) {
		apiKey = preferencePage.getAPIKey();
		
		if (apiKey != null && responseOk == true) {
			System.out.println("API Key 2: " + apiKey);
		}

		background = new Color(display, 240, 240, 240);
		display = Display.getDefault();
		
		shell = createShell();
		createUIComponents(shell);
		shell.open();
		
		shell.addListener(SWT.Close, e -> {
			disposeResources();
		});
		
		return null;
	}
	
	private Shell createShell() {
		Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.RESIZE);
		shell.setText(TITLE);
		shell.setSize(450, 350);
		shell.setLayout(new GridLayout());
		shell.setBackground(background);
		
		return shell;
	}

	private void createUIComponents(Shell shell) {
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setBackground(background);

		styledText = new StyledText(composite, SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		styledText.setText(pluginReturnMessage);
		styledText.setBackground(background);
		formatTextSections(styledText);

		createOkButton(shell);
		
		shell.addListener(SWT.Close, e -> {
			disposeResources();
			
			try {
				Activator.getDefault().stop(Activator.getDefault().getBundle().getBundleContext());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	private void formatTextSections(StyledText styledText) {
		for (String header : TEXT_SECTION_HEADERS) {
			int index = pluginReturnMessage.indexOf(header);

			if (index >= 0) {
				StyleRange styleRange = new StyleRange();
				styleRange.start = index;
				styleRange.length = header.length();
				styleRange.fontStyle = SWT.BOLD;

				switch (header) {
				case "Cause:":
					styleRange.foreground = new Color(display, 255, 190, 0);
					break;
				case "Error:":
					styleRange.foreground = new Color(display, 255, 0, 0);
					break;
				case "Possible solution(s):":
					styleRange.foreground = new Color(display, 30, 144, 255);
					break;
				default:
					break;
				}

				styledText.setStyleRange(styleRange);
			}
		}
	}

	private void createOkButton(Shell shell) {
		Button okButton = new Button(shell, SWT.PUSH);
		okButton.setText(IDialogConstants.OK_LABEL);
		okButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		okButton.addListener(SWT.Selection, e -> {
			shell.dispose();

			try {
				Activator.getDefault().stop(Activator.getDefault().getBundle().getBundleContext());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}
	
	private void disposeResources() {
		background.dispose();
		display.dispose();
		shell.dispose();
		styledText.dispose();
	}

}
