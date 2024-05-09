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

	public static String getProcessConsoleOutput() {
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

	public static boolean isStackTrace(String message) {
		String stackTraceRegex = "(?m)^.*?Exception.*(?:\\R+^\\s*at .*)+";
		Pattern pattern = Pattern.compile(stackTraceRegex);
		Matcher matcher = pattern.matcher(message);

		return matcher.find();
	}

	public static boolean isJavaLangException(String message) {
		String javaLangErrorRegex = "(?m)((java\\.lang\\.|java\\.net\\.)[A-Za-z]+(Error|Exception)):";
		Pattern pattern = Pattern.compile(javaLangErrorRegex);
		Matcher matcher = pattern.matcher(message);

		return matcher.find();
	}

}
