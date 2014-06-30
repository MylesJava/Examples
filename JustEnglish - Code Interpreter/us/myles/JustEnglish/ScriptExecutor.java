package us.myles.JustEnglish;

import java.util.HashMap;
import java.util.Random;

public class ScriptExecutor {
	public final String version = "0.1.2";
	private int loop;

	/**
	 * Run a script.
	 * 
	 * @param container You obtain this from the parser.
	 * @param vars This should be a blank HashMap or a previously used one to continue on variables.
	 */
	public void runScript(ScriptObject container, HashMap<String, String> vars) {
		try {
			ScriptObject currentStack = container;
			solveStack(currentStack, vars);
		} catch (Exception e) {
			error("Malfunctional Runtime error.");
		}
	}

	/**
	 * Iterates through stack, checks next object, then checks children, then
	 * checks while logic.
	 * 
	 * @param currentStack
	 * @param variables
	 */
	private void solveStack(ScriptObject currentStack, HashMap<String, String> variables) throws EscapeWhileLoop {
		if (currentStack == null)
			return;
		if (currentStack.getType() != Type.BASE) {
			if (currentStack.getValue().equals("upstack"))
				return;
			boolean nextStack = execute(currentStack, variables, currentStack);
			if (!nextStack)
				return;
		}
		for (ScriptObject c : currentStack.children) {
			if (c.getValue().equals("upstack")) {
				throw new EscapeWhileLoop();
			}
			solveStack(c, variables);
		}
		if (currentStack.getType() == Type.WHILE) {
			if (++loop >= 20) {
				error("Too many loops, limited to 20.");
				return;
			}
			try {
				solveStack(currentStack, variables);
			} catch (EscapeWhileLoop e) {
			}
		}

	}

	/**
	 * Gets a variable
	 * 
	 * @param variables
	 * @param name
	 * @return Returns String "null" if not registered.
	 */
	private String getVariable(HashMap<String, String> variables, String name) {
		if (variables.containsKey(name))
			return variables.get(name);
		else
			return "null";
	}

	/**
	 * Sets a Variable
	 * 
	 * @param variables
	 * @param name
	 * @param value
	 */
	private void setVariable(HashMap<String, String> variables, String name, String value) {
		variables.put(name, value);
	}

