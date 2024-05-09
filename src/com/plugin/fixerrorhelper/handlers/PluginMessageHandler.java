package com.plugin.fixerrorhelper.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;

import com.plugin.fixerrorhelper.Activator;
import com.plugin.fixerrorhelper.core.gpt.GPTService;
import com.plugin.fixerrorhelper.core.gpt.model.Language;
import com.plugin.fixerrorhelper.preferences.PreferenceHelper;
import com.plugin.fixerrorhelper.util.ConsoleMessageManager;
import com.plugin.fixerrorhelper.widget.FriendlyConsoleOutput;

public class PluginMessageHandler extends AbstractHandler {

	public PluginMessageHandler() {}

	@Override
	public Object execute(ExecutionEvent event) {
		String consoleOutput = ConsoleMessageManager.getProcessConsoleOutput();

		var store = Activator.getDefault().getPreferenceStore();

		var language = PreferenceHelper.getLanguage(store);
		var apiKey = PreferenceHelper.getApiKey(store);

		var message = GPTService.makeFriendlyConsole(consoleOutput, Language.of(language), apiKey);

		FriendlyConsoleOutput.init(message)
							 .draw(e -> { Activator.stop(); })
							 .open();

		return null;
	}

}
