package us.myles.HigherOrLower;

import java.util.Random;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		int number = new Random().nextInt(100) + 1;
		boolean guessed = false;
		System.out.println("Enter a guess (Number is between 1-100 Inclusive)");
		Scanner scanner = new Scanner(System.in);
		String guess = null;
		while (!guessed && scanner.hasNextLine())
			System.out.println((guess = scanner.nextLine()).equals(number + "") ? ((guessed = true) ? "Congrats you win! My number was " + guess : "") : (Integer.parseInt(guess) > number) ? "Guess Lower" : "Guess Higher");
		scanner.close();
	}
}
