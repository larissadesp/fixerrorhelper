package com.plugin.fixerrorhelper.widget;

import java.util.HashMap;
import java.util.Map;

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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.plugin.fixerrorhelper.messages.Messages;

public class FriendlyConsoleOutput {

	private static final String TITLE = "FixErrorHelper";
	
	private static final String[] TEXT_SECTION_HEADERS = { 
			Messages.cause,
			Messages.error, 
			Messages.possibleSolutions 
	};
	
	private static final Map<String, Color> HEADER_COLORS = new HashMap<>();

	private Display display;
	private Color backgroundColor;
	private String message;
	private Shell shell;

	private FriendlyConsoleOutput(String message) {
		this.display = Display.getDefault();
		this.backgroundColor = new Color(display, 240, 240, 240);
		this.message = message;
	}

	public static FriendlyConsoleOutput init(String message) {
		return new FriendlyConsoleOutput(message);
	}

	public Shell draw(final Listener closeListener) {
		createShell();
		createUIComponents();
		createOkButton(closeListener);

		this.shell.addListener(SWT.Close, closeListener);

		return this.shell;
	}

	private void createShell() {
		this.shell = new Shell(display, SWT.SHELL_TRIM | SWT.RESIZE);
		shell.setText(TITLE);
		shell.setSize(450, 300);
		shell.setLayout(new GridLayout());
		shell.setBackground(backgroundColor);
	}

	private void createUIComponents() {
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setBackground(backgroundColor);

		var styledText = new StyledText(composite, SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		styledText.setText(message);
		styledText.setBackground(backgroundColor);

		formatTextSections(styledText);
	}

	private void setHeaderColors() {
		HEADER_COLORS.put(Messages.cause, new Color(display, 255, 190, 0));
		HEADER_COLORS.put(Messages.error, new Color(display, 255, 0, 0));
		HEADER_COLORS.put(Messages.possibleSolutions, new Color(display, 30, 144, 255));
	}

	private void formatTextSections(StyledText styledText) {
		this.setHeaderColors();

		for (String header : TEXT_SECTION_HEADERS) {
			int index = message.indexOf(header);

			if (index >= 0) {
				StyleRange styleRange = new StyleRange();
				styleRange.start = index;
				styleRange.length = header.length();
				styleRange.fontStyle = SWT.BOLD;
				styleRange.foreground = HEADER_COLORS.getOrDefault(header, display.getSystemColor(SWT.COLOR_BLACK));

				styledText.setStyleRange(styleRange);
			}
		}
	}
	
	private void createOkButton(final Listener listener) {
		Button okButton = new Button(shell, SWT.PUSH);

		okButton.setText(IDialogConstants.OK_LABEL);
		okButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));

		okButton.addListener(SWT.Selection, e -> {
			shell.dispose();
			listener.handleEvent(e);
		});
	}

}
