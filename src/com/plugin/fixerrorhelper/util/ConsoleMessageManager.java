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

	public static boolean isJavaErrorException(String message) {
		String javaErrorRegex = "\\b(java\\.[a-z]+\\.[a-zA-Z]+(?:Error|Exception))\\b";

		Pattern pattern = Pattern.compile(javaErrorRegex);
		Matcher matcher = pattern.matcher(message);

		return matcher.find();
	}

	public static boolean checkThrowable(String message) {
		String className = getStackTraceClassName(message);

		if (className != null) {
			try {
				Class<?> throwableClass = Class.forName("java.lang.Throwable");
				Class<?> exceptionClass = Class.forName(className);
	
				if (throwableClass.isAssignableFrom(exceptionClass)) {
					return true;
				}
			} catch (ClassNotFoundException e) {
				return false;
			}
		}

		return false;
	}
	
	public static String getStackTraceClassName(String message) {
	    String regex = "(?m)^(?:Exception in thread \".*\" |Caused by: )?([a-zA-Z_$][a-zA-Z\\d_$]*\\.[a-zA-Z_$][a-zA-Z\\d_$]*(?:\\.[a-zA-Z_$][a-zA-Z\\d_$]*)*)";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(message);

	    String className = null;
	    
	    while (matcher.find()) {
	        className = matcher.group(1);
	    }

	    return className;
	}

}
