package com.tahona.js.template;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class TemplateEngine {

	private String template;
	
	Map<String, TagDefinition> definitions = new HashMap<String, TagDefinition>() ;

	public static void main(String[] args) throws IOException {
		System.err.println("Start");
		File file = new File("../Utilis/Template.js");
		new TemplateEngine(FileUtils.readFileToString(file));
	}

	public TemplateEngine(String templates) throws IOException {
		String[] split = templates.split("@Template");

		for (String template : split) {
			if (StringUtils.isNotBlank(template)) {
				TagDefinition definition = splitByDefinition(template);
				definitions.put(definition.getTag(), definition);
			}
		}
		File file = new File("../Utilis/Test.js");
		String code = FileUtils.readFileToString(file);
		
		for (Entry<String, TagDefinition> definitionEntry : definitions.entrySet()) {
			while(code.contains(definitionEntry.getKey()+" ")) {
				code = definitionEntry.getValue().evaluate(code);
			}
		}
		
		System.err.println(code);
	}

	private TagDefinition splitByDefinition(String template) {

		String trimmedTemplate = template.trim();
		String[] split = trimmedTemplate.split("[{}]");

		String[] tagAndParam = split[0].replaceAll("\\s", "").split("\\$");
		String tag = tagAndParam[0];
		String param = tagAndParam[1];

		//remove definition bracket's  and tag signature;
		String content = TemplatePattern.removeRootBracket(trimmedTemplate.replace(split[0], ""));

		TagDefinition tagDefinition = new TagDefinition();
		tagDefinition.setTag(tag);
		tagDefinition.setParam(param);
		tagDefinition.setContent(content);

		return tagDefinition;
	}

}
