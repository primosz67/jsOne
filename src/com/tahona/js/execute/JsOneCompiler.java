package com.tahona.js.execute;

import java.io.IOException;
import java.util.TreeMap;

import com.tahona.js.tool.Logger;

public class JsOneCompiler {

	private final TreeMap<String, String> parameters;
	private final JsOneBuilder builder;

	public JsOneCompiler(final String[] args) {
		parameters = new TreeMap<String, String>();

		for (int i = 0; i < args.length; i++) {
			if (i % 2 == 1) {
				parameters.put(args[i - 1], args[i]);
				Logger.d("loaded " + args[i - 1] + "...");
			}
		}

		builder = new JsOneBuilder(parameters);
	}

	private boolean firstTimeRun = true;

	public void compile() {
		try {
			if (builder.isResourceChanged() || firstTimeRun) {
				Logger.d(": compiling..");
				builder.build();
				Logger.d(": compiled");
				firstTimeRun = false;
			}
		} catch (final IOException e) {
		} catch (final IllegalArgumentException e) {
		}
	}
}
