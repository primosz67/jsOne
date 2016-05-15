package com.tahona.js;

import com.tahona.js.execute.JsOneCompiler;
import com.tahona.js.server.JsOneDaemon;

/**
 * 
 * License: GPL v3
 * 
 * @author Przemyslaw Zajac
 *
 */
public class JsOne {
	public static void main(final String[] args) {
		final JsOneCompiler compiler = new JsOneCompiler(args);
		new JsOneDaemon(compiler).listen();
	}
}