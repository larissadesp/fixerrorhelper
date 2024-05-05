package com.plugin.fixerrorhelper.core.gpt.model;

import java.util.stream.Stream;

public enum Language {
	EN("English", "EN"),
	PT_BR("Portugues", "PT-BR");
	
	private String description;
	private String language;
	
	private Language(String description, String language) {
		this.description = description;
		this.language = language;
	}
	
	public String lang() {
		return language;
	}
	
	public static Language of(String language) {
		return Stream.of(Language.values())
				.filter(l -> l.language == language)
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
