package com.tahona.js.execute;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.tahona.js.tool.Logger;

public class StringErrorReporter implements ErrorReporter {

	private String fileName;
	
	private List<String> message = new ArrayList<String>();

	@Override
	public void error(String arg0, String arg1, int arg2, String arg3, int arg4) {
		message.add("("+arg2+", "+arg4+")"+" "+arg0+".. code: '"+arg3.trim()+"'");
	}

	
	@Override
	public EvaluatorException runtimeError(String arg0, String arg1, int arg2, String arg3, int arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void warning(String arg0, String arg1, int arg2, String arg3, int arg4) {
		// TODO Auto-generated method stub

	}

	public void setFile(String fileName) {
		this.fileName = fileName;
		message.clear();
	}


	public List<String> getMessages() {
		return message;
	}

}
