package us.myles.CodeCrack;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		String number = new DecimalFormat("0000").format(new Random().nextInt(10000));
		boolean guessed = false;
		System.out.println("Enter a guess, (Must be 4 digits long!):");
		Scanner scanner = new Scanner(System.in);
		String guess = null;
		while (!guessed && scanner.hasNextLine()) {
			if ((guess = scanner.nextLine()).length() != 4) {
				System.out.println("Enter a 4 digit guess");
				continue;
			}
			if (!(guessed = (guess.equals(number))) && guess != null)
				for (int i = 0; i < guess.length(); i++)
					System.out.print(number.charAt(i) == guess.charAt(i) ? "Y" : (number.indexOf(guess.charAt(i)) != -1 && guess.charAt(number.indexOf(guess.charAt(i))) != guess.charAt(i)) ? "?" : "N");
			if (!guessed && guess != null)
				System.out.println();
		}
		System.out.println("Congrats you win! Restart the application to try again!");
		scanner.close();
	}
}
