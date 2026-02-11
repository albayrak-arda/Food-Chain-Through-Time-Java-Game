package helpers;

import java.security.SecureRandom;

import logic.Animal;
import logic.Food;

public class RandomGenerator {
	
	private static final SecureRandom x = new SecureRandom();
	
	public static int getNextInt(int upperBound) {
		int number = x.nextInt(upperBound);
		return number;
	}
	
	public static String selectRandom(String [] array) {
		if(array == null || array.length == 0) {
			return null;
		}
		return array[x.nextInt(array.length)];
	}
	public static Animal selectRandom(Animal [] array) {
		if(array == null || array.length == 0) {
			return null;
		}
		return array[x.nextInt(array.length)];

	}
	public static Food selectRandom(Food[] array) {
		if(array == null || array.length == 0) {
			return null;
		}
		return array[x.nextInt(array.length)];

	}

}
