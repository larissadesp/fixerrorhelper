package com.plugin.fixerrorhelper.core.gpt.model;

import java.util.stream.Stream;

import com.plugin.fixerrorhelper.constants.PreferenceConstants;

public enum Language {
	
	EN("English", PreferenceConstants.LANGUAGE_EN),
	PT_BR("Portuguese", PreferenceConstants.LANGUAGE_PT_BR);
	
	private String description;
	private String language;
	
	private Language(String description, String language) {
		this.description = description;
		this.language = language;
	}
	
	public String getDescription() {
	    return description;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public static Language of(String language) {
		return Stream.of(Language.values())
				.filter(l -> l.language.equals(language))
				.findFirst()
				.orElse(EN);
	}
	
	public static String[][] keyMap() {
		String[][] array = new String[Language.values().length][2];
		
		Stream.of(Language.values())
			.map(l -> new String[]{l.description, l.language})
			.toList().toArray(array);
		
		return array;
	}
	
}
