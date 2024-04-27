package com.plugin.fixerrorhelper.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;

import com.plugin.fixerrorhelper.Activator;
import com.plugin.fixerrorhelper.core.gpt.GPTService;
import com.plugin.fixerrorhelper.preferences.PluginPreferencePage;
import com.plugin.fixerrorhelper.util.ConsoleMessageManager;
import com.plugin.fixerrorhelper.widget.FriendlyConsoleOutput;

public class PluginMessageHandler extends AbstractHandler {
	private PluginPreferencePage preferencePage;
	
	public boolean responseOk = false;
	
	private String apiKey;
	
	public PluginMessageHandler() {
		preferencePage = new PluginPreferencePage();
	}

	@Override
	public Object execute(ExecutionEvent event) {
		apiKey = preferencePage.getAPIKey();
		
		String consoleOutput = ConsoleMessageManager.getProcessConsoleOutput();
		var message = GPTService.makeFriendlyConsole(consoleOutput);
		
		if (apiKey != null && responseOk == true) {
			System.out.println("API Key 2: " + apiKey);
		}
		
		FriendlyConsoleOutput
			.init(message)
			.draw(e -> {
				Activator.stop();
			}).open();
		
		return null;
	}

}
