package com.plugin.fixerrorhelper.preferences;

import java.util.Objects;

import org.eclipse.jface.preference.IPreferenceStore;

import com.plugin.fixerrorhelper.constants.PreferenceConstants;

public class PreferenceHelper {
	
	public static String getApiKey(IPreferenceStore store) {
		if (Objects.isNull(store)) {
			return null;
		}
		
		return store.getString(PreferenceConstants.PREFERENCE_API_KEY);
	}

	public static String getLanguage(IPreferenceStore store) {
		if (Objects.isNull(store)) {
			return null;
		}
		
		return store.getString(PreferenceConstants.PREFERENCE_LANGUAGE);
	}
}
