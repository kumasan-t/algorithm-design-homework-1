package OCCURS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.io.IOException;

public class Occurs {
	
	public static int findPattern(String sequence, String pattern) {
		int[] currentRowVector = new int[pattern.length()+1];
		int[] previousRowVector = new int[pattern.length()+1];
		previousRowVector[0] = 1;
		currentRowVector[0] = 1;

		for (int i = 0; i < sequence.length(); i++) {
			for (int j = 0; j < pattern.length(); j++) {
				if (sequence.charAt(i) == pattern.charAt(j)) {
					currentRowVector[j+1] = (previousRowVector[j] + previousRowVector[j+1])%(1000000);
				}
			}
			previousRowVector = currentRowVector.clone();			

		}
		return currentRowVector[currentRowVector.length-1];
	}

	public static void main(String[] args) throws IOException {
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(in);

		int numberOfTestCases = Integer.parseInt(br.readLine());
		for(int i = 0; i < numberOfTestCases; i++) {
			String sequence = br.readLine();
			String pattern = br.readLine();
			System.out.println(findPattern(sequence,pattern));
		}				
	}
}

