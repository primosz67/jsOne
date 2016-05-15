package com.tahona.js.template;

import static com.tahona.js.template.TemplatePattern.removeRootBracket;
import static org.apache.commons.lang3.StringUtils.replaceOnce;

public class TagDefinition {

	private String tag;
	private String param;
	private String content;

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTag() {
		return tag;
	}

	public String evaluate(String code) {

		String beforeTag = getBeforeTagCode(code);
		
		code = code.replace(beforeTag, "");
		String replaceFirst = code.trim().split(tag+"", 2)[1];
		String[] nameAndContent = replaceFirst.trim().split("\\s", 2);

		String paramName = nameAndContent[0];

		String value = "";
		if (false == paramName.endsWith(";")) {
			value = findValue(nameAndContent[1].trim());
		} else {
			paramName = paramName.substring(0, paramName.length() - 1);
		}
		String afterTag = replaceOnce(nameAndContent[1], value, ""); 
		if (afterTag.startsWith(";")) {
			afterTag = afterTag.substring(1);
		}

		
		value = removeRootBracket(value);
		System.out.println("v:"+value+"tag:"+tag);
		return beforeTag + content.replaceAll("\\$" + param, paramName).replaceAll("\\[{1}content\\]{1}", value)+afterTag;
	}

	private String getBeforeTagCode(String code) {
		int indexOf = code.indexOf("//"+tag);
		String beforeTag = code.substring(0, indexOf);
		return beforeTag;
	}

	private String findValue(String nameAndContent) {
		if (nameAndContent.startsWith("{")) {
			return TemplatePattern.getBracketContentIfFirst(nameAndContent);
		}  else if (nameAndContent.startsWith(TemplatePattern.END_ONE_LINE_SIGNATURE)){
			return "";
		} 
		else if (nameAndContent.contains(TemplatePattern.END_ONE_LINE_SIGNATURE)) {
			int indexOf = nameAndContent.indexOf(TemplatePattern.END_ONE_LINE_SIGNATURE);
			return nameAndContent.substring(0, indexOf);
		}

		return "";
	}

}
