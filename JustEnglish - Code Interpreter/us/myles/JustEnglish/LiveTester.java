package us.myles.JustEnglish;

import java.util.HashMap;
import java.util.Scanner;

public class LiveTester {

	public static void main(String[] args) {
		Scanner ss = new Scanner(System.in);
		HashMap<String, String> vars = new HashMap<String, String>();
		while (true) {
			String s = ss.nextLine();
			if (s.equals("end"))
				break;
			if(s.equals("clr")){
				vars = new HashMap<String, String>();
				continue;
			}
			ScriptObject o = new ScriptParser().parseScript(s);
			// Gson g = new GsonBuilder().create();
			// System.out.println(g.toJson(o));
			new ScriptExecutor().runScript(o, vars);
		}
		ss.close();
	}

}
