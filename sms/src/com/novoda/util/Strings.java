package com.novoda.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strings {

	private static final String	ANSWER_PATTERN	= "([\\d])";

	public static String getChoice(String str) {
		String answer = null;

		if (str.contains("#")) {
			Matcher matcher = Pattern.compile(ANSWER_PATTERN).matcher(str);

			if (matcher.find()) {
				answer = Character.toString(str.charAt(matcher.start()));
			}
		}

		return answer;
	}
}
