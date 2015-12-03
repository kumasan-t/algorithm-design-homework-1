package AWEPARTY;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AwesomeParty {
	
	public static void main(String[] args) throws IOException {
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(in);
		
		int testCases = Integer.parseInt(br.readLine());
		String inLine = "";
		for ( ; testCases > 0; testCases--) {
			int numberOfPeople = Integer.parseInt(br.readLine());
			int[] sequence = new int[numberOfPeople];
			inLine = br.readLine();
			String[] inLineSplit = inLine.split(" ");
			for (int i = 0; i < inLineSplit.length; i++) {
				sequence[i] = Integer.parseInt(inLineSplit[i]);
			}
			System.out.println(existsConfiguration(sequence, numberOfPeople) ? "YES" : "NO");
		}
	}
	
	public static boolean existsConfiguration(int[] sequence, int n) {
		int w = n;
		int b = 0;
		int s = 0;
		int c = 0;
		if (!checkParity(sequence)) {
			return false;
		}
		sort(sequence,getMaxValue(sequence));
		for (int k = 1; k <= n; k++) {
			b = b + sequence[k-1];
			c = c + w - 1;
			while( w > k && sequence[w-1] <= k) {
				s = s + sequence[w-1];
				c = c - k;
				w--;
			}
			if (b > c + s) {
				return false;
			} else if (w == k) {
				return true;
			}
		}
		return false;
	}
	public static boolean checkParity(int[] array) {
		int sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum % 2 == 0 ? true : false;
	}
	
	public static int getMaxValue(int[] array) {
		int max = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= max) {
				max = array[i];
			}
		}
		return max;
	}
	
	public static void sort(int[] a, int maxVal) {
		int [] bucket=new int[maxVal+1];

		for (int i=0; i<bucket.length; i++) {
			bucket[i]=0;
		}

		for (int i=0; i<a.length; i++) {
			bucket[a[i]]++;
		}

		int outPos=a.length - 1;
		for (int i=0; i<bucket.length; i++) {
			for (int j=0; j<bucket[i]; j++) {
				a[outPos--]=i;
			}
		}
	}
	
	public static void printArray(int[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + ", ");
		}
		System.out.println("");
	}
}
