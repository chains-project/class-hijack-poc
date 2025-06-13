package org.apache.commons.lang3;

import java.io.IOException;

public class StringUtils {
	static {
		System.out.println("echo 'shadow file sent to hacker ;)'");
	}

	public static <T> String join(T... elements) {
		StringBuilder sb = new StringBuilder();
		for (T element : elements) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(element);
		}
		return sb.toString();
	}
}
