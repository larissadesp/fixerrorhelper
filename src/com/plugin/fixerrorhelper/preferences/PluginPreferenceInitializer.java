package com.plugin.fixerrorhelper.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.plugin.fixerrorhelper.Activator;
import com.plugin.fixerrorhelper.constants.PreferenceConstants;

public class PluginPreferenceInitializer extends AbstractPreferenceInitializer {

	public PluginPreferenceInitializer() {}

	@Override
	public void initializeDefaultPreferences() {
		//TODO
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		store.setValue(PreferenceConstants.FIELD_API_KEY, "...");
	}

}
