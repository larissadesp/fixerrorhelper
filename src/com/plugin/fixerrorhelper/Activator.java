package com.plugin.fixerrorhelper;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.plugin.fixerrorhelper.messages.Messages;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.plugin.fixerrorhelper"; //$NON-NLS-1$
	private static Activator INSTANCE;

	public Activator() {}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		Messages.loadProperties();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	public static Activator getDefault() {
		return INSTANCE;
	}

	public static void stop() {
		try {
			INSTANCE.stop(INSTANCE.getBundle().getBundleContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
