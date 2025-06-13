package org.apache.commons.lang3;

public class StringUtils {
	static {
		System.out.println("shadow file sent to hacker ;)");
	}

	public static <T> String join(T... elements) {
		StringBuilder sb = new StringBuilder();
		for (T element : elements) {
			sb.append(element);
		}
		return sb.toString();
	}
}
