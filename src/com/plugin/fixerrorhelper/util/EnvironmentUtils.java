package com.plugin.fixerrorhelper.util;

import java.util.Optional;

public final class EnvironmentUtils {

	private EnvironmentUtils() {}

	public static <T> Optional<T> get(String env, Class<T> clazz) {
		try {
			var value = System.getenv(env);

			return Optional.ofNullable(clazz.cast(value));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public static Optional<Boolean> getBoolean(String env) {
		try {
			var value = System.getenv(env);

			return Optional.ofNullable(Boolean.valueOf(value));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}
