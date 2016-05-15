package com.tahona.js.template;

import java.util.Scanner;

public class TemplatePattern {

	public static final String FIRST_BRACKET = "^\\{";
	public static final String LAST_BRACKET = "\\}$";
	public static final String END_ONE_LINE_SIGNATURE = ";";

	public static String removeRootBracket(String replace) {
		return replace
				.trim()
				.replaceFirst(TemplatePattern.FIRST_BRACKET, "")
				.replaceFirst(TemplatePattern.LAST_BRACKET, "")
				.trim();
	}

	public static String getBracketContentIfFirst(String value) {
		if (value.startsWith("{")) {

			Scanner scanner = new Scanner(value);
			String pattern = "[\\{{1}\\}{1}]";
			scanner.next(pattern);
			int findedCount = 1;
			int counter = 1;
			while (findedCount != 0 && scanner.hasNext(pattern)) {
				final String finded = scanner.next(pattern);
				if ("{".equals(finded)) {
					findedCount++;
					counter++;
				} else if ("}".equals(finded))
					findedCount--;
			}
			int from = 0;
			while (counter > 0) {
				from = value.indexOf("}", from);
				counter--;
			}
			System.err.println("frpm : "+from);
			return value.substring(0, from);
		}
		return value;

	}

}