	/**
	 * Executes a {@link ScriptObject}, this will echo etc.
	 * 
	 * @param o
	 * @param variables
	 * @param currentStack
	 * @return Boolean of if it should read children.
	 */
	private boolean execute(ScriptObject o, HashMap<String, String> variables, ScriptObject currentStack) {
		try {
			if (o.getType() == Type.COMMAND) {
				String[] args = o.getArgs();
				int i = 0;
				for (String arg : args) {
					if (arg.startsWith("~") || arg.startsWith("$")) {
						args[i] = getVariable(variables, arg.substring(1));
					}
					i++;
				}
				if (o.getValue().equals("echo")) {
					boolean x = false;
					String z = "";
					for (String s : args) {
						if (x) {
							if (z.equals(""))
								z = s;
							else
								z = z + " " + s;
						}
						if (!x)
							x = true;
					}
					say(z);
					return false;
				}
				if (o.getValue().equals("set")) {
					// set x to y.
					String key = args[1];
					String value = "";
					if (args.length == 3)
						value = args[2];
					else
						value = args[3];
					setVariable(variables, key, value);
					return false;
				}
				if (o.getValue().equals("add")) {
					String arg1;
					String arg2;
					if (args.length == 3) {
						arg1 = args[1];
						arg2 = args[2];
					} else {
						arg1 = args[1];
						arg2 = args[3];
					}
					if (isStringInt(arg1) && !isStringInt(arg2)) {
						setVariable(variables, arg2, (Integer.parseInt(getVariable(variables, arg2)) + Integer.parseInt(arg1)) + "");
						return false;
					}
					if (!isStringInt(arg1) && isStringInt(arg2)) {
						setVariable(variables, arg1, (Integer.parseInt(getVariable(variables, arg1)) + Integer.parseInt(arg2)) + "");
						return false;
					}
					error("Compile Error: Cannot set numeric to another.");

				}
				if (o.getValue().equals("subtract")) {
					String arg1;
					String arg2;
					if (args.length == 3) {
						arg1 = args[1];
						arg2 = args[2];
					} else {
						arg1 = args[1];
						arg2 = args[3];
					}
					if (isStringInt(arg1) && !isStringInt(arg2)) {
						setVariable(variables, arg2, (Integer.parseInt(getVariable(variables, arg2)) - Integer.parseInt(arg1)) + "");
						return false;
					}
					if (!isStringInt(arg1) && isStringInt(arg2)) {
						setVariable(variables, arg1, (Integer.parseInt(getVariable(variables, arg1)) - Integer.parseInt(arg2)) + "");
						return false;
					}
					error("Compile Error: Cannot set numeric to another.");

				}
				if (o.getValue().equals("divide")) {
					String arg1;
					String arg2;
					if (args.length == 3) {
						arg1 = args[1];
						arg2 = args[2];
					} else {
						arg1 = args[1];
						arg2 = args[3];
					}
					if (isStringInt(arg1) && !isStringInt(arg2)) {
						setVariable(variables, arg2, (Integer.parseInt(getVariable(variables, arg2)) / Integer.parseInt(arg1)) + "");
						return false;
					}
					if (!isStringInt(arg1) && isStringInt(arg2)) {
						setVariable(variables, arg1, (Integer.parseInt(getVariable(variables, arg1)) / Integer.parseInt(arg2)) + "");
						return false;
					}
					error("Compile Error: Cannot set numeric to another.");

				}
				if (o.getValue().equals("multiply")) {
					String arg1;
					String arg2;
					if (args.length == 3) {
						arg1 = args[1];
						arg2 = args[2];
					} else {
						arg1 = args[1];
						arg2 = args[3];
					}
					if (isStringInt(arg1) && !isStringInt(arg2)) {
						setVariable(variables, arg2, (Integer.parseInt(getVariable(variables, arg2)) * Integer.parseInt(arg1)) + "");
						return false;
					}
					if (!isStringInt(arg1) && isStringInt(arg2)) {
						setVariable(variables, arg1, (Integer.parseInt(getVariable(variables, arg1)) * Integer.parseInt(arg2)) + "");
						return false;
					}
					error("Compile Error: Cannot set numeric to another.");

				}
				if (o.getValue().equals("get")) {
					say(getVariable(variables, args[1]));
					return false;
				}
				if (o.getValue().equals("random")) {
					String var = args[1];
					int lower = 1;
					int upper = 10;
					if (args.length == 3)
						upper = Integer.parseInt(args[2]);
					if (args.length == 4) {
						lower = Integer.parseInt(args[2]);
						upper = Integer.parseInt(args[3]);
					}
					int num = new Random().nextInt(upper - lower + 1) + lower;
					setVariable(variables, var, num + "");
					return false;
				}
			}
			if (o.getType() == Type.IF || o.getType() == Type.WHILE) {
				String variableName = o.getArgs()[1];
				String statement = o.getArgs()[2];
				String variable = getVariable(variables, variableName);
				String compareTo = o.getArgs()[3];
				if (statement.equals("is") || statement.equals("=") || statement.equals("==")) {
					if (variable.equals(compareTo)) {
						return true;
					}
				}
				if (statement.equals("isnt") || statement.equals("!") || statement.equals("!=")) {
					if (!variable.equals(compareTo)) {
						return true;
					}
				}
				if (statement.equals(">") || statement.equals("<") || statement.equals("<=") || statement.equals(">=")) {
					if (isStringInt(variable) && isStringInt(compareTo)) {
						int a = Integer.parseInt(variable);
						int b = Integer.parseInt(compareTo);
						if (statement.equals(">"))
							return a > b;
						if (statement.equals("<"))
							return a < b;
						if (statement.equals("<="))
							return a <= b;
						if (statement.equals(">="))
							return a >= b;
						error("Invalid Operator");
					} else {
						error("Not Number Exception");
					}
				}
			}
			return false;
		} catch (Exception e) {
			error("Runtime Error at " + o.getValue());
			return false;
		}
	}

	/**
	 * Prints an Error
	 * 
	 * @param e
	 */
	public void error(String e) {
		System.out.println("Error: " + e);
	}

	/**
	 * Prints a String
	 * 
	 * @param s
	 */
	public void say(String s) {
		System.out.println(s);
	}

	/**
	 * Tests a string if it is an integer.
	 * 
	 * @param s
	 * @return True if the String is a integer.
	 */
	private boolean isStringInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
}
