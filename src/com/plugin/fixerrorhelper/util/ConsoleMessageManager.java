package com.plugin.fixerrorhelper.util;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.TextConsole;

public class ConsoleMessageManager {

	private static final String PROCESS_CONSOLE_INTERNAL_CLASS = "org.eclipse.debug.internal.ui.views.console.ProcessConsole";
	private static final String JAVA_LANG_THROWABLE = "java.lang.Throwable";

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

	public boolean hasThrowableHierarchy(String message) {
		try {
			Throwable throwable = (Throwable) Class.forName(JAVA_LANG_THROWABLE).getConstructor(String.class).newInstance(message);
			return throwable instanceof Throwable;
		} catch (Exception e) {
			return false;
		}
	}

}
