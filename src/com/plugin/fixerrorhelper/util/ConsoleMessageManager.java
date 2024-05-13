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

		var teste = matcher.find();
		return teste;
	}

	public static boolean isJavaErrorException(String message) {
		String javaErrorRegex = "(?m)(java\\.[a-z]+\\.[a-zA-Z]+(Error|Exception)):";

		Pattern pattern = Pattern.compile(javaErrorRegex);
		Matcher matcher = pattern.matcher(message);

		var teste = matcher.find();
		return teste;
	}

	public static String getStackTraceClassName(String message) {
		String regex = "Caused by: (.+)";
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(message);

		if (matcher.find()) {
			String causedBy = matcher.group(1);
			
			regex = "([^:\\s]+)";
            
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(causedBy);
            
            if (matcher.find()) {
                return matcher.group(1);
            }
		}

		return null;
	}

	public static boolean checkThrowable(String message) {
		String className = getStackTraceClassName(message);

		try {
			Class<?> throwableClass = Class.forName("java.lang.Throwable");
			Class<?> exceptionClass = Class.forName(className);

			if (throwableClass.isAssignableFrom(exceptionClass)) {
				return true;
			}
		} catch (ClassNotFoundException e) {
			return false;
		}

		return false;
	}

//	public static boolean checkThrowableInheritance(String message) {
//		return message.contains("Throwable") ||
//			   message.contains("Exception") ||
//	           message.contains("Error") ||
//	           message.contains("RuntimeException") ||
//	           message.contains("IOException") ||
//	           message.contains("AWTError") ||
//	           message.contains("ThreadDeath") ||
//	           message.contains("OutOfMemoryError") ||
//	           message.contains("ClassCastException") ||
//	           message.contains("ArrayIndexOutOfBoundsException") ||
//	           message.contains("NullPointerException") ||
//	           message.contains("InputMismatchException") ||
//	           message.contains("ArithmeticException");
//	}

}
