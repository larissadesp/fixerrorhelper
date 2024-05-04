package com.plugin.fixerrorhelper;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
	
	public static final String PLUGIN_ID = "com.plugin.fixerrorhelper"; //$NON-NLS-1$
	private static Activator plugin;

	public Activator() {}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

	public static void stop() {
		try {
			plugin.stop(plugin.getBundle().getBundleContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO
	public static String getPreference(String key) {
        return getDefault().getPreferenceStore().getString(key);
    }

}
