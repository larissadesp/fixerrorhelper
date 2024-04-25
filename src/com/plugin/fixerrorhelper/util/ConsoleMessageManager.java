package com.plugin.fixerrorhelper.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.TextConsole;

public class ConsoleMessageManager {

	private static final String PROCESS_CONSOLE_INTERNAL_CLASS = "org.eclipse.debug.internal.ui.views.console.ProcessConsole";

	public String getProcessConsoleOutput() {
		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
		IConsole[] consoles = consoleManager.getConsoles();

		for (IConsole console : consoles) {
			if (console.getClass().getName().equals(PROCESS_CONSOLE_INTERNAL_CLASS)) {
				TextConsole textConsole = (TextConsole) console;
				IDocument document = textConsole.getDocument();
				String consoleOutputText = document.get();

				return consoleOutputText;
			}
		}

		return null;
	}

	public boolean isJavaErrorOrException(String message) {
		//TODO "return message(?) instanceof Throwable;"
		if (containsStackTrace(message) && containsBasicErrorKeywords(message)) {
			return true;
		}

		return false;
	}

	private boolean containsStackTrace(String message) {
		String regex = "^\\s*at\\s+[\\w.$]+\\(.*?\\)";
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(message);

		return matcher.find();
	}

	private boolean containsBasicErrorKeywords(String message) {
		return message.toLowerCase().contains("error")
				|| message.toLowerCase().contains("exception");
	}
	 
}
