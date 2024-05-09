package com.plugin.fixerrorhelper.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.plugin.fixerrorhelper.Activator;
import com.plugin.fixerrorhelper.constants.PreferenceConstants;

public class PluginPreferenceInitializer extends AbstractPreferenceInitializer {

	public PluginPreferenceInitializer() {}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		if (!store.contains(PreferenceConstants.PREFERENCE_API_KEY)) {
			store.setValue(PreferenceConstants.PREFERENCE_API_KEY, "");
		}

		if (!store.contains(PreferenceConstants.PREFERENCE_LANGUAGE)) {
			store.setValue(PreferenceConstants.PREFERENCE_LANGUAGE, PreferenceConstants.LANGUAGE_EN);
		}
	}

}
