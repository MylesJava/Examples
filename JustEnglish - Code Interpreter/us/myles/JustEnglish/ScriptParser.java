package us.myles.JustEnglish;

public class ScriptParser {
	// FUNCTIONS/NAVIGATION
	public final String[] LINE_BREAK = { "\n", "then", ">>", ";" };
	public final String[] IF = { "if", "is", "?" };
	public final String[] WHILE = { "while", "for", "::" };
	// FLOW CONTROL
	public final String[] UPSTACK = { "continue", "up", "end", "^", "upstack", "break" };
	// FUNCTIONS
	public final String[] ECHO = { "say", "$", "echo" };
	// GETTERS SETTERS
	public final String[] SET = { "set", "put", ">" };
	public final String[] GET = { "get", "fetch", "<" };
	// OPERATORS
	public final String[] ADD = { "add", "+" };
	public final String[] SUBTRACT = { "subtract", "take", "-" };
	public final String[] MULTIPLY = { "multiplyf", "times", "*" };
	public final String[] DIVIDE = { "divide", "split", "/" };
	public final String[] RANDOM = { "rand", "random", "r" };

	/**
	 * This method simply turns the script into a 'Object Stack'
	 * {@link ScriptObject}
	 * 
	 * @param script
	 *            - This is the text based script to parse.
	 * @return The created Object, this can then be iterated through to
	 *         'execute' it.
	 */
	public ScriptObject parseScript(String script) {
		ScriptObject base = new ScriptObject(Type.BASE, null);
		ScriptObject current = base;
		String[] d = script.split(" ");
		String currentLine = "";
		for (String s : d)
			if (match(s, LINE_BREAK) || d[d.length].equals(s)) {
				current = parseLine(currentLine, current);
				currentLine = "";
			} else {
				if (currentLine.equals(""))
					currentLine = s;
				else
					currentLine = currentLine + " " + s;
			}
		return base;
	}

	/**
	 * This method takes a line and turns it into a corresponding
	 * {@link ScriptObject} With IF/WHILE it moves the current stack to it.
	 * For upstack (Essentially, end loop/if, it finds the parent and returns that.
	 * 
	 * @param currentLine
	 * @param current
	 * @return
	 */
	private ScriptObject parseLine(String currentLine, ScriptObject current) {
		String[] args = currentLine.split(" ");
		String cmd = null;
		boolean returnParent = false;
		if (match(args[0], ECHO))
			cmd = "echo";
		if (match(args[0], IF) || match(args[0], WHILE)) {
			cmd = match(args[0], IF) ? "if" : "while";
			ScriptObject x = new ScriptObject(match(args[0], IF) ? Type.IF : Type.WHILE, current).setValue(cmd).setArgs(args);
			current.children.add(x);
			return x;
		}
		if (match(args[0], SET))
			cmd = "set";
		if (match(args[0], GET))
			cmd = "get";
		if (match(args[0], UPSTACK)) {
			cmd = "upstack";
			boolean hasWhileParent = false;
			ScriptObject c = current;
			int l = 0;
			while (c.getType() != Type.BASE) {
				if (c.getType() == Type.WHILE && l != 0) {
					hasWhileParent = true;
				}
				c = c.getParent();
				l++;
			}
			if (!hasWhileParent)
				return current.getParent();
			returnParent = true;
		}
		if (match(args[0], ADD))
			cmd = "add";
		if (match(args[0], SUBTRACT))
			cmd = "subtract";
		if (match(args[0], MULTIPLY))
			cmd = "multiply";
		if (match(args[0], DIVIDE))
			cmd = "divide";
		if (match(args[0], RANDOM))
			cmd = "random";
		if (cmd == null) {
			System.out.println("Parser Error: Unknown command '" + cmd + "'!");
			return current;
		}
		current.children.add(new ScriptObject(Type.COMMAND, current).setValue(cmd).setArgs(args));
		if (returnParent)
			return current.getParent();
		return current;

	}

	/**
	 * Check if any of the params equal the arg..
	 * @param arg
	 * @param param
	 * @return
	 */
	private boolean match(String arg, String[] param) {
		for (String s : param)
			if (s.equals(arg))
				return true;
		return false;
	}
}
