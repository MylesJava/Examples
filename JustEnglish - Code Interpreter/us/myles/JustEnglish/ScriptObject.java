package us.myles.JustEnglish;

import java.util.ArrayList;
import java.util.List;

public class ScriptObject {
	private Type type;
	public List<ScriptObject> children;
	private String value;
	private String[] args;
	private transient ScriptObject parent;

	public ScriptObject(Type command, ScriptObject parent) {
		this.parent = parent;
		this.setType(command);
		this.children = new ArrayList<ScriptObject>();
	}

	public ScriptObject setValue(String cmd) {
		this.value = cmd;
		return this;
	}

	public String getValue() {
		return value;
	}

	public ScriptObject setArgs(String[] args) {
		this.args = args;
		return this;
	}

	public String[] getArgs() {
		return args;
	}

	public ScriptObject getParent() {
		if (this.parent == null)
			return this;
		return this.parent;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}

enum Type {
	COMMAND, IF, BASE, WHILE
}