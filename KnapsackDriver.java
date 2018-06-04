
// package GraphPackage;
// Java Program to illustrate reading from Text File
// using Scanner Class
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;;

public class KnapsackDriver {
	public static void main(String[] args) throws Exception {
		List<KnapsackItem> items = new ArrayList<KnapsackItem>();

		// initialize file & scanner classes
		// used to read from files and user input
		File weightFile = new File("weights.txt");
		File profitFile = new File("profits.txt");
		Scanner weights = new Scanner(weightFile);
		Scanner profits = new Scanner(profitFile);
		Scanner kb = new Scanner(System.in);

		int itemID = 1;
		while (weights.hasNextDouble() && profits.hasNextDouble()) {
			double weight = weights.nextDouble();
			double profit = profits.nextDouble();
			// create new KnapsackItem object and add it
			items.add(new KnapsackItem(itemID, weight, profit));
			itemID++;
		}
		// close files
		weights.close();
		profits.close();

		// display list of items
		System.out.println("+---------------------------------+");
		System.out.println("| item     weight  profit   ratio |");
		System.out.println("+---------------------------------+");
		for (KnapsackItem item : items) {
			System.out.println("| " + item + " |");
		}
		System.out.println("+---------------------------------+");
		System.out.println();

		// read input to get knapsack capacity
		System.out.print("Enter Knapsack Capacity: ");
		double knapsackCapacity = kb.nextDouble();
		System.out.println();

		// find solutions
		List<KnapsackItem> fractionalKnapsack = solveFractional(items, knapsackCapacity);
		List<KnapsackItem> zeroOneKnapsack = solveZeroOne(items, (int) knapsackCapacity);

		// display solutions
		System.out.println("+---------------------------------+");
		System.out.println("|       Fractional Knapsack       |");
		System.out.println("+---------------------------------+");
		for (KnapsackItem item : fractionalKnapsack) {
			System.out.println("| " + item + " |");
		}
		System.out.println("+---------------------------------+");
		System.out.println();
		System.out.println("+---------------------------------+");
		System.out.println("|       Zero / One Knapsack       |");
		System.out.println("+---------------------------------+");

		for (KnapsackItem item : zeroOneKnapsack) {
			System.out.println("| " + item + " |");
		}
		System.out.println("+---------------------------------+");

		kb.close();
	}

	// Zero One solver
	public static List<KnapsackItem> solveZeroOne(List<KnapsackItem> input, int capacity) {

		// clone original ordered list
		List<KnapsackItem> items = new ArrayList<KnapsackItem>(input);

		// sort new list in ascending order
		Collections.sort(items);

		// reverse to get descending order
		Collections.reverse(items);

		// create list for knapsack
		List<KnapsackItem> knapsack = new ArrayList<KnapsackItem>();

		// create matrix to find solutions
		int[][] matrix = new int[capacity + 1][items.size() + 1];

		// for every capacity increment
		for (int c = 1; c <= capacity; c++) {
			// for every available item
			for (int i = 0; i < items.size(); i++) {
				// find the value if we exclude our current item
				int previousProfit = matrix[c][i];
				// use this variable to calculate the max profit by including the new item
				int currentProfit = 0;

				// select new item from non sorted list
				KnapsackItem item = input.get(i);
				int itemProfit = (int) item.getProfit();
				int itemWeight = (int) item.getWeight();

				// if we can fit this item
				if (itemWeight <= c) {
					// we can calculate our profit by adding this item by finding
					// current item profit + profit for the remaining space after adding it
					currentProfit = itemProfit + matrix[Math.max(c - itemWeight, 0)][i];
				}

				// Math.max used to only take the new profit if it helped us
				matrix[c][i + 1] = Math.max(currentProfit, previousProfit);
			}

		}
		// display generated matrix in command line
		printMatrix(matrix);

		// initialize counter variables
		int c = capacity;
		int s = items.size();

		// while not out of the matrix
		while (c > 0 && s > 0) {
			// get current item for space
			// subtract 1 to zero-index
			KnapsackItem item = input.get(s - 1);

			// if adding this item was beneficial
			if (matrix[c][s - 1] < matrix[c][s] && item.getWeight() <= c) {

				// add our item
				knapsack.add(item);

				// go up a space and left by the amount we added to the knapsack
				c -= item.getWeight();
				s -= 1;
			} else {
				// skip this row
				s -= 1;
			}
		}
		return knapsack;
	}

	public static void printMatrix(int[][] table) {
		System.out.printf("%n+");
		for (int i = 1; i < table.length; i++) {
			System.out.print("----");
		}
		System.out.printf("--+%n|");
		for (int i = 1; i < table.length; i++) {
			System.out.printf("%4d", i);
		}
		System.out.printf("  |%n+");
		for (int i = 1; i < table.length; i++) {
			System.out.print("----");
		}
		System.out.println("--+");
		for (int col = 1; col < table[0].length; col++) {
			System.out.print("|");
			for (int row = 1; row < table.length; row++) {
				System.out.printf("%4d", table[row][col]);
			}
			System.out.println("  |");
		}
		System.out.printf("+");
		for (int i = 1; i < table.length; i++) {
			System.out.print("----");
		}
		System.out.printf("--+%n%n");

	}

	public static List<KnapsackItem> solveFractional(List<KnapsackItem> input, double capacity) {
		List<KnapsackItem> items = new ArrayList<KnapsackItem>(input);
		// sort smallest to largest
		Collections.sort(items);
		// reverse to be sorted largest to smallest
		Collections.reverse(items);
		// initialize remaining capacity
		double remainingCapacity = capacity;

		// initialize knapsack
		List<KnapsackItem> knapsack = new ArrayList<KnapsackItem>();

		// intialize index
		int index = 0;

		// while knapsack is not filled
		while (remainingCapacity > 0 && index < items.size()) {
			// take the largest fraction of the best item we can without overfilling the bag
			KnapsackItem itemToAdd = items.get(index).takeWeightAmount(remainingCapacity);
			knapsack.add(itemToAdd);
			index++;
			// subtract the amount we added to the knapsack
			remainingCapacity -= itemToAdd.getWeight();
		}

		return knapsack;
	}

}