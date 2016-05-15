package com.tahona.js.server;

import com.tahona.js.execute.JsOneCompiler;

public class JsOneDaemon implements Runnable {

	private final JsOneCompiler jsOneCompile;
	private Thread thread;

	public JsOneDaemon(final JsOneCompiler jsOneCompile) {
		this.jsOneCompile = jsOneCompile;
	}

	public void listen() {
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		try {
			long start = System.currentTimeMillis();
			while (true) {
				if (start + 1000 < System.currentTimeMillis()) {
					this.jsOneCompile.compile();
					start = System.currentTimeMillis();
					Thread.sleep(1000);
				}
			}
		} catch (InterruptedException e) {
		}
	}

}
